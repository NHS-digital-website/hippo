package uk.nhs.digital.externalstorage.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

@Node(jcrType = "externalstorage:resource")
public class Resource extends HippoDocument {

    public String getUrl() {
        return encode(getSingleProperty(PropertyKeys.URL));
    }

    public String getFilename() {
        return getSingleProperty(PropertyKeys.FILENAME);
    }

    public String getMimeType() {
        return getSingleProperty(PropertyKeys.MIME_TYPE);
    }

    public Long getLength() {
        return getSingleProperty(PropertyKeys.SIZE);
    }

    public String getReference() {
        return getSingleProperty(PropertyKeys.REFERENCE);
    }

    public String getReferenceEncoded() {
        return encode(getReference());
    }

    public String getEncoding() {
        return getSingleProperty(PropertyKeys.ENCODING);
    }

    public Calendar getLastModified() {
        return getSingleProperty(PropertyKeys.LAST_MODIFIED);
    }

    private String encode(String str) {
        // Ripped off from the AWS sdk, this ensures proper encoding of the URL
        try {
            return URLEncoder.encode(str, "UTF-8")
                .replace("%3A", ":")
                .replace("%2F", "/")
                .replace("+", "%20");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
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
