package uk.nhs.digital.apispecs.module;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;

import org.apache.commons.lang3.StringUtils;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.repository.modules.DaemonModule;
import org.onehippo.repository.modules.RequiresService;
import org.onehippo.repository.scheduling.RepositoryJob;
import org.onehippo.repository.scheduling.RepositoryJobCronTrigger;
import org.onehippo.repository.scheduling.RepositoryJobInfo;
import org.onehippo.repository.scheduling.RepositoryScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.apispecs.exception.ApiCatalogueJobException;
import uk.nhs.digital.apispecs.exception.ApiCatalogueModuleException;
import uk.nhs.digital.apispecs.jobs.ApiSpecSyncFromApigeeJob;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

@RequiresService(types = { RepositoryScheduler.class } )
public class ApiSpecSyncFromApigeeModule implements DaemonModule {

    private static final Logger log = LoggerFactory.getLogger(ApiSpecSyncFromApigeeModule.class);

    // See JavaDoc of java.time.Duration.parse for format.
    private static final String SCHEDULING_DELAY_DEFAULT_VALUE = "PT45M";

    private static final String SCHEDULING_DELAY_PROPERTY_NAME = "devzone.apispec.sync.schedule-delay-duration";

    private static final String JOB_GROUP = "devzone";
    private static final String JOB_TRIGGER_NAME = "cronTrigger";

    @Override public void initialize(final Session session) throws RepositoryException {

        try {
            final Duration schedulingDelay = configurableSchedulingDelay();

            scheduleJobsWithDelay(schedulingDelay);

        } catch (final Exception e) {
            log.error("Failed to schedule API Catalogue related jobs.", e);
        }
    }

    private void scheduleJobsWithDelay(final Duration schedulingDelay) {

        final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        Arrays.stream(ScheduledJobs.values()).forEach(job -> {
            log.info("Deferring scheduling of job {}/{} by {}", JOB_GROUP, job.jobName, schedulingDelay);

            scheduledExecutorService.schedule(() -> scheduleInJcr(job), schedulingDelay.getSeconds(), SECONDS);
        });

        scheduledExecutorService.shutdown();
    }

    private void scheduleInJcr(final ScheduledJobs job) {

        try {
            log.info("Configuring job {}/{} for updating API specifications.", JOB_GROUP, job.jobName);

            deactivatePreviousJobInstanceIfExists(job.jobName);

            if (shouldBeEnabled(job)) {

                schedule(job);

            } else {
                log.warn("Job {}/{} is not configured to be enabled, therefore it will not be activated.", JOB_GROUP, job.jobName);
            }

        } catch (final Exception e) {
            throw new ApiCatalogueJobException(format("Failed to schedule job %s/%s", JOB_GROUP, job.jobName), e);
        }
    }

    @Override public void shutdown() {
        Arrays.stream(ScheduledJobs.values())
            .map(x -> x.jobName)
            .forEach(jobName -> {
                try {
                    deactivatePreviousJobInstanceIfExists(jobName);
                } catch (final Exception e) {
                    log.error(format("Failed to cleanly shut down job %s/%s", JOB_GROUP, jobName), e);
                }
            });
    }

    private Duration configurableSchedulingDelay() {

        final String schedulingDelayRaw = Optional.ofNullable(System.getProperty(SCHEDULING_DELAY_PROPERTY_NAME))
            .filter(StringUtils::isNotBlank)
            .map(String::trim)
            .orElse(SCHEDULING_DELAY_DEFAULT_VALUE);

        try {
            return Duration.parse(schedulingDelayRaw);

        } catch (final Exception e) {
            throw new IllegalArgumentException("Scheduling duration is not in a valid ISO-8601 format: " + schedulingDelayRaw, e);
        }
    }

    private void schedule(final ScheduledJobs job) throws RepositoryException {
        final String cronExpression = cronExpression(job)
            .orElseThrow(() -> new ApiCatalogueModuleException(
                "No cron expression available; expected one to be configured via system property " + job.systemPropertyName));

        final RepositoryJobInfo jobInfo = new RepositoryJobInfo(job.jobName, JOB_GROUP, job.jobClass);

        final RepositoryJobCronTrigger trigger = new RepositoryJobCronTrigger(JOB_TRIGGER_NAME, cronExpression);

        scheduler().scheduleJob(jobInfo, trigger);

        log.info("Job {}/{} has been scheduled with cron expression: {}", JOB_GROUP, job.jobName, cronExpression);
    }

    private void deactivatePreviousJobInstanceIfExists(final String jobName) throws RepositoryException {
        if (scheduler().checkExists(jobName, JOB_GROUP)) {
            scheduler().deleteJob(jobName, JOB_GROUP);
            log.info("Job {}/{} has been deactivated.", JOB_GROUP, jobName);
        }
    }

    private RepositoryScheduler scheduler() {
        return HippoServiceRegistry.getService(RepositoryScheduler.class);
    }

    private boolean shouldBeEnabled(final ScheduledJobs job) {
        return cronExpression(job).isPresent();
    }

    private Optional<String> cronExpression(final ScheduledJobs job) {
        return Optional.ofNullable(System.getProperty(job.systemPropertyName))
            .map(String::trim)
            .filter(StringUtils::isNotBlank);
    }

    enum ScheduledJobs {

        DAILY(
            "apiSpecSyncFromApigee",
            ApiSpecSyncFromApigeeJob.class,
            "devzone.apispec.sync.daily-cron-expression"
        );

        private final String jobName;
        private final Class<? extends RepositoryJob> jobClass;
        private final String systemPropertyName;

        ScheduledJobs(final String jobName, final Class<? extends RepositoryJob> jobClass, final String systemPropertyName) {
            this.jobName = jobName;
            this.jobClass = jobClass;
            this.systemPropertyName = systemPropertyName;
        }
    }
}
