package uk.nhs.digital.common.components.apispecification.commonmark;

public class MarkdownConversionException extends RuntimeException {

    public MarkdownConversionException(final String message, final Exception cause) {
        super(message, cause);
    }
}
