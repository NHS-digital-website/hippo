package uk.nhs.digital.arc.storage;

import java.io.InputStream;

/**
 * Container class for meta information and the input stream associated with a file
 */
public class ArcFileData {
    InputStream delegateStream;
    String contentType;

    public ArcFileData(InputStream delegateStream, String contentType) {
        this.delegateStream = delegateStream;
        this.contentType = contentType;
    }

    public InputStream getDelegateStream() {
        return delegateStream;
    }

    public String getContentType() {
        return contentType;
    }
}
