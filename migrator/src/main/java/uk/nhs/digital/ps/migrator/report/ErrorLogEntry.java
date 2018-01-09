package uk.nhs.digital.ps.migrator.report;

public class ErrorLogEntry {
    private Exception exception;
    private String description;

    public ErrorLogEntry(final String description) {
        this(null, description);
    }

    public ErrorLogEntry(final Exception exception, final String description) {
        this.exception = exception;
        this.description = description;
    }

    public Exception getException() {
        return exception;
    }

    public String getDescription() {
        return description;
    }
}
