package cell_programming.population.individual;

import java.util.Map;
import java.util.TreeMap;

public class Rule {

    private final int ruleNumber;
    private final RuleType type;
    private Map<String, String> ruleMap = new TreeMap<>();

    private Rule(final int ruleNumber, final RuleType type) {
        this.ruleNumber = ruleNumber;
        this.type = type;
        populateRuleMap();
    }

    public static Rule build(final int ruleNumber) {
        return new Rule(
                ruleNumber, getRuleType(ruleNumber));
    }

    private static RuleType getRuleType(int ruleNumber) {
        return ruleNumber < 256 ? RuleType.ONE : RuleType.TWO;
    }


    public int getRuleNumber() {
        return ruleNumber;
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

    private String zeroPaddedAtStart(
            int numberOfZeros, final String representation) {

        final StringBuilder stringBuilder = new StringBuilder();

        while (numberOfZeros-- > 0) {
            stringBuilder.append("0");
        }
        stringBuilder.append(representation);

        return stringBuilder.toString();
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
        final int L = log2(N);
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
}
