package utils.cp;

import cell_programming.population.individual.Rule;
import cell_programming.population.individual.RuleType;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static utils.string.StringUtils.zeroPaddedAtStart;

public class GeneticOperatorHelper {

    public static Rule select(final Rule childOne,
                              final Rule toBeReplacedRule,
                              final Rule childTwo,
                              final Random random) {

        final List<Rule> newRules = Stream
                .of(childOne, childTwo)
                .filter(rule -> isSame(rule, toBeReplacedRule))
                .collect(Collectors.toList());

        //if we have a child of the same type as the rule that will be replaced then select randomly a child
        if (!newRules.isEmpty()) {
            return newRules.get(random.nextInt(newRules.size()));
        }

        return random.nextDouble() <= .5 ? childOne : childTwo;
    }


    /**
     * Crossover the parents in order to obtain a new child
     *
     * @param mother: the first parent
     * @param father: the second parent
     * @param random: the random generator
     * @return a new rule which represent the child of the parents
     */
    public static Rule crossoverSpecies(final Rule mother,
                                        final Rule father,
                                        final Random random) {

        final int BOUND = 8;

        //generate a random number which represents the bound
        int splitPoint = random.nextInt(BOUND);
        while (splitPoint == 0) {
            splitPoint = random.nextInt(BOUND);
        }

        if (isSame(mother, father)) {
            return crossoverSameType(mother, father, splitPoint);
        }

        return crossoverDifferentTypes(mother, father, splitPoint);
    }

    /**
     * Perform the mutation with a fixed probability
     *
     * @param rule:                 the rule that we will mutate
     * @param random:               the random generator
     * @param MUTATION_PROBABILITY: the probability of mutation
     * @return a new rule
     */
    public static Rule mutate(final Rule rule,
                              final Random random,
                              final double MUTATION_PROBABILITY) {

        final int N = (rule.getType().equals(RuleType.ONE) ? 8 : 32);

        long ruleNumber = rule.getRuleNumber();
        final StringBuilder binary = new StringBuilder(
                zeroPaddedAtStart(Long.toBinaryString(ruleNumber), N)
        );

        for (int i = 0; i < N; ++i) {
            final double probability = random.nextDouble();
            if (probability > MUTATION_PROBABILITY) {
                continue;
            }
            binary.setCharAt(i, binary.charAt(i) == '0' ? '1' : '0');
        }

        return Rule.build(Long.valueOf(binary.toString(), 2));
    }


    /**
     * @param mother: the mother
     * @param father: the father
     * @return true if those parents belong to the same species
     */
    public static boolean isSame(final Rule mother, final Rule father) {
        return mother.getType().equals(father.getType());
    }


    /**
     * Check if the mother and father have the same species
     *
     * @param mother: the mother of the child
     * @param father: the father of the child
     * @return a child of the mother and father
     */
    private static Rule crossoverSameType(final Rule mother,
                                          final Rule father,
                                          final int splitPoint) {

        //get the binary representation of rules
        final String motherBinaryRep = zeroPaddedAtStart(
                Long.toBinaryString(
                        mother.getRuleNumber()
                ),
                mother.getType().equals(RuleType.ONE) ? 8 : 32
        );
        final String fatherBinaryRep = zeroPaddedAtStart(
                Long.toBinaryString(
                        father.getRuleNumber()
                ),
                father.getType().equals(RuleType.ONE) ? 8 : 32
        );

        // get the child
        final String childBinaryRepresentation =
                motherBinaryRep.substring(0, splitPoint) + fatherBinaryRep.substring(splitPoint);

        // get the binary representation of the child
        return Rule.build(
                Long.valueOf(childBinaryRepresentation, 2)
        );
    }

    /**
     * Crossover if the mother and father are from different types
     *
     * @param mother: the mother of the child
     * @param father: the father of the child
     * @return a child of the mother and father
     */
    private static Rule crossoverDifferentTypes(final Rule mother,
                                                final Rule father,
                                                final int splitPoint) {

        //get the binary representation of rules
        final String motherBinaryRep = zeroPaddedAtStart(
                Long.toBinaryString(
                        mother.getRuleNumber()
                ),
                mother.getType().equals(RuleType.ONE) ? 8 : 32
        );
        final String fatherBinaryRep = zeroPaddedAtStart(
                Long.toBinaryString(
                        father.getRuleNumber()
                ),
                father.getType().equals(RuleType.ONE) ? 8 : 32
        );

        //if the mother is of type one
        if (mother.getType().equals(RuleType.ONE)) {
            final String childBinRep =
                    motherBinaryRep.substring(0, splitPoint) +
                            putGenesFromTypeTwoFather(fatherBinaryRep, motherBinaryRep, splitPoint);
            //build the new rule
            return Rule.build(Long.valueOf(childBinRep, 2));
        }


        final String childBinRep = putGenesFromTypeTwoMother(motherBinaryRep, fatherBinaryRep, splitPoint);
        return Rule.build(Long.valueOf(childBinRep, 2));
    }

    /**
     * crossover if father is of type 2
     *
     * @param father
     * @param mother
     * @param splitPoint
     * @return
     */
    private static String putGenesFromTypeTwoFather(final String father,
                                                    final String mother,
                                                    final int splitPoint) {

        // calculate the index from binary representation (0 is 7 and 7 is 0)
        final int SIZE = father.length() - 1;

        //iterate through genes
        final StringBuilder stringBuilder = new StringBuilder();
        for (int j = 8 - splitPoint - 1; j >= 0; --j) {
            //get the number of ones and zeros from gene vicinity
            final int nrO = (int) Stream.of(
                    father.charAt(SIZE - (2 * j)),
                    father.charAt(SIZE - (2 * j + 1)),
                    father.charAt(SIZE - (2 * j + 16)),
                    father.charAt(SIZE - (2 * j + 17))).filter(x -> x == '1').count();
            final int nrZ = 4 - nrO;

            //if the number of zeros is equal to numbers of 1 than keep the mother gene
            if (nrO == nrZ) {
                stringBuilder.append(mother.charAt(8 - 1 - j));
                continue;
            }

            //if the number of 1s is greater than the number of 0s we put 1 otherwise 0
            stringBuilder.append(nrO > nrZ ? '1' : '0');
        }

        return stringBuilder.toString();
    }


    /**
     * crossover if mother if of type 2
     *
     * @param mother
     * @param father
     * @param splitPoint
     * @return
     */
    private static String putGenesFromTypeTwoMother(final String mother,
                                                    final String father,
                                                    final int splitPoint) {

        // calculate the index from binary representation (0 is 7 and 7 is 0)
        final int SIZE = mother.length() - 1;

        //iterate through genes
        final StringBuilder geneModifier = new StringBuilder(mother);

        for (int j = 8 - splitPoint - 1; j >= 0; --j) {
            //get all genes from group
            final int geneAIndex = SIZE - (2 * j);
            final int geneBIndex = SIZE - (2 * j + 1);
            final int geneCIndex = SIZE - (2 * j + 16);
            final int geneDIndex = SIZE - (2 * j + 17);

            //get the number of ones and zeros from gene vicinity
            final int nrO = (int) Stream.of(
                    mother.charAt(geneAIndex),
                    mother.charAt(geneBIndex),
                    mother.charAt(geneCIndex),
                    mother.charAt(geneDIndex)).filter(x -> x == '1').count();
            final int nrZ = 4 - nrO;

            // keep the same if gene number are equal
            if (nrO == nrZ) {
                continue;
            }

            // get the father gene
            final char fatherGene = father.charAt(j);

            //get the predominant gene
            final char predominantGene = nrO > nrZ ? '1' : '0';
            final char valueForNonPredominant = fatherGene == '1' ? '0' : '1';

            replaceGene(geneModifier, predominantGene, fatherGene, valueForNonPredominant, geneAIndex);
            replaceGene(geneModifier, predominantGene, fatherGene, valueForNonPredominant, geneBIndex);
            replaceGene(geneModifier, predominantGene, fatherGene, valueForNonPredominant, geneCIndex);
            replaceGene(geneModifier, predominantGene, fatherGene, valueForNonPredominant, geneDIndex);
        }

        return geneModifier.toString();
    }

    private static void replaceGene(final StringBuilder stringBuilder,
                                    final char predominantGene,
                                    final char nonPredominantGene,
                                    final char newValueForPredominantGene,
                                    final int geneIndex) {

        final char gene = stringBuilder.charAt(geneIndex);

        if (gene != predominantGene) {
            stringBuilder.setCharAt(geneIndex, nonPredominantGene);
            return;
        }

        stringBuilder.setCharAt(geneIndex, newValueForPredominantGene);
    }

    /**
     * Transform a rule from a type (One or Two) into another type
     *
     * @param rule:         the rule that will be transformed
     * @param toBeReplaced: the type in which we want to transform the rule
     * @return a new rule, representing the transformation of the rule in the type @param type
     */
    private static Rule transform(final Rule rule,
                                  final Rule toBeReplaced,
                                  final Random random) {

        //if the rule is of the same  type do nothing
        if (isSame(toBeReplaced, rule)) {
            return rule;
        }

        return crossoverSpecies(toBeReplaced, rule, random);
    }

}
