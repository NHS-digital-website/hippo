package uk.nhs.digital.externalstorage.workflow.externalFilePublish;

import uk.nhs.digital.externalstorage.s3.S3Connector;
import uk.nhs.digital.externalstorage.workflow.AbstractExternalFileTask;

public class ExternalFilePublishTask extends AbstractExternalFileTask {

    protected void setTargetStatus(S3Connector s3Connector, final String resource) {
        s3Connector.publishResource(resource);
    }
}
