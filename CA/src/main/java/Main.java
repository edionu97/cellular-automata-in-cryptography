import cell_programming.celullar_automata.CA;
import cell_programming.population.individual.Cell;
import cell_programming.population.individual.Rule;
import entropy.IEntropy;
import entropy.PNSEntropyCalculator;
import utils.entropy.ISearcher;
import utils.entropy.KMPSearcher;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Main {
    public static void main(final String... args) {
        final ISearcher searcher = new KMPSearcher() ;
        final IEntropy entropy = new PNSEntropyCalculator(4, searcher);
    }
}
