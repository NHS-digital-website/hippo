package uk.nhs.digital.externalstorage.modules;

import static org.slf4j.LoggerFactory.getLogger;
import static uk.nhs.digital.externalstorage.modules.S3ConnectorServiceRegistrationModuleParams.*;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.apache.commons.lang3.Validate;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.repository.modules.AbstractReconfigurableDaemonModule;
import org.onehippo.repository.modules.ProvidesService;

import org.slf4j.Logger;
import uk.nhs.digital.externalstorage.s3.*;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;


@ProvidesService(types = PooledS3Connector.class)
public class S3ConnectorServiceRegistrationModule extends AbstractReconfigurableDaemonModule {

    private static final Logger log = getLogger(S3ConnectorServiceRegistrationModule.class);

    private final Object configurationLock = new Object();

    private String s3Bucket;
    private String s3Region;
    private String s3Endpoint;

    private int downloadsMaxConcurrentCount;
    private long downloadsShutdownTimeoutInSecs;

    private int uploadsMaxConcurrentCount;
    private long uploadsShutdownTimeoutInSecs;

    private ExecutorService downloadExecutorService;
    private ExecutorService uploadExecutorService;
    private PooledS3Connector pooledS3Connector;

    @Override
    protected void doConfigure(final Node moduleConfig) throws RepositoryException {

        log.info("Updating S3 Connector configuration.");

        // Unlike other public methods of this class, this one can be called several times,
        // and, in theory, from different threads therefore write access to the fields needs
        // synchronising.
        synchronized (configurationLock) {
            readInConfiguration(moduleConfig);

            reportNewConfiguration();

            validateConfiguration();

            unregisterServiceIfRegistered();

            registerService();

            log.info("Updated configuration has been applied.");
        }
    }

    @Override
    protected void doInitialize(final Session session) throws RepositoryException {
        // no-op - all work is done in doConfigure
    }

    @Override
    protected void doShutdown() {
        unregisterServiceIfRegistered();
        blockUntilTasksCompleteOrTimeout();
    }

    private void readInConfiguration(final Node moduleConfig) throws RepositoryException {
        s3Bucket = getValue(AWS_BUCKET, moduleConfig);
        s3Region = getValue(AWS_REGION, moduleConfig);
        s3Endpoint = getValue(AWS_S3_ENDPOINT, moduleConfig);

        downloadsMaxConcurrentCount = getValue(DOWNLOAD_MAX_CONC_COUNT, moduleConfig);
        downloadsShutdownTimeoutInSecs = getValue(DOWNLOAD_SHUTDOWN_TIMEOUT_IN_SECS, moduleConfig);

        uploadsMaxConcurrentCount = getValue(UPLOAD_MAX_CONC_COUNT, moduleConfig);
        uploadsShutdownTimeoutInSecs = getValue(UPLOAD_SHUTDOWN_TIMEOUT_IN_SECS, moduleConfig);
    }

    private void validateConfiguration() {

        try {
            Validate.notBlank(s3Bucket, "Required argument is missing: " + AWS_BUCKET.getJcrModulePropertyKey());
            Validate.notBlank(s3Region, "Required argument is missing: " + AWS_REGION.getJcrModulePropertyKey());
            // s3Endpoint is optional

            Validate.inclusiveBetween(1L, (long)Integer.MAX_VALUE, downloadsMaxConcurrentCount,
                "Required argument is missing or out of range: " + DOWNLOAD_MAX_CONC_COUNT.getJcrModulePropertyKey());
            Validate.inclusiveBetween(1L, (long)Integer.MAX_VALUE, downloadsShutdownTimeoutInSecs,
                "Required argument is missing or out of range: " + DOWNLOAD_SHUTDOWN_TIMEOUT_IN_SECS.getJcrModulePropertyKey());
            Validate.inclusiveBetween(1L, (long)Integer.MAX_VALUE, uploadsMaxConcurrentCount,
                "Required argument is missing or out of range: " + DOWNLOAD_MAX_CONC_COUNT.getJcrModulePropertyKey());
            Validate.inclusiveBetween(1L, (long)Integer.MAX_VALUE, uploadsShutdownTimeoutInSecs,
                "Required argument is missing or out of range: " + DOWNLOAD_SHUTDOWN_TIMEOUT_IN_SECS.getJcrModulePropertyKey());
        } catch (Exception validationException) {
            throw new IllegalArgumentException("Failed to configure " + getClass().getSimpleName(), validationException);
        }
    }

    private void registerService() {

        final S3Connector s3Connector = new S3SdkConnector(
            getAmazonS3Client(),
            s3Bucket,
            new S3ObjectKeyGenerator(this::newRandomString)
        );

        downloadExecutorService = Executors.newWorkStealingPool(downloadsMaxConcurrentCount);
        uploadExecutorService = Executors.newWorkStealingPool(uploadsMaxConcurrentCount);

        pooledS3Connector = new BlockingPooledS3Connector(
            s3Connector,
            downloadExecutorService,
            uploadExecutorService,
            new S3TransfersReportingTracker()
        );

        HippoServiceRegistry.registerService(pooledS3Connector, PooledS3Connector.class);
    }

    private void unregisterServiceIfRegistered() {

        if (HippoServiceRegistry.getService(PooledS3Connector.class) != null) {

            HippoServiceRegistry.unregisterService(pooledS3Connector, PooledS3Connector.class);

            // null checks protect against failed doInitialize/doConfigure
            Optional.ofNullable(downloadExecutorService).ifPresent(ExecutorService::shutdown);
            Optional.ofNullable(uploadExecutorService).ifPresent(ExecutorService::shutdown);
        }
    }

    /**
     * <p>
     * Blocks the application shutdown until all files finish downloading and uploading or the timeout
     * occurs, whichever comes first.
     * </p><p>
     * Most files are small enough that it's likely they'll finish downloading within given timeout.
     * There is no way around eventually forcefully terminating overrunning transfers, but it's expected
     * that such cases would be few and far between.
     * </p>
     */
    private void blockUntilTasksCompleteOrTimeout() {

        waitUntilDoneOrTimeout("download", downloadExecutorService, downloadsShutdownTimeoutInSecs);
        waitUntilDoneOrTimeout("upload", uploadExecutorService, uploadsShutdownTimeoutInSecs);
    }

    private static void waitUntilDoneOrTimeout(final String s3Operation,
                                               final ExecutorService executorService,
                                               final long shutdownTimeoutInSecs
    ) {
        if (executorService != null) {
            try {
                if (!executorService.isTerminated()) {
                    log.info("At least one S3 {} is still in progress; waiting for it to finish.", s3Operation);
                }
                if (!executorService.awaitTermination(shutdownTimeoutInSecs, TimeUnit.SECONDS)) {
                    log.warn(
                        "At least one in-progress S3 {} has been forcefully terminated due to application shutdown.",
                        s3Operation
                    );
                }
            } catch (final InterruptedException ie) {
                throw new RuntimeException("Interrupted when shutting down S3 " + s3Operation + "executor service.",
                    ie);
            }
        }
    }

    private AmazonS3 getAmazonS3Client() {
        AWSCredentialsProvider provider = new SystemPropertiesCredentialsProvider();
        AmazonS3ClientBuilder s3Builder = AmazonS3ClientBuilder.standard()
            .withCredentials(provider)
            .withRegion(Regions.fromName(s3Region));

        if (!s3Endpoint.isEmpty()) {
            s3Builder.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(s3Endpoint, s3Region));
        }

        return s3Builder.build();
    }

    private <T> T getValue(final S3ConnectorServiceRegistrationModuleParams<T> configProperty, final Node moduleConfig) throws RepositoryException {
        String value = System.getProperty(configProperty.getSystemPropertyKey(), "");
        if (value.isEmpty()) {
            value = moduleConfig.getProperty(configProperty.getJcrModulePropertyKey()).getString();
        }

        return configProperty.fromString(value);
    }

    private String newRandomString() {
        return UUID.randomUUID().toString();
    }

    private void reportNewConfiguration() {
        log.info("S3ConnectorServiceRegistrationModule{"
            + "s3Bucket='" + s3Bucket + '\''
            + ", s3Region='" + s3Region + '\''
            + ", s3Endpoint='" + s3Endpoint + '\''
            + ", downloadsMaxConcurrentCount=" + downloadsMaxConcurrentCount
            + ", downloadsShutdownTimeoutInSecs=" + downloadsShutdownTimeoutInSecs
            + ", uploadsMaxConcurrentCount=" + uploadsMaxConcurrentCount
            + ", uploadsShutdownTimeoutInSecs=" + uploadsShutdownTimeoutInSecs
            + '}'
        );
    }
}
