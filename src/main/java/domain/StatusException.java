package domain;

public class StatusException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public StatusException(String message) {
        super(message);
    }

    public StatusException(Throwable cause) {
        super(cause);
    }

    public StatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
