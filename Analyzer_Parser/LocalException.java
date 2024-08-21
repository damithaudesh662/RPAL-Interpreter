package Analyzer_Parser;
@SuppressWarnings("serial")
public class LocalException extends Exception {
    // Constructors
    public LocalException() {
        super();
    }

    public LocalException(String message) {
        super(message);
    }

    public LocalException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocalException(Throwable cause) {
        super(cause);
    }
}