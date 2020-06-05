package uk.nhs.digital.apispecs;

public class OpenApiSpecificationRepositoryException extends RuntimeException {

    public OpenApiSpecificationRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpenApiSpecificationRepositoryException(String message) {
        super(message);
    }
}
