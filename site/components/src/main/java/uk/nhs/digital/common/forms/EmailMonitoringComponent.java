package uk.nhs.digital.common.forms;

import org.onehippo.repository.scheduling.RepositoryJob;
import org.onehippo.repository.scheduling.RepositoryJobExecutionContext;

import javax.jcr.RepositoryException;

public class EmailMonitoringComponent implements RepositoryJob {
    public void execute(final RepositoryJobExecutionContext context) throws RepositoryException {
        try {
            System.out.println("EMAIL MONITOR: SUCCESS");
        } catch (Exception e) {
            System.out.println("EMAIL MONITOR: FAILED");
            System.out.println(e.getMessage());
        }
    }
}
