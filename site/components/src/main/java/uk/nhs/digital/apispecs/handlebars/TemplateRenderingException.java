package uk.nhs.digital.apispecs.handlebars;

public class TemplateRenderingException extends RuntimeException {

    public TemplateRenderingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
