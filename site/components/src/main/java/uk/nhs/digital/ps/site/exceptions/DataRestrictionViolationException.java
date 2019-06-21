package uk.nhs.digital.ps.site.exceptions;

public class DataRestrictionViolationException extends RuntimeException {

    public DataRestrictionViolationException(final String message) {
        super(message);
    }
}
