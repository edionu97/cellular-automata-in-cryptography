package cell_programming.population;

import java.util.List;

public interface IPopulation <T> {

    /**
     * Generate the population
     */
    void generate();

    /**
     * @return a list with the individuals that are in the population
     */
    List<T> getGeneratedIndividuals();
}
