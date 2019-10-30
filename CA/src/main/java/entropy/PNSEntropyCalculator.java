package entropy;

import utils.entropy.ISearcher;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class PNSEntropyCalculator implements IEntropy {

    private final int h;

    private String sequence;
    private final ISearcher searcher;
    private final List<String> subsequences;
    private final MathContext mathContext = new MathContext(10, RoundingMode.HALF_UP);

    public PNSEntropyCalculator(
            final int h, final ISearcher searcher) {
        this.h = h;
        this.searcher = searcher;
        this.subsequences = splitIntoSubsequences();
    }

    @Override
    public void setSequence(final String sequence) {
        this.sequence = sequence;
    }

    @Override
    public double getEntropyValue() { // max value can be h

        double entropy = 0.0;
        for (final String s : subsequences) {
            final double probability = computeProbability(s);

            if (probability == 0) {
                continue;
            }

            entropy += probability * (Math.log(probability) / Math.log(2));
        }

        return Math.abs(entropy);
    }

    /**
     * Generate subsequences of binary data representation of length h
     *
     * @return a list of subsequences
     */
    private List<String> splitIntoSubsequences() {

        final List<String> subsequences = new ArrayList<>();

        for (int i = 0; i < (1 << h); ++i) {
            final String binary = Integer.toBinaryString(i);
            subsequences.add(
                    addZeros(h - binary.length()) + binary
            );
        }

        return subsequences;
    }

    /**
     * Add zeros at the start of the string
     * @param numbers: the number of zeros that will be added
     * @return a string with only 0 values
     */
    private String addZeros(int numbers){

        final StringBuilder stringBuilder = new StringBuilder();
        while (numbers-- > 0){
            stringBuilder.append("0");
        }

        return stringBuilder.toString();
    }

    private double computeProbability(final String s) {
        //computes the probability as favorable cases / total cases
        final double numberOfApparitions = searcher.getNumberOfApparitions(sequence, s);

        //total sequences of length h from pattern
        final double totalSequences = (sequence.length() - (h - 1));

        // the probability of pattern s to appear in sequence
        return numberOfApparitions / totalSequences;
    }
}
