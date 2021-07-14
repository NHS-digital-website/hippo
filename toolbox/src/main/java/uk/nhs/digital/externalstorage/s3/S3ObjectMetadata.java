package uk.nhs.digital.externalstorage.s3;

public interface S3ObjectMetadata {

    long getSize();

    String getMimeType();

    String getFileName();

    String getUrl();

    String getReference();
}
