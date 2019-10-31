package cell_programming.population;

import cell_programming.population.individual.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population {

    private static final int RULE_TYPE_2_BOUND = 1 << 30;
    private static final int RULE_TYPE_1_BOUND = 1 << 8 - 1;
    private static final double PROBABILITY_GENERATION = .12;

    private final Random random;
    private List<Rule> rules;
    private int populationSize;

    public Population(
            final Random random,
            final int populationSize) {
        this.random = random;
        this.populationSize = populationSize;
    }

    public void generate() {

        rules = new ArrayList<>();
        for (int i = 0; i < populationSize; ++i) {
            final double prop = random.nextDouble();

            int number = prop < PROBABILITY_GENERATION ?
                    random.nextInt(RULE_TYPE_2_BOUND) :
                    random.nextInt(RULE_TYPE_1_BOUND);

            rules.add(Rule.build(number));
        }
    }

    public List<Rule> getGeneratedRules() {
        return rules != null ? rules : new ArrayList<>();
    }
}
