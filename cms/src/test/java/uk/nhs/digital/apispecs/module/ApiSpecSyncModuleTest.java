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
import uk.nhs.digital.apispecs.jobs.ApiSpecSyncFromProxygenJob;

import java.util.List;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HippoServiceRegistry.class})
@PowerMockIgnore({"org.awaitility.*", "java.util.concurrent.*"})
public class ApiSpecSyncModuleTest {

    public static final String DAILY_APIGEE_JOB_NAME = "apiSpecSyncFromApigee";
    public static final String DAILY_PROXYGEN_JOB_NAME = "apiSpecSyncFromProxygen";
    public static final String JOB_GROUP_NAME = "devzone";
    private static final String TRIGGER_NAME = "cronTrigger";

    @Mock private RepositoryScheduler scheduler;
    @Mock private Session session;

    private ArgumentCaptor<RepositoryJobInfo> jobInfoArgCaptor = ArgumentCaptor.forClass(RepositoryJobInfo.class);
    private ArgumentCaptor<RepositoryJobTrigger> jobTriggerArgCaptor = ArgumentCaptor.forClass(RepositoryJobTrigger.class);

    private final ApiSpecSyncModule apiSpecSyncModule = new ApiSpecSyncModule();

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

        System.setProperty("devzone.apispec.sync.apigee.daily-cron-expression", expectedDailyCronExpression);
        System.setProperty("devzone.apispec.sync.proxygen.daily-cron-expression", expectedDailyCronExpression);
        System.setProperty("devzone.apispec.sync.schedule-delay-duration", "PT0.5S");

        // when
        apiSpecSyncModule.initialize(session);

        // then
        with()
            .pollDelay(1, SECONDS)
            .await()
            .atMost(5, SECONDS)
            .untilAsserted(() ->
                then(scheduler).should(times(2)).scheduleJob(any(), any())
            );

        then(scheduler).should(times(2)).scheduleJob(
            jobInfoArgCaptor.capture(),
            jobTriggerArgCaptor.capture()
        );

        final List<RepositoryJobInfo> actualJobInfos = jobInfoArgCaptor.getAllValues();

        final RepositoryJobInfo dailyApigeeJobInfo = actualJobInfos.get(0);
        assertThat("Daily Apigee job is scheduled with correct group.", dailyApigeeJobInfo.getGroup(), is(JOB_GROUP_NAME));
        assertThat("Daily Apigee job is scheduled with correct name.", dailyApigeeJobInfo.getName(), is(DAILY_APIGEE_JOB_NAME));
        assertThat("Daily Apigee job is scheduled with correct class.", dailyApigeeJobInfo.getJobClass(), is(ApiSpecSyncFromApigeeJob.class));

        final RepositoryJobInfo dailyProxygenJobInfo = actualJobInfos.get(1);
        assertThat("Daily Proxygen job is scheduled with correct group.", dailyProxygenJobInfo.getGroup(), is(JOB_GROUP_NAME));
        assertThat("Daily Proxygen job is scheduled with correct name.", dailyProxygenJobInfo.getName(), is(DAILY_PROXYGEN_JOB_NAME));
        assertThat("Daily Proxygen job is scheduled with correct class.", dailyProxygenJobInfo.getJobClass(), is(ApiSpecSyncFromProxygenJob.class));

        final List<RepositoryJobTrigger> actualJobTriggers = jobTriggerArgCaptor.getAllValues();

        final RepositoryJobTrigger dailyApigeeJobTrigger = actualJobTriggers.get(0);
        assertThat("Daily Apigee job is scheduled as a cron job.", dailyApigeeJobTrigger, instanceOf(RepositoryJobCronTrigger.class));
        assertThat("Daily Apigee job is scheduled with correct trigger name", dailyApigeeJobTrigger.getName(), is(TRIGGER_NAME));
        assertThat("Daily Apigee job is scheduled with correct cron expression",
            ((RepositoryJobCronTrigger) dailyApigeeJobTrigger).getCronExpression(), is(expectedDailyCronExpression));

        final RepositoryJobTrigger dailyProxygenJobTrigger = actualJobTriggers.get(1);
        assertThat("Daily Proxygen job is scheduled as a cron job.", dailyProxygenJobTrigger, instanceOf(RepositoryJobCronTrigger.class));
        assertThat("Daily Proxygen job is scheduled with correct trigger name", dailyProxygenJobTrigger.getName(), is(TRIGGER_NAME));
        assertThat("Daily Proxygen job is scheduled with correct cron expression",
            ((RepositoryJobCronTrigger) dailyProxygenJobTrigger).getCronExpression(), is(expectedDailyCronExpression));
    }

    @Test
    public void initialize_deactivatesOldJobs_whenPreviousJobInstancesExist() throws RepositoryException {

        // given
        given(scheduler.checkExists(any(), any())).willReturn(true);

        System.setProperty("devzone.apispec.sync.schedule-delay-duration", "PT1S");

        // when
        apiSpecSyncModule.initialize(session);

        // then
        with()
            .pollDelay(1, SECONDS)
            .await()
            .atMost(5, SECONDS)
            .untilAsserted(() -> {
                then(scheduler).should().checkExists(DAILY_APIGEE_JOB_NAME, JOB_GROUP_NAME);
                then(scheduler).should().checkExists(DAILY_PROXYGEN_JOB_NAME, JOB_GROUP_NAME);
                then(scheduler).should().deleteJob(DAILY_APIGEE_JOB_NAME, JOB_GROUP_NAME);
                then(scheduler).should().deleteJob(DAILY_PROXYGEN_JOB_NAME, JOB_GROUP_NAME);
            });
    }

    @Test
    public void initialize_doesNotScheduleAnyJob_whenNoCronExpressionIsSet() throws RepositoryException {

        // given
        System.setProperty("devzone.apispec.sync.schedule-delay-duration", "PT0.5S");

        // when
        apiSpecSyncModule.initialize(session);

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
        apiSpecSyncModule.shutdown();

        // then
        then(scheduler).should().checkExists(DAILY_APIGEE_JOB_NAME, JOB_GROUP_NAME);
        then(scheduler).should().checkExists(DAILY_PROXYGEN_JOB_NAME, JOB_GROUP_NAME);
        then(scheduler).should().deleteJob(DAILY_APIGEE_JOB_NAME, JOB_GROUP_NAME);
        then(scheduler).should().deleteJob(DAILY_PROXYGEN_JOB_NAME, JOB_GROUP_NAME);
    }

    @Test
    public void shutdown_doesNothing_whenNoMatchingJobsPreviouslyScheduled() throws RepositoryException {

        // given
        given(scheduler.checkExists(DAILY_APIGEE_JOB_NAME, JOB_GROUP_NAME)).willReturn(false);
        given(scheduler.checkExists(DAILY_PROXYGEN_JOB_NAME, JOB_GROUP_NAME)).willReturn(false);

        // when
        apiSpecSyncModule.shutdown();

        // then
        then(scheduler).should(never()).deleteJob(any(), any());
    }

    private void unsetSystemProperties() {
        System.getProperties().remove("devzone.apispec.sync.apigee.daily-cron-expression");
        System.getProperties().remove("devzone.apispec.sync.proxygen.daily-cron-expression");
        System.getProperties().remove("devzone.apispec.sync.schedule-delay-duration");
    }
}
