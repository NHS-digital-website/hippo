package uk.nhs.digital.apispecs.apigee;

public class ApigeeServiceException extends RuntimeException {

    public ApigeeServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApigeeServiceException(String message) {
        super(message);
    }
}
