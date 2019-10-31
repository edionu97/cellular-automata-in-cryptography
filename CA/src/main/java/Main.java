import cell_programming.population.Population;
import cell_programming.population.individual.Cell;
import cell_programming.population.individual.Rule;
import entropy.IEntropy;
import entropy.PNSEntropyCalculator;
import utils.entropy.ISearcher;
import utils.entropy.KMPSearcher;

import java.util.Random;

public class Main {
    public static void main(final String... args) {
        final ISearcher searcher = new KMPSearcher() ;
        final IEntropy entropy = new PNSEntropyCalculator(4, searcher);

        entropy.setSequence("101101010011101010111");

        System.out.println(entropy.getEntropyValue());

        final Rule rule = Rule.build(95593893);

        final StringBuilder configuration = new StringBuilder("01110101011101010111010101110101");
        System.out.println(configuration);

        final Cell cell = new Cell(rule, 2, configuration);
        cell.evolve();

        System.out.println(configuration);

        final Random random = new Random();

        Population population = new Population(random, 5);

        population.generate();
    }
}
