import entropy.IEntropy;
import entropy.PNSEntropyCalculator;
import utils.entropy.ISearcher;
import utils.entropy.KMPSearcher;

public class Main {
    public static void main(final String... args) {
        final ISearcher searcher = new KMPSearcher() ;
        final IEntropy entropy = new PNSEntropyCalculator(4, searcher);

        entropy.setSequence("101101010011101010111");

        System.out.println(entropy.getEntropyValue());

    }
}
