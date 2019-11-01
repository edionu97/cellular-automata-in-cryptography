package utils.cp;

import java.util.ArrayList;
import java.util.List;

public class Neighborhood {

    private static final char SKIP_CHARACTER = '-';

    private String neighborhoodTemplate;

    public Neighborhood(final String template) {
        this.neighborhoodTemplate = template;
    }

    public <T> List<T> getValueFromNeighborhood(final List<T> configuration, final int middleCell) {

        final List<Integer> neighborhoodIndices = new ArrayList<>();
        final int middleIndex = neighborhoodTemplate.length() / 2;

        for (int i = 0; i < neighborhoodTemplate.length(); ++i) {
            final char ch = neighborhoodTemplate.charAt(i);
            if (ch == SKIP_CHARACTER) {
                continue;
            }
            neighborhoodIndices.add(i - middleIndex);
        }

        return findNeighborsBasedOnIndices(neighborhoodIndices, configuration, middleCell);
    }

    private <T> List<T> findNeighborsBasedOnIndices(final List<Integer> indices,
                                                    final List<T> configuration,
                                                    final int middleCell) {

        final List<T> elements = new ArrayList<>();
        final int configurationSize = configuration.size();

        //select the values
        indices.forEach(i -> {

            int position = middleCell + i;
            //left overflow go right
            if(position < 0) {
                position += configurationSize;
            }
            //right overflow go left
            if(position >= configurationSize){
                position = position % configurationSize;
            }

            elements.add(configuration.get(position));
        });
        
        return elements;
    }
}

