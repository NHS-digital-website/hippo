package uk.nhs.digital.externalstorage;

public interface ExternalStorageConstants {

    String NODE_TYPE_EXTERNAL_RESOURCE = "externalstorage:resource";

    String PROPERTY_EXTERNAL_STORAGE_REFERENCE = "externalstorage:reference";
    String PROPERTY_EXTERNAL_STORAGE_SIZE = "externalstorage:size";
    String PROPERTY_EXTERNAL_STORAGE_PUBLIC_URL = "externalstorage:url";
    String PROPERTY_EXTERNAL_STORAGE_MIME_TYPE = "jcr:mimeType";
    String PROPERTY_EXTERNAL_STORAGE_FILE_NAME = "hippo:filename";
    String PROPERTY_EXTERNAL_STORAGE_LAST_MODIFIED = "jcr:lastModified";

    String SYSTEM_PROPERTY_AWS_BUCKET_NAME = "externalstorage.aws.bucket";
    String SYSTEM_PROPERTY_AWS_REGION = "externalstorage.aws.region";
    String SYSTEM_PROPERTY_AWS_S3_ENDPOINT = "externalstorage.aws.s3.endpoint";
}
