package cell_programming.celullar_automata;

import cell_programming.population.individual.Cell;
import cell_programming.population.individual.Rule;

import java.util.List;

public interface ICellularAutomaton {

    /**
     * Reassign the rules to each cell from cellular automaton
     *
     * @param rules:         a list of rules that will be used
     * @param numberOfRules: the number of rules that are necessary
     */
    void reassignRules(final List<Rule> rules, final int numberOfRules);

    /**
     * Assign the configuration to all cells so that each cell will have the least version of configuration
     * @param newConfiguration: the new configuration
     */
    void assignConfiguration(final StringBuilder newConfiguration);

    /**
     * @return the PNS after the cellular evolution
     */
    StringBuilder evolve();

    List<Cell> getCells();
}
