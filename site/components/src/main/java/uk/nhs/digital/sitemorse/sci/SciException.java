package uk.nhs.digital.sitemorse.sci;


/**
 * All exceptions thrown by SCIClient are subclasses of this exception.
 * They may be encapsulating other Java exceptions, or be a string message.
 * If they are an error message direct from the SCI server they will be of
 * type SCIServerError. These must be displayed to the user.
 */
public class SciException extends Exception {
    private static final long serialVersionUID = 8955498931115729307L;

    SciException(Throwable e) {
        super(e);
    }

    SciException(String string) {
        super(string);
    }
}
