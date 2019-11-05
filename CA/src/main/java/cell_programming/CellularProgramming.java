package cell_programming;

import cell_programming.celullar_automata.ICellularAutomaton;
import cell_programming.population.IPopulation;
import cell_programming.population.Population;
import cell_programming.population.individual.Rule;
import entropy.IEntropy;
import utils.cp.Neighborhood;

import java.util.*;
import java.util.function.BiConsumer;

import static utils.cp.GeneticOperatorHelper.*;

public class CellularProgramming implements ICellularProgramming {


    private static final double MUTATION_PROBABILITY = .01;
    private static final int C = 300;//number of initial configuration
    private static final int M = 4096;//number of evolving steps
    private static final int GENERATION_NUMBER = 100; //number of generations
    private static final Neighborhood neighborhood = new Neighborhood("1--1--1");

    private final Random random = new Random();
    private final IEntropy<String> entropyCalculator;
    private final ICellularAutomaton cellular;
    private final int bytesNumber;
    private final BiConsumer<Integer, Double> generationUpdater;

    public CellularProgramming(final IEntropy<String> entropyCalculator,
                               final ICellularAutomaton cellular,
                               final int keyBitsNumber,
                               final BiConsumer<Integer, Double> updater) {

        this.entropyCalculator = entropyCalculator;
        this.cellular = cellular;
        this.bytesNumber = keyBitsNumber;
        this.generationUpdater = updater;
    }

    @Override
    public List<Rule> evolve() {


        final IPopulation<Rule> population = new Population(random, bytesNumber);
        population.generate();

        Map.Entry<Double, List<Rule>> selectedRules = new AbstractMap.SimpleEntry<>(.0, new ArrayList<>());
        for (int gen = 0; gen < GENERATION_NUMBER; ++gen) {
            //reassign rules
            cellular.reassignRules(
                    population.getGeneratedIndividuals(), bytesNumber
            );
            //compute the fitness
            final double fitness = computeGlobalFitness();
            //inform the updater
            generationUpdater.accept(gen, fitness);
            //retain the best configuration obtained
            if(selectedRules.getKey() < fitness){
                selectedRules = new AbstractMap.SimpleEntry<>(fitness, population.getGeneratedIndividuals());
            }

            this.applyGeneticOperators(population);
        }

        final double fitness = computeGlobalFitness();
        generationUpdater.accept(GENERATION_NUMBER, fitness);

        return selectedRules.getValue();
    }

    /**
     * @return calculates and returns a double value which represents the value of avg entropy
     */
    private double computeGlobalFitness() {

        double totalEntropy = .0;
        for (int i = 0; i < C; ++i) {

            //generate the configuration
            StringBuilder configuration = new StringBuilder(
                    getRandomConfiguration(bytesNumber)
            );
            for (int j = 0; j < M; ++j) {
                //assign the configuration
                cellular.assignConfiguration(configuration);
                configuration = cellular.evolve();
            }

            //set the sequence and calculate the entropy value
            totalEntropy += entropyCalculator.getEntropyValue(
                    configuration.toString()
            );
        }

        computeRuleFitness();

        return totalEntropy / C;
    }

    /**
     * Get a random configuration of size @param bitNr
     *
     * @param bitNr: the number that represents the length of the configuration
     * @return a string representing the random configuration
     */
    private String getRandomConfiguration(int bitNr) {

        final StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < bitNr; ++i) {
            stringBuilder.append(random.nextBoolean() ? "1" : "0");
        }

        return stringBuilder.toString();
    }

    /**
     * Compute te fitness for each rule from population
     */
    private void computeRuleFitness() {
        cellular
                .getCells()
                .forEach(
                        cell -> cell.computeRuleFitness(entropyCalculator)
                );
    }

    /**
     * In this function the genetic operators will be applied
     *
     * @param population: the rule population that contains the given rules
     */
    private void applyGeneticOperators(final IPopulation<Rule> population) {

        final List<Rule> rules = population.getGeneratedIndividuals();

        //the new rules for next population
        final List<Rule> newRules = new ArrayList<>();

        int index = 0;
        for (final Rule rule : rules) {
            //get all related cells based on the neighborhood pattern
            final List<Rule> relatedCells = neighborhood.getValueFromNeighborhood(rules, index++);
            //get the number of best rules and the best rules
            final Map.Entry<List<Rule>, Integer> bestRules = bestRulesFromNeighborhoodCount(relatedCells, rule);
            //depending of the number of best rules from neighborhood, apply different types of crossover
            switch (bestRules.getValue()) {
                //if the rule is the best from it's neighborhood is 0 we keep it
                case 0: {
                    newRules.add(
                            Rule.build(
                                    rule.getRuleNumber()
                            )
                    );
                    break;
                }
                //if the number of best from it's neighborhood is 1 than we mutate
                case 1: {
                    crossoverOneBetter(
                            newRules,
                            rule,
                            bestRules);
                    break;
                }
                //if the number of best from it's neighborhood is 2 crossover its parents, select a random child and mutate it
                case 2: {
                    crossoverTwoBetter(
                            newRules,
                            rule,
                            bestRules.getKey()
                    );
                    break;
                }
                //if the number of best rules from neighborhood are greater than 2 then randomly select 2 and apply crossover
                default: {
                    final int size = bestRules.getKey().size();
                    crossoverTwoBetter(
                            newRules,
                            rule,
                            Arrays.asList(
                                    bestRules.getKey().get(random.nextInt(size)),
                                    bestRules.getKey().get(random.nextInt(size))
                            )
                    );
                    break;
                }
            }

        }

        //set the population
        population.setPopulation(newRules);
    }

    /**
     * This function will handle the crossover if the number of better cells from
     * neighborhood is exactly two
     *
     * @param newRules:          the list of new rules that will obtain
     * @param rule:              the current rules
     * @param numberOfBestRules: best rules from rule's neighborhood
     */
    private void crossoverTwoBetter(final List<Rule> newRules,
                                    final Rule rule,
                                    final List<Rule> numberOfBestRules) {

        //get the rule parents (it's mother and it's father)
        final Rule mother = numberOfBestRules.get(0);
        final Rule father = numberOfBestRules.get(1);

        // mate the parents in order to obtain a child
        final Rule childOne = crossoverSpecies(mother, father, random);
        final Rule childTwo = crossoverSpecies(father, mother, random);

        //add new characteristics to child
        final Rule childOneMutated = mutate(childOne, random, MUTATION_PROBABILITY);
        final Rule childTwoMutated = mutate(childTwo, random, MUTATION_PROBABILITY);

        //select a child from the mutated children and add it into population
        newRules.add(
                select(childOneMutated, rule, childTwoMutated, random)
        );
    }

    /**
     * This function will handle the crossover if the number of better cells from
     * neighborhood is exactly one
     *
     * @param newRules:          the list of new rules that will obtain
     * @param rule:              the current rules
     * @param numberOfBestRules: best rules from rule's neighborhood
     */
    private void crossoverOneBetter(final List<Rule> newRules, Rule rule,
                                    final Map.Entry<List<Rule>, Integer> numberOfBestRules) {

        final Rule bestRule = numberOfBestRules.getKey().get(0);

        //assume that the rules have the same type
        Rule newRule = mutate(bestRule, random, MUTATION_PROBABILITY);
        //if they are from different type then keep the initial value
        if (!bestRule.getType().equals(rule.getType())) {
            newRule = rule;
        }

        newRules.add(Rule.build(newRule.getRuleNumber()));
    }


    /**
     * Computes the number of best rules from cell's neighborhood
     *
     * @param neighborhood: the cells neighborhood
     * @param middleRule:   the mai rule
     * @return a pair of two objects [number_of_rules, [rules...]]
     */
    private Map.Entry<List<Rule>, Integer> bestRulesFromNeighborhoodCount(final List<Rule> neighborhood,
                                                                          final Rule middleRule) {

        final List<Rule> betterRules = new ArrayList<>();

        //count how many rules
        int count = 0;
        for (final Rule rule : neighborhood) {
            if (rule.getFitness() <= middleRule.getFitness()) {
                continue;
            }
            ++count;
            betterRules.add(rule);
        }

        return new AbstractMap.SimpleEntry<>(betterRules, count);
    }

}
