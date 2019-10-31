import cell_programming.CellularProgramming;
import cell_programming.celullar_automata.CA;
import cell_programming.population.individual.Cell;
import cell_programming.population.individual.Rule;
import entropy.IEntropy;
import entropy.PNSEntropyCalculator;
import utils.entropy.ISearcher;
import utils.entropy.KMPSearcher;

public class Main {
    public static void main(final String... args) {
        final ISearcher searcher = new KMPSearcher() ;
        final IEntropy entropy = new PNSEntropyCalculator(4, searcher);
        final CA cellular = new CA();

        final CellularProgramming programming = new CellularProgramming(
                entropy, cellular, 50
        );

        programming.evolve();
    }
}
