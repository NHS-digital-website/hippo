package uk.nhs.digital.externalstorage.s3;

import org.onehippo.cms7.services.SingletonService;

import java.io.InputStream;

@SingletonService
public interface S3Connector {

    String getBucketName();

    boolean publishResource(String objectPath);

    boolean unpublishResource(String objectPath);

    S3ObjectMetadata uploadFile(InputStream fileStream, String objectPath, String contentType);

    long getFileSize(String objectPath);
}
