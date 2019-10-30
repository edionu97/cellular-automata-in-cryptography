package utils.entropy;

public interface ISearcher {

    /**
     * @param searchableString: the string in which we are searching a pattern
     * @param pattern: the pattern that is searched into that string
     * @return a number representing the number of apparitions of @param pattern in searchableString
     */
    int getNumberOfApparitions(
            final String searchableString, final String pattern);

}
