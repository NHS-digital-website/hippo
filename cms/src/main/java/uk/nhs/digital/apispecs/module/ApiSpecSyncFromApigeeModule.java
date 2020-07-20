package uk.nhs.digital.apispecs.module;

import static java.lang.Boolean.parseBoolean;
import static uk.nhs.digital.apispecs.module.ApiSpecSyncFromApigeeModule.ConfigPropertyNames.CRON_EXPRESSION;
import static uk.nhs.digital.apispecs.module.ApiSpecSyncFromApigeeModule.ConfigPropertyNames.SYNC_ENABLED;

import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.repository.modules.AbstractReconfigurableDaemonModule;
import org.onehippo.repository.scheduling.RepositoryJobCronTrigger;
import org.onehippo.repository.scheduling.RepositoryJobInfo;
import org.onehippo.repository.scheduling.RepositoryScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.JcrNodeUtils;
import uk.nhs.digital.apispecs.jobs.ApiSpecSyncFromApigeeJob;

import java.util.Optional;
import java.util.stream.Stream;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class ApiSpecSyncFromApigeeModule extends AbstractReconfigurableDaemonModule {

    private static final Logger log = LoggerFactory.getLogger(ApiSpecSyncFromApigeeModule.class);

    private final Object configurationLock = new Object();

    private static final String JOB_GROUP = "devzone";
    private static final String JOB_NAME = "apiSpecSyncFromApigee";
    private static final String JOB_TRIGGER_NAME = String.format("%s.%s.%s", JOB_GROUP, JOB_NAME, "cronTrigger");

    @Override protected void doConfigure(final Node moduleConfigNode) {

        // This method is called both on system startup AND on update to the node through the admin console.
        //
        // Given that we need to update the job on both events, we create and configure the
        // job here rather than in doInitialize which is only being called on system startup.

        synchronized (configurationLock) {

            try {
                log.info("Configuring job {}:{} for synchronising API Specifications from Apigee.", JOB_GROUP, JOB_NAME); // the only one called on the config node change

                deactivatePreviousJobInstanceIfExists();

                if (jobShouldBeEnabled(moduleConfigNode)) {

                    scheduleNewJob(moduleConfigNode);

                } else {
                    log.warn("Job {}:{} is not configured to be enabled, therefore it will not be activated.", JOB_GROUP, JOB_NAME);
                }

            } catch (final Exception e) {
                throw new RuntimeException(String.format("Failed to configure job %s:%s", JOB_GROUP, JOB_NAME), e);
            }
        }
    }

    @Override protected void doInitialize(final Session session) {
        // no-op: all the work is done in doConfigure - see the comment in that method
    }

    @Override protected void doShutdown() {

        try {
            deactivatePreviousJobInstanceIfExists();

        } catch (final Exception e) {
            throw new RuntimeException(String.format("Failed to cleanly shut down job %s:%s", JOB_GROUP, JOB_NAME), e);
        }
    }

    private void scheduleNewJob(final Node moduleConfigNode) throws RepositoryException {

        final RepositoryJobInfo jobInfo = new RepositoryJobInfo(JOB_NAME, JOB_GROUP, ApiSpecSyncFromApigeeJob.class);

        final String cronExpression = cronExpression(moduleConfigNode);

        final RepositoryJobCronTrigger trigger = new RepositoryJobCronTrigger(JOB_TRIGGER_NAME, cronExpression);

        scheduler().scheduleJob(jobInfo, trigger);

        log.info("Job {}:{} has been scheduled with cron expression: {}", JOB_GROUP, JOB_NAME, cronExpression);
    }

    private void deactivatePreviousJobInstanceIfExists() throws RepositoryException {

        if (scheduler().checkExists(JOB_NAME, JOB_GROUP)) {

            scheduler().deleteJob(JOB_NAME, JOB_GROUP);

            log.info("Job {}:{} has been deactivated.", JOB_GROUP, JOB_NAME);
        }
    }

    private RepositoryScheduler scheduler() {
        return HippoServiceRegistry.getService(RepositoryScheduler.class);
    }

    private boolean jobShouldBeEnabled(final Node moduleConfigNode) {

        final boolean enabledViaJcr = JcrNodeUtils.getBooleanPropertyQuietly(moduleConfigNode, SYNC_ENABLED.jcrPropertyName).orElse(false);

        final boolean enabledViaSystemProperty = parseBoolean(System.getProperty(SYNC_ENABLED.systemPropertyName));

        return enabledViaJcr || enabledViaSystemProperty;
    }

    private String cronExpression(final Node moduleConfigNode) {

        final Optional<String> cronExprFromJcr = JcrNodeUtils.getStringPropertyQuietly(moduleConfigNode, CRON_EXPRESSION.jcrPropertyName);

        final Optional<String> cronExprFromSystemProperty = Optional.ofNullable(System.getProperty(CRON_EXPRESSION.systemPropertyName));

        return Stream.of(cronExprFromJcr, cronExprFromSystemProperty)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Cron expression has not been configured."));
    }

    enum ConfigPropertyNames {

        SYNC_ENABLED("devzone.apispec.sync.enabled", "enabled"),
        CRON_EXPRESSION("devzone.apispec.sync.cron-expression", "cronExpression");

        private final String systemPropertyName;
        private final String jcrPropertyName;

        ConfigPropertyNames(final String systemPropertyName, final String jcrPropertyName) {
            this.systemPropertyName = systemPropertyName;
            this.jcrPropertyName = jcrPropertyName;
        }
    }
}
