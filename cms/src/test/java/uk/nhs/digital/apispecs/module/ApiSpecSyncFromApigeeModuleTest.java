package uk.nhs.digital.apispecs.module;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.repository.scheduling.RepositoryJobCronTrigger;
import org.onehippo.repository.scheduling.RepositoryJobInfo;
import org.onehippo.repository.scheduling.RepositoryJobTrigger;
import org.onehippo.repository.scheduling.RepositoryScheduler;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.nhs.digital.apispecs.jobs.ApiSpecSyncFromApigeeJob;

import java.util.List;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HippoServiceRegistry.class})
@PowerMockIgnore({"org.awaitility.*", "java.util.concurrent.*"})
public class ApiSpecSyncFromApigeeModuleTest {

    public static final String DAILY_JOB_NAME = "apiSpecSyncFromApigee";
    public static final String JOB_GROUP_NAME = "devzone";
    private static final String TRIGGER_NAME = "cronTrigger";

    @Mock private RepositoryScheduler scheduler;
    @Mock private Session session;

    private ArgumentCaptor<RepositoryJobInfo> jobInfoArgCaptor = ArgumentCaptor.forClass(RepositoryJobInfo.class);
    private ArgumentCaptor<RepositoryJobTrigger> jobTriggerArgCaptor = ArgumentCaptor.forClass(RepositoryJobTrigger.class);

    private final ApiSpecSyncFromApigeeModule apiSpecSyncFromApigeeModule = new ApiSpecSyncFromApigeeModule();

    @Before
    public void setUp() {
        openMocks(this);

        mockStatic(HippoServiceRegistry.class);
        given(HippoServiceRegistry.getService(RepositoryScheduler.class)).willReturn(scheduler);

    }

    @After
    public void tearDown() {
        unsetSystemProperties();
    }

    @Test
    public void initialize_schedulesNewJobs_whenConfiguredWithCronExpression() throws RepositoryException {

        // given
        final String expectedDailyCronExpression = "0 0/10 * ? * *";

        System.setProperty("devzone.apispec.sync.daily-cron-expression", expectedDailyCronExpression);
        System.setProperty("devzone.apispec.sync.schedule-delay-duration", "PT0.5S");

        // when
        apiSpecSyncFromApigeeModule.initialize(session);

        // then
        with()
            .pollDelay(1, SECONDS)
            .await()
            .atMost(5, SECONDS)
            .untilAsserted(() ->
                then(scheduler).should(times(1)).scheduleJob(any(), any())
            );

        then(scheduler).should(times(1)).scheduleJob(
            jobInfoArgCaptor.capture(),
            jobTriggerArgCaptor.capture()
        );

        final List<RepositoryJobInfo> actualJobInfos = jobInfoArgCaptor.getAllValues();

        final RepositoryJobInfo dailyJobInfo = actualJobInfos.get(0);
        assertThat("Daily job is scheduled with correct group.", dailyJobInfo.getGroup(), is(JOB_GROUP_NAME));
        assertThat("Daily job is scheduled with correct name.", dailyJobInfo.getName(), is(DAILY_JOB_NAME));
        assertThat("Daily job is scheduled with correct class.", dailyJobInfo.getJobClass(), is(ApiSpecSyncFromApigeeJob.class));

        final List<RepositoryJobTrigger> actualJobTriggers = jobTriggerArgCaptor.getAllValues();

        final RepositoryJobTrigger dailyJobTrigger = actualJobTriggers.get(0);
        assertThat("Daily job is scheduled as a cron job.", dailyJobTrigger, instanceOf(RepositoryJobCronTrigger.class));
        assertThat("Daily job is scheduled with correct trigger name", dailyJobTrigger.getName(), is(TRIGGER_NAME));
        assertThat("Daily job is scheduled with correct cron expression", ((RepositoryJobCronTrigger)dailyJobTrigger).getCronExpression(), is(expectedDailyCronExpression));
    }

    @Test
    public void initialize_deactivatesOldJobs_whenPreviousJobInstancesExist() throws RepositoryException {

        // given
        given(scheduler.checkExists(any(), any())).willReturn(true);

        System.setProperty("devzone.apispec.sync.schedule-delay-duration", "PT1S");

        // when
        apiSpecSyncFromApigeeModule.initialize(session);

        // then
        with()
            .pollDelay(1, SECONDS)
            .await()
            .atMost(5, SECONDS)
            .untilAsserted(() -> {
                then(scheduler).should().checkExists(DAILY_JOB_NAME, JOB_GROUP_NAME);
                then(scheduler).should().deleteJob(DAILY_JOB_NAME, JOB_GROUP_NAME);
            });
    }

    @Test
    public void initialize_doesNotScheduleAnyJob_whenNoCronExpressionIsSet() throws RepositoryException {

        // given
        System.setProperty("devzone.apispec.sync.schedule-delay-duration", "PT0.5S");

        // when
        apiSpecSyncFromApigeeModule.initialize(session);

        // then
        with()
            .pollDelay(1, SECONDS)
            .await()
            .atMost(2, SECONDS)
            .untilAsserted(() ->
                then(scheduler).should(never()).scheduleJob(any(), any())
            );
    }

    @Test
    public void shutdown_deletesScheduledJobs_whenMatchingJobsPreviouslyScheduled() throws RepositoryException {

        // given
        given(scheduler.checkExists(any(), any())).willReturn(true);

        // when
        apiSpecSyncFromApigeeModule.shutdown();

        // then
        then(scheduler).should().checkExists(DAILY_JOB_NAME, JOB_GROUP_NAME);
        then(scheduler).should().deleteJob(DAILY_JOB_NAME, JOB_GROUP_NAME);
    }

    @Test
    public void shutdown_doesNothing_whenNoMatchingJobsPreviouslyScheduled() throws RepositoryException {

        // given
        given(scheduler.checkExists(DAILY_JOB_NAME, JOB_GROUP_NAME)).willReturn(false);

        // when
        apiSpecSyncFromApigeeModule.shutdown();

        // then
        then(scheduler).should(never()).deleteJob(any(), any());
    }

    private void unsetSystemProperties() {
        System.getProperties().remove("devzone.apispec.sync.daily-cron-expression");
        System.getProperties().remove("devzone.apispec.sync.schedule-delay-duration");
    }
}
