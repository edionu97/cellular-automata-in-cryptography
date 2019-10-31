package utils.string;

public class StringUtils {

    public static String zeroPaddedAtStart(int numberOfZeros, final String representation) {

        final StringBuilder stringBuilder = new StringBuilder();

        while (numberOfZeros-- > 0) {
            stringBuilder.append("0");
        }
        stringBuilder.append(representation);

        return stringBuilder.toString();
    }

}
