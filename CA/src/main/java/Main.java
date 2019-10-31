import cell_programming.CellularProgramming;
import cell_programming.ICellularProgramming;
import cell_programming.celullar_automata.CellularAutomaton;
import cell_programming.celullar_automata.ICellularAutomaton;
import entropy.IEntropy;
import entropy.PNSEntropyCalculator;
import utils.entropy.ISearcher;
import utils.entropy.KMPSearcher;

public class Main {
    public static void main(final String... args) {

        final ISearcher searcher = new KMPSearcher();
        final IEntropy entropy = new PNSEntropyCalculator(4, searcher);
        final ICellularAutomaton cellular = new CellularAutomaton();

        final ICellularProgramming programming = new CellularProgramming(
                entropy, cellular, 50
        );

        programming.evolve();
    }
}
