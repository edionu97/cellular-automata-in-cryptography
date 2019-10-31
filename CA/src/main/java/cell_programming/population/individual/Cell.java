package cell_programming.population.individual;

import utils.string.StringUtils;

public class Cell {

    private Rule cellRule;

    private int cellIndex;

    private volatile StringBuilder configuration;

    public Cell(
            final Rule cellRule,
            final int cellIndex,
            final StringBuilder configuration) {

        this.cellRule = cellRule;
        this.cellIndex = cellIndex;
        this.configuration = configuration;
    }

    public Rule getCellRule() {
        return cellRule;
    }

    public void setCellRule(Rule cellRule) {
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
        return cellRule.applyRuleOnAt(
                configuration.toString(), cellIndex);
    }
}
