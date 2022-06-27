package uk.nhs.digital.arc.job;

import org.onehippo.repository.scheduling.RepositoryJob;
import org.onehippo.repository.scheduling.RepositoryJobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.arc.process.ManifestProcessor;

import java.io.IOException;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class ArcRepositoryJob implements RepositoryJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArcRepositoryJob.class);

    public ArcRepositoryJob() {
    }

    @Override
    public void execute(RepositoryJobExecutionContext repositoryJobExecutionContext) throws RepositoryException {
        Session session = null;

        try {
            session = repositoryJobExecutionContext.createSystemSession();
            String manifestFile = repositoryJobExecutionContext.getAttribute("manifest_file");
            String nodePath = repositoryJobExecutionContext.getAttribute("node_path");

            LOGGER.info("ArcRepositoryJob executing repo job now with manifest_file value of '" + manifestFile + "'");

            ManifestProcessor manifestProcessor = new ManifestProcessor(session, manifestFile, nodePath);
            try {
                manifestProcessor.readWrapperFromFile();
            } catch (IOException e) {
                LOGGER.error("Unable to read the manifest file from the specified location", e);
            }
        } finally {
            if (session != null) {
                LOGGER.debug("Now logging the user out of the system session we created");
                session.logout();
            }
        }
    }
}
