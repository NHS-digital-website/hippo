package uk.nhs.digital.s3;

import org.onehippo.cms7.services.SingletonService;

@SingletonService
public interface S3Connector {

    boolean publishResource(String objectPath);

    boolean unpublishResource(String objectPath);

    boolean uploadFile(String objectPath);
}
