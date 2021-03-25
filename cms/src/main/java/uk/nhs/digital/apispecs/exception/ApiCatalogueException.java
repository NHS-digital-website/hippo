package uk.nhs.digital.apispecs.exception;

public abstract class ApiCatalogueException extends RuntimeException {

    public ApiCatalogueException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ApiCatalogueException(final String message) {
        super(message);
    }
}
