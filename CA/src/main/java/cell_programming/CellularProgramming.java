package cell_programming;

import cell_programming.celullar_automata.CA;
import cell_programming.population.Population;
import entropy.IEntropy;

import java.util.Random;

public class CellularProgramming {

    private static final int C = 300;
    private static final int M = 50;
    private static final int GENERATION_NUMBER = 1;
    private final CA cellular;
    private final IEntropy entropyCalculator;
    private final Random random = new Random(1);
    private int N;
    private Population population;


    public CellularProgramming(final IEntropy entropyCalculator,
                               final CA cellular,
                               final int N) {

        this.entropyCalculator = entropyCalculator;
        this.cellular = cellular;
        this.N = N;
    }

    public void evolve() {

        population = new Population(random, N);
        population.generate();


        for (int gen = 0; gen < GENERATION_NUMBER; ++gen) {
            //reassign rules
            cellular.reassignRules(
                    population.getGeneratedRules(), N
            );

            double fitness = getFitness();

            System.out.println(fitness);
        }

    }

    private double getFitness() {

        double totalEntropy = .0;
        for (int i = 0; i < C; ++i) {
            //generate the configuration
            StringBuilder configuration = new StringBuilder(
                    getRandomConfiguration(N)
            );

            for(int j = 0; j < M; ++j){
                //assign the configuration
                cellular.assignConfiguration(configuration);
                configuration = cellular.evolve();
            }

            //set the sequence and calculate the entropy value
            entropyCalculator.setSequence(configuration.toString());
            totalEntropy += entropyCalculator.getEntropyValue();
        }

        return totalEntropy / C;
    }

    private String getRandomConfiguration(int bitNr) {

        final StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < bitNr; ++i) {
            stringBuilder.append(random.nextBoolean() ? "1" : "0");
        }

        return stringBuilder.toString();
    }
}