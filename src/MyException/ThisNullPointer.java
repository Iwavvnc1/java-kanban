package MyException;

public class ThisNullPointer extends RuntimeException {
    public ThisNullPointer(String message, Throwable cause) {
        super(message,cause);
    }
}
