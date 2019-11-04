package utils.string;

public class StringUtils {

    public static String zeroPaddedAtStart(final String representation,
                                           final int desiredZeros) {
        final int nr = desiredZeros - representation.length();
        return zeroPaddedAtStart(nr, representation);
    }

    public static String zeroPaddedAtStart(int numberOfZeros, final String representation) {

        final StringBuilder stringBuilder = new StringBuilder();

        while (numberOfZeros-- > 0) {
            stringBuilder.append("0");
        }
        stringBuilder.append(representation);

        return stringBuilder.toString();
    }

}
