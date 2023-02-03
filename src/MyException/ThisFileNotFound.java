package MyException;

public class ThisFileNotFound extends RuntimeException {
    public ThisFileNotFound(final String message) {
        super(message);
    }
}
