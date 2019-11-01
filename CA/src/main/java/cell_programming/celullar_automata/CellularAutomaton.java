package cell_programming.celullar_automata;

import cell_programming.population.individual.Cell;
import cell_programming.population.individual.Rule;
import utils.threads.ThreadHelper;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public class CellularAutomaton implements ICellularAutomaton {

    private final List<Cell> cells = new ArrayList<>();
    private volatile StringBuilder configuration;

    private ThreadHelper threadHelper = ThreadHelper.getInstance();


    /**
     * This function in used to add the new rules to each cell
     *
     * @param rules: a list of rules that will be used
     * @param N:     the number of the rules necessary to encode N bites
     */
    @Override
    //todo parallel
    public void reassignRules(final List<Rule> rules, final int N) {

        if (rules.size() != N) {
            throw new RuntimeException(
                    "Each cell is responsible with a byte of information, so you must have enough rules for each cell"
            );
        }

        if (cells.isEmpty()) {
            populateCells(rules);
            return;
        }

        int index = 0;
        for (final Cell cell : cells) {
            cell.setCellIndex(index);
            cell.setConfiguration(configuration);
            cell.setCellRule(rules.get(index++));
        }
    }

    @Override
    public void assignConfiguration(final StringBuilder newConfiguration) {
        this.configuration = newConfiguration;
        cells.forEach(cell -> cell.setConfiguration(newConfiguration));
    }

    @Override
    public StringBuilder evolve() {

        final StringBuilder builder = new StringBuilder();
        builder.setLength(configuration.length());

        final List<Future<List<Map.Entry<Integer, Character>>>> precessedList = threadHelper.distributeOnThreadsAndSubmit(
                cells, cellBatch -> {
                    //process the batch
                    final List<Map.Entry<Integer, Character>> processedBatch = new ArrayList<>();
                    cellBatch.forEach(cell -> {
                        final int cellIndex = cell.getCellIndex();
                        final char character = cell.evolve().charAt(0);
                        processedBatch.add(new AbstractMap.SimpleEntry<>(cellIndex, character));
                    });
                    return processedBatch;
                }
        );

        precessedList.forEach(listFuture -> {
            try {
                listFuture.get().forEach(result -> {
                    builder.setCharAt(result.getKey(), result.getValue());
                });
            } catch (final Exception ex) {
                ex.printStackTrace();
            }
        });

        return builder;
    }

    @Override
    public List<Cell> getCells() {
        return cells
                ;
    }

    private void populateCells(final List<Rule> rules) {
        int index = 0;
        for (final Rule rule : rules) {
            cells.add(new Cell(rule, index++, configuration));
        }
    }
}
