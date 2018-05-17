package uk.nhs.digital.externalstorage;

public interface ExternalStorageConstants {

    String NODE_TYPE_EXTERNAL_RESOURCE = "externalstorage:resource";

    /**
     * Example: {@code 42/F961C2/dataset-attachment-text.pdf}
     */
    String PROPERTY_EXTERNAL_STORAGE_REFERENCE = "externalstorage:reference";

    /**
     * Example: {@code https://files.local.nhsd.io/42/F961C2/dataset-attachment-text.pdf}
     */
    String PROPERTY_EXTERNAL_STORAGE_PUBLIC_URL = "externalstorage:url";

    String PROPERTY_EXTERNAL_STORAGE_SIZE = "externalstorage:size";

    String PROPERTY_EXTERNAL_STORAGE_MIME_TYPE = "jcr:mimeType";
    String PROPERTY_EXTERNAL_STORAGE_FILE_NAME = "hippo:filename";
    String PROPERTY_EXTERNAL_STORAGE_LAST_MODIFIED = "jcr:lastModified";
}
