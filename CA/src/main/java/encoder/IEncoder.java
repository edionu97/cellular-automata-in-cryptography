package encoder;

public interface IEncoder {
    /**
     * Encodes the given string into another string
     * @param toBeEncoded: the string that will be encoded
     * @return a string representing the encoded value
     */
    String encode(final String toBeEncoded);

    /**
     * Decodes a given string
     * @param toBeDecoded: the string that will be decoded
     * @return a string which represents the decoded string
     */
    String decode(final String toBeDecoded);
}
