package entropy;

public interface IEntropy<T> {
    double getEntropyValue(final T value);

    void setSequence(final String sequence);
}
