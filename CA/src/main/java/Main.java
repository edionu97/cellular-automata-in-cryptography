import cell_programming.CellularProgramming;
import cell_programming.ICellularProgramming;
import cell_programming.celullar_automata.CellularAutomaton;
import cell_programming.celullar_automata.ICellularAutomaton;
import entropy.IEntropy;
import entropy.PNSEntropyCalculator;
import utils.cp.Neighborhood;
import utils.entropy.ISearcher;
import utils.entropy.KMPSearcher;
import utils.threads.ThreadHelper;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Main {
    public static void main(final String... args) {


//        final ISearcher searcher = new KMPSearcher();
//        final IEntropy<String> entropy = new PNSEntropyCalculator(4, searcher);
//        final ICellularAutomaton cellular = new CellularAutomaton();
//
//        final ICellularProgramming programming = new CellularProgramming(
//                entropy, cellular, 50
//        );
//
//        programming.evolve();

        Neighborhood neighborhood = new Neighborhood("1111111");

        neighborhood.getValueFromNeighborhood(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15), 5);


    }
}
