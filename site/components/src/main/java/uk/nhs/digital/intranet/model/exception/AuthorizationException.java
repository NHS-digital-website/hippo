package uk.nhs.digital.intranet.model.exception;

import org.springframework.http.HttpStatusCode;

public class AuthorizationException extends Exception {

    private final HttpStatusCode statusCode;

    public AuthorizationException(final HttpStatusCode statusCode, final Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }
}
