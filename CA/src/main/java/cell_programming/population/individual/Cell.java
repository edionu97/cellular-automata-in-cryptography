package cell_programming.population.individual;

import entropy.IEntropy;

public class Cell {

    private Rule cellRule;

    private int cellIndex;

    private volatile StringBuilder configuration, cellBitStream;

    public Cell(
            final Rule cellRule,
            final int cellIndex,
            final StringBuilder configuration) {

        this.cellRule = cellRule;
        this.cellIndex = cellIndex;
        this.configuration = configuration;
        this.cellBitStream = new StringBuilder();
    }

    public Rule getCellRule() {
        return cellRule;
    }

    public void setCellRule(final Rule cellRule) {
        cellBitStream = new StringBuilder();
        this.cellRule = cellRule;
    }

    public StringBuilder getConfiguration() {
        return configuration;
    }

    public void setConfiguration(final StringBuilder configuration) {
        this.configuration = configuration;
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(final int cellIndex) {
        this.cellIndex = cellIndex;
    }

    public synchronized String evolve() {

        final String result = cellRule.applyRuleOnAt(
                configuration.toString(), cellIndex);

        cellBitStream.append(result);

        return result;
    }

    public void computeRuleFitness(final IEntropy entropy) {
        entropy.setSequence(cellBitStream.toString());
        final double ruleFitness = entropy.getEntropyValue();
        getCellRule().setFitness(ruleFitness);
    }
}
