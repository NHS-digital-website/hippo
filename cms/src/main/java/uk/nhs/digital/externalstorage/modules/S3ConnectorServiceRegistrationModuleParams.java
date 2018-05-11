package uk.nhs.digital.externalstorage.modules;

import org.apache.commons.lang3.Validate;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class S3ConnectorServiceRegistrationModuleParams {

    // SYSTEM PROPERTY KEYS
    private static final String KEY_AWS_BUCKET = "externalstorage.aws.bucket";
    private static final String KEY_AWS_REGION = "externalstorage.aws.region";
    private static final String KEY_AWS_S3_ENDPOINT = "externalstorage.aws.s3.endpoint";
    // JCR PROPERTIES KEYS
    private static final String KEY_DOWNLOAD_MAX_CONC_COUNT = "maxConcurrentDownloadsCount";
    private static final String KEY_DOWNLOAD_SHUTDOWN_TIMEOUT_IN_SECS = "downloadShutdownTimeoutInSecs";
    private static final String KEY_UPLOAD_MAX_CONC_COUNT = "maxConcurrentUploadsCount";
    private static final String KEY_UPLOAD_SHUTDOWN_TIMEOUT_IN_SECS = "uploadShutdownTimeoutInSecs";

    private String s3Bucket;
    private String s3Region;
    private String s3Endpoint;
    private int downloadsMaxConcurrentCount;
    private int downloadsShutdownTimeoutInSecs;
    private int uploadsMaxConcurrentCount;
    private int uploadsShutdownTimeoutInSecs;


    private S3ConnectorServiceRegistrationModuleParams(final String s3Bucket,
                                                       final String s3Region,
                                                       final String s3Endpoint,
                                                       final int downloadsMaxConcurrentCount,
                                                       final int downloadsShutdownTimeoutInSecs,
                                                       final int uploadsMaxConcurrentCount,
                                                       final int uploadsShutdownTimeoutInSecs
    ) {
        this.s3Bucket = s3Bucket;
        this.s3Region = s3Region;
        this.s3Endpoint = s3Endpoint;
        this.downloadsMaxConcurrentCount = downloadsMaxConcurrentCount;
        this.downloadsShutdownTimeoutInSecs = downloadsShutdownTimeoutInSecs;
        this.uploadsMaxConcurrentCount = uploadsMaxConcurrentCount;
        this.uploadsShutdownTimeoutInSecs = uploadsShutdownTimeoutInSecs;
    }

    static S3ConnectorServiceRegistrationModuleParams extractParameters(final Node moduleConfigNode) {
        return new S3ConnectorServiceRegistrationModuleParams(

            // System properties set via command line
            System.getProperty(KEY_AWS_BUCKET, ""),
            System.getProperty(KEY_AWS_REGION, ""),
            System.getProperty(KEY_AWS_S3_ENDPOINT, ""),

            // JCR properties set by YAML config
            // (hippo:configuration/hippo:modules/s3ConnectorRegistrationModule/hippo:moduleconfig)
            getIntJcrPropertyValue(moduleConfigNode, KEY_DOWNLOAD_MAX_CONC_COUNT),
            getIntJcrPropertyValue(moduleConfigNode, KEY_DOWNLOAD_SHUTDOWN_TIMEOUT_IN_SECS),
            getIntJcrPropertyValue(moduleConfigNode, KEY_UPLOAD_MAX_CONC_COUNT),
            getIntJcrPropertyValue(moduleConfigNode, KEY_UPLOAD_SHUTDOWN_TIMEOUT_IN_SECS)
        );
    }

    private static int getIntJcrPropertyValue(Node moduleConfigNode, String jcrPropertyKey) {
        try {
            return moduleConfigNode.getProperty(jcrPropertyKey).getDecimal().intValue();
        } catch (RepositoryException repositoryException) {
            throw new RuntimeException(repositoryException);
        }
    }

    public void validate() {

        try {
            Validate.notBlank(s3Bucket, "Required argument is missing: " + KEY_AWS_BUCKET);
            Validate.notBlank(s3Region, "Required argument is missing: " + KEY_AWS_REGION);
            // s3Endpoint is optional

            Validate.inclusiveBetween(1L, Integer.MAX_VALUE, downloadsMaxConcurrentCount,
                "Required argument is missing or out of range: " + KEY_DOWNLOAD_MAX_CONC_COUNT);
            Validate.inclusiveBetween(1L, Integer.MAX_VALUE, downloadsShutdownTimeoutInSecs,
                "Required argument is missing or out of range: " + KEY_DOWNLOAD_SHUTDOWN_TIMEOUT_IN_SECS);
            Validate.inclusiveBetween(1L, Integer.MAX_VALUE, uploadsMaxConcurrentCount,
                "Required argument is missing or out of range: " + KEY_UPLOAD_MAX_CONC_COUNT);
            Validate.inclusiveBetween(1L, Integer.MAX_VALUE, uploadsShutdownTimeoutInSecs,
                "Required argument is missing or out of range: " + KEY_UPLOAD_SHUTDOWN_TIMEOUT_IN_SECS);
        } catch (Exception validationException) {
            throw new IllegalArgumentException("Failed to configure " + getClass().getSimpleName(), validationException);
        }
    }

    String getS3Bucket() {
        return s3Bucket;
    }

    String getS3Region() {
        return s3Region;
    }

    String getS3Endpoint() {
        return s3Endpoint;
    }

    int getDownloadsMaxConcurrentCount() {
        return downloadsMaxConcurrentCount;
    }

    int getDownloadsShutdownTimeoutInSecs() {
        return downloadsShutdownTimeoutInSecs;
    }

    int getUploadsMaxConcurrentCount() {
        return uploadsMaxConcurrentCount;
    }

    int getUploadsShutdownTimeoutInSecs() {
        return uploadsShutdownTimeoutInSecs;
    }

    @Override
    public String toString() {
        return "S3ConnectorServiceRegistrationModuleParams{"
            + "s3Bucket='" + s3Bucket + '\''
            + ", s3Region='" + s3Region + '\''
            + ", s3Endpoint='" + s3Endpoint + '\''
            + ", downloadsMaxConcurrentCount=" + downloadsMaxConcurrentCount
            + ", downloadsShutdownTimeoutInSecs=" + downloadsShutdownTimeoutInSecs
            + ", uploadsMaxConcurrentCount=" + uploadsMaxConcurrentCount
            + ", uploadsShutdownTimeoutInSecs=" + uploadsShutdownTimeoutInSecs
            + '}';
    }
}
