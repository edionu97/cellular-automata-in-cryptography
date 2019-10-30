package utils.entropy;

public class KMPSearcher implements ISearcher {

    @Override
    public int getNumberOfApparitions(
            final String searchableString, final String pattern) {

        //get the longest prefix which is also the suffix of pattern
        final int[] next = getLongestPrefixWhichIsAlsoTheSuffix(pattern);

        int k = 0, count = 0;
        for (int i = 0; i < searchableString.length(); ++i) {

            // compare character by character the pattern and the string
            while (k > 0 && searchableString.charAt(i) != pattern.charAt(k)) {
                k = next[k - 1];
            }

            //if the characters are equal than continue
            if (searchableString.charAt(i) == pattern.charAt(k)) {
                ++k;
            }

            //if we matched the all pattern than count the match and move to match next pattern
            if (k == pattern.length()) {
                k = next[k - 1];
                ++count;
            }
        }


        return count;
    }


    private int[] getLongestPrefixWhichIsAlsoTheSuffix(final String pattern) {

        final int[] next = new int[pattern.length()];
        int k = 0;

        for (int i = 1; i < next.length; ++i) {
            // as long as the prefix is different from the suffix iterate through
            while (k > 0 && pattern.charAt(k) != pattern.charAt(i)) {
                k = next[k - 1];
            }
            //if the value is equal than the pattern of prefix increases by one
            if (pattern.charAt(k) == pattern.charAt(i)) {
                ++k;
            }
            next[i] = k;
        }

        return next;
    }
}
