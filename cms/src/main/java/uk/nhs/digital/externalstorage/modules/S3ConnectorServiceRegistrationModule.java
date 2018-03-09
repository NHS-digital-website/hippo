package uk.nhs.digital.externalstorage.modules;

import static uk.nhs.digital.externalstorage.ExternalStorageConstants.SYSTEM_PROPERTY_AWS_BUCKET_NAME;
import static uk.nhs.digital.externalstorage.ExternalStorageConstants.SYSTEM_PROPERTY_AWS_REGION;
import static uk.nhs.digital.externalstorage.ExternalStorageConstants.SYSTEM_PROPERTY_AWS_S3_ENDPOINT;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.repository.modules.AbstractReconfigurableDaemonModule;
import org.onehippo.repository.modules.ProvidesService;

import uk.nhs.digital.externalstorage.s3.S3Connector;
import uk.nhs.digital.externalstorage.s3.S3ConnectorImpl;
import uk.nhs.digital.externalstorage.s3.S3ObjectKeyGenerator;

import java.util.UUID;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

@ProvidesService(types = S3Connector.class)
public class S3ConnectorServiceRegistrationModule extends AbstractReconfigurableDaemonModule {

    private final Object configurationLock = new Object();
    private S3Connector s3Connector;
    private String s3Bucket;
    private String s3Region;
    private String s3Endpoint;

    @Override
    protected void doConfigure(final Node moduleConfig) throws RepositoryException {
        synchronized (configurationLock) {
            s3Bucket = getConfigValue(SYSTEM_PROPERTY_AWS_BUCKET_NAME, "s3Bucket", moduleConfig);
            s3Region = getConfigValue(SYSTEM_PROPERTY_AWS_REGION, "s3Region", moduleConfig);

            if (moduleConfig.hasProperty("s3Endpoint")) {
                s3Endpoint = getConfigValue(SYSTEM_PROPERTY_AWS_S3_ENDPOINT, "s3Endpoint", moduleConfig);
            } else {
                s3Endpoint = System.getProperty(SYSTEM_PROPERTY_AWS_S3_ENDPOINT, "");
            }
        }
    }

    @Override
    protected void doInitialize(final Session session) throws RepositoryException {
        s3Connector = new S3ConnectorImpl(
            getAmazonS3(),
            s3Bucket,
            new S3ObjectKeyGenerator(this::newRandomString)
        );

        HippoServiceRegistry.registerService(s3Connector, S3Connector.class);
    }

    private String newRandomString() {
        return UUID.randomUUID().toString();
    }

    @Override
    protected void doShutdown() {
        HippoServiceRegistry.unregisterService(s3Connector, S3Connector.class);
    }

    private AmazonS3 getAmazonS3() {
        AWSCredentialsProvider provider = new SystemPropertiesCredentialsProvider();
        AmazonS3ClientBuilder s3Builder = AmazonS3ClientBuilder.standard()
            .withCredentials(provider)
            .withRegion(Regions.fromName(s3Region));

        if (!s3Endpoint.isEmpty()) {
            s3Builder.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(s3Endpoint, s3Region));
        }

        return s3Builder.build();
    }

    private String getConfigValue(final String systemProperty, final String moduleProperty, Node moduleConfig) throws RepositoryException {
        String value = System.getProperty(systemProperty, "");
        if (value.isEmpty()) {
            value = moduleConfig.getProperty(moduleProperty).getString();
        }

        return value;
    }
}
