package uk.nhs.digital.externalstorage.s3;

import com.amazonaws.services.s3.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class S3ObjectMetadataImpl implements S3ObjectMetadata {

    static final Logger log = LoggerFactory.getLogger(S3ObjectMetadataImpl.class);

    private final long size;
    private final String mimeType;
    private final String fileName;
    private final String url;
    private final String reference;

    S3ObjectMetadataImpl(ObjectMetadata metadata, String bucketName, String objectKey) {
        URL url = getUrlObject(bucketName, objectKey);

        size = metadata.getContentLength();
        mimeType = metadata.getContentType();
        reference = url.getPath().substring(1);
        this.url = url.toString();
        fileName =  reference.substring(reference.lastIndexOf('/') + 1);
    }

    public long getSize() {
        return size;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUrl() {
        return url;
    }

    public String getReference() {
        return reference;
    }

    private URL getUrlObject(String bucketName, String objectKey) {
        try {
            return new URL("https://" + bucketName + "/" + objectKey);
        } catch (MalformedURLException ex) {
            log.error("Malformed url " + url, ex);
            throw new RuntimeException("Malformed url " + url, ex);
        }
    }
}
