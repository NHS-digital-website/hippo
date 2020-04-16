package uk.nhs.digital.intranet.model.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends Exception {

    private final HttpStatus statusCode;

    public AuthorizationException(final HttpStatus statusCode, final Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
