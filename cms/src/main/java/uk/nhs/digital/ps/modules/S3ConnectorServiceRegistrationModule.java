package uk.nhs.digital.ps.modules;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.repository.modules.AbstractReconfigurableDaemonModule;
import org.onehippo.repository.modules.ProvidesService;

import uk.nhs.digital.s3.S3Connector;
import uk.nhs.digital.s3.S3ConnectorImpl;

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
            s3Bucket = moduleConfig.getProperty("s3Bucket").getString();
            s3Region = moduleConfig.getProperty("s3Region").getString();
            s3Endpoint = moduleConfig.hasProperty("s3Endpoint")
                ? moduleConfig.getProperty("s3Endpoint").getString() : "";
        }
    }

    @Override
    protected void doInitialize(final Session session) throws RepositoryException {
        s3Connector = new S3ConnectorImpl(getAmazonS3(), s3Bucket);
        HippoServiceRegistry.registerService(s3Connector, S3Connector.class);
    }

    @Override
    protected void doShutdown() {
        HippoServiceRegistry.unregisterService(s3Connector, S3Connector.class);
    }

    private AmazonS3 getAmazonS3() {
        Region reg = Region.getRegion(Regions.fromName(s3Region));
        AWSCredentialsProvider provider = new SystemPropertiesCredentialsProvider();
        AmazonS3 s3 = new AmazonS3Client(provider);
        s3.setRegion(reg);

        String endpoint = reg.getServiceEndpoint("s3");
        if (!s3Endpoint.isEmpty()) {
            endpoint = s3Endpoint;
        }
        s3.setEndpoint(endpoint);

        return s3;
    }
}
