package uk.nhs.digital.externalstorage.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;

import java.util.Calendar;

@Node(jcrType = "externalstorage:resource")
public class Resource extends HippoDocument {

    public String getUrl() {
        return getProperty(PropertyKeys.URL);
    }

    public String getFilename() {
        return getProperty(PropertyKeys.FILENAME);
    }

    public Long getMimeType() {
        return getProperty(PropertyKeys.MIME_TYPE);
    }

    public Long getLength() {
        return getProperty(PropertyKeys.SIZE);
    }

    public String getReference() {
        return getProperty(PropertyKeys.REFERENCE);
    }

    public String getEncoding() {
        return getProperty(PropertyKeys.ENCODING);
    }

    public Calendar getLastModified() {
        return getProperty(PropertyKeys.LAST_MODIFIED);
    }

    interface PropertyKeys {
        String REFERENCE = "externalstorage:reference";
        String SIZE = "externalstorage:size";
        String URL = "externalstorage:url";
        String FILENAME = "hippo:filename";
        String ENCODING = "cr:encoding";
        String LAST_MODIFIED = "jcr:lastModified";
        String MIME_TYPE = "jcr:mimeType";
    }
}
