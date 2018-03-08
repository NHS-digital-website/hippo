package uk.nhs.digital.externalstorage.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.externalstorage.ExternalStorageConstants;
import uk.nhs.digital.externalstorage.s3.S3ObjectMetadata;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class S3NodeMetadata implements S3ObjectMetadata {

    static final Logger log = LoggerFactory.getLogger(S3NodeMetadata.class);

    private final long size;
    private final String mimeType;
    private final String fileName;
    private final String url;
    private final String reference;

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getReference() {
        return reference;
    }

    public S3NodeMetadata(Node node) {
        size = getLongProperty(node, ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_SIZE);
        mimeType = getStringProperty(node, ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_MIME_TYPE);
        fileName = getStringProperty(node, ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_FILE_NAME);
        url = getStringProperty(node, ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_PUBLIC_URL);
        reference = getStringProperty(node, ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_REFERENCE);
    }

    private String getStringProperty(Node node, String propertyName) {
        String defaultReturn = "value not found";

        try {
            if (node.hasProperty(propertyName)) {
                return node.getProperty(propertyName).getString();
            } else {
                log.warn("No property found {}, using: " + defaultReturn, propertyName);
            }
        } catch (RepositoryException ex) {
            log.error("Repository exception while trying to read property " + propertyName + ", using: " + defaultReturn, ex);
        }

        return defaultReturn;
    }

    private long getLongProperty(Node node, String propertyName) {
        try {
            if (node.hasProperty(propertyName)) {
                return node.getProperty(propertyName).getLong();
            } else {
                log.warn("No property found {}, using 0", propertyName);
            }
        } catch (RepositoryException ex) {
            log.error("Repository exception while trying to read property " + propertyName + ", using 0", ex);
        }

        return 0;
    }
}
