package MyException;

public class SubWithoutEpicId extends RuntimeException {
    public SubWithoutEpicId(final String message) {
        super(message);
    }
}
