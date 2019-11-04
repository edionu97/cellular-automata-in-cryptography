package cell_programming.population.individual;

import java.util.HashMap;
import java.util.Map;

import static utils.string.StringUtils.zeroPaddedAtStart;

public class Rule {

    private final RuleType type;
    private final Map<String, String> ruleMap = new HashMap<>();
    private long ruleNumber;
    private double fitness;

    private Rule(final long ruleNumber, final RuleType type) {
        this.ruleNumber = ruleNumber;
        this.type = type;
        populateRuleMap();
    }

    public static Rule build(final long ruleNumber) {
        return new Rule(
                ruleNumber, getRuleType(ruleNumber));
    }

    private static RuleType getRuleType(long ruleNumber) {
        return ruleNumber < 256 ? RuleType.ONE : RuleType.TWO;
    }


    public long getRuleNumber() {
        return ruleNumber;
    }

    public void setRuleNumber(final long ruleNumber) {
        this.ruleNumber = ruleNumber;
        populateRuleMap();
    }

    public RuleType getType() {
        return type;
    }

    public String applyRuleOnAt(
            final String configuration, final int index) {

        return ruleMap.get(
                getNeighborhood(configuration, index)
        );
    }

    private void populateRuleMap() {
        ruleMap.clear();

        int n = 2 + 1;
        if (getType().equals(RuleType.TWO)) {
            n = 2 * 2 + 1;
        }

        final String representation = Long.toBinaryString(
                getRuleNumber()
        );

        populateRuleTable(zeroPaddedAtStart(
                (1 << n) - representation.length(), representation));
    }

    private void populateRuleTable(final String configuration) {

        final int N = configuration.length(), L = log2(N);

        for (int cellIndex = N - 1; cellIndex >= 0; --cellIndex) {
            //get the rule bit map
            final String representation = Long.toBinaryString(cellIndex);
            final String paddedString = zeroPaddedAtStart(
                    L - representation.length(), representation);
            final String bitValue = configuration.charAt(configuration.length() - cellIndex - 1) + "";
            //add into map
            ruleMap.put(paddedString, bitValue);
        }
    }

    private int log2(int x) {
        return (int) (Math.log(x) / Math.log(2));
    }

    private String getNeighborhood(final String configuration, final int index) {

        //get the rule length
        final int N = configuration.length();
        final int L = RuleType.ONE.equals(getType()) ? 3 : 5;

        final StringBuilder builder = new StringBuilder();

        //set the value from the middle bit to value of the chat ar index
        builder.setLength(L);
        builder.setCharAt(
                L / 2, configuration.charAt(index)
        );

        buildLeftSide(configuration, index, N, L, builder);
        buildRightSide(configuration, index, N, L, builder);

        return builder.toString();
    }

    private void buildRightSide(final String configuration,
                                final int index,
                                final int n,
                                final int l,
                                final StringBuilder builder) {
        //build the right side
        int poz = index;
        for (int i = 0; i < l / 2; ++i) {
            builder.setCharAt(
                    l / 2 + i + 1,
                    configuration.charAt(++poz % n)
            );
        }
    }

    private void buildLeftSide(final String configuration,
                               final int index,
                               final int n,
                               final int l,
                               final StringBuilder builder) {
        //build the left side
        int poz = index;
        for (int i = 0; i < l / 2; ++i) {
            if (--poz < 0) {
                poz = n - 1;
            }
            builder.setCharAt(
                    l / 2 - i - 1,
                    configuration.charAt(poz)
            );
        }
    }


    public double getFitness() {
        return fitness;
    }

    public void setFitness(final double fitness) {
        this.fitness = fitness;
    }

    @Override
    public String toString() {
        return String.format("rno %d = %f", ruleNumber, fitness);
    }
}
