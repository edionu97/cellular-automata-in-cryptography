import cell_programming.CellularProgramming;
import cell_programming.ICellularProgramming;
import cell_programming.celullar_automata.CellularAutomaton;
import cell_programming.celullar_automata.ICellularAutomaton;
import cell_programming.population.individual.Rule;
import entropy.IEntropy;
import entropy.PNSEntropyCalculator;
import utils.entropy.ISearcher;
import utils.entropy.KMPSearcher;

import java.util.List;

public class Main {
    public static void main(final String... args) {


        final ISearcher searcher = new KMPSearcher();
        final IEntropy<String> entropy = new PNSEntropyCalculator(4, searcher);
        final ICellularAutomaton cellular = new CellularAutomaton();

        final ICellularProgramming programming = new CellularProgramming(
                entropy, cellular, 50, (index, fitness) -> System.out.println("gen" + index + " " + fitness)
        );

        final List<Rule> rules = programming.evolve();

        cellular.reassignRules(rules, rules.size());

        System.out.println(rules);
    }
}
