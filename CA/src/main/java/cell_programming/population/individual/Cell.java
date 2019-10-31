package cell_programming.population.individual;

public class Cell {

    private Rule cellRule;

    private int cellIndex;

    private StringBuilder configuration;

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

    public void evolve() {
        configuration.setCharAt(
                cellIndex,
                cellRule.applyRuleOnAt(
                        configuration.toString(),
                        cellIndex
                ).charAt(0)
        );
    }
}
