package uk.nhs.digital.arc.exception;

public class ArcException extends Exception {
    public ArcException(String message, Throwable e) {
        super(message, e);
    }

    public ArcException(Throwable e) {
        super(e);
    }

    public ArcException(String message) {
        super(message);
    }
}
