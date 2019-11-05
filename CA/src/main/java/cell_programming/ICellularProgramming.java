package cell_programming;

import cell_programming.population.individual.Rule;

import java.util.List;

public interface ICellularProgramming {
    /**
     * This function is used in order to evolve the population
     * @returns a list of rules that will be applied in order to encode the key
     */
    List<Rule> evolve();
}
