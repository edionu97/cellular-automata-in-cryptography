package cell_programming;

import cell_programming.celullar_automata.ICellularAutomaton;
import cell_programming.population.IPopulation;
import cell_programming.population.Population;
import cell_programming.population.individual.Rule;
import entropy.IEntropy;

import java.util.Random;

public class CellularProgramming implements ICellularProgramming {

    private static final int C = 300;
    private static final int M = 50;
    private static final int GENERATION_NUMBER = 5;

    private final Random random = new Random();
    private final IEntropy<String> entropyCalculator;
    private final ICellularAutomaton cellular;

    private int bytesNumber;


    public CellularProgramming(final IEntropy<String> entropyCalculator,
                               final ICellularAutomaton cellular,
                               final int bytesNumber) {

        this.entropyCalculator = entropyCalculator;
        this.cellular = cellular;
        this.bytesNumber = bytesNumber;
    }

    @Override
    public void evolve() {

        final IPopulation<Rule> population = new Population(random, bytesNumber);
        population.generate();

        double fitness = .0;
        for (int gen = 0; gen < GENERATION_NUMBER; ++gen) {
            //reassign rules
            cellular.reassignRules(
                    population.getGeneratedIndividuals(), bytesNumber
            );

            fitness = computeGlobalFitness();
            this.applyGeneticOperators(population);
        }

        System.out.println(fitness);
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
     * @param population: the rule population that contains the given rules
     */
    private void applyGeneticOperators(final IPopulation<Rule> population) {

    }
}
