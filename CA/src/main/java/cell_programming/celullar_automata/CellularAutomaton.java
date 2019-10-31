package cell_programming.celullar_automata;

import cell_programming.population.individual.Cell;
import cell_programming.population.individual.Rule;

import java.util.ArrayList;
import java.util.List;

public class CellularAutomaton implements ICellularAutomaton {

    private final List<Cell> cells = new ArrayList<>();
    private StringBuilder configuration;

    @Override
    public void reassignRules(final List<Rule> rules, final int N) {

        if(rules.size() != N){
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
        cells.forEach(cell ->
                builder.setCharAt(
                        cell.getCellIndex(),
                        cell.evolve().charAt(0)
                )
        );

        return builder;
    }


    private void populateCells(final List<Rule> rules) {
        int index = 0;
        for (final Rule rule : rules) {
            cells.add(new Cell(rule, index++, configuration));
        }
    }

}
