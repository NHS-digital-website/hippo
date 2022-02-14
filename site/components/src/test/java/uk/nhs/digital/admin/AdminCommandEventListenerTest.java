package uk.nhs.digital.admin;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static uk.nhs.digital.test.TestLogger.LogAssertor.*;
import static uk.nhs.digital.test.util.RandomTestUtils.randomString;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.onehippo.cms7.event.HippoEvent;
import org.onehippo.cms7.event.HippoEventConstants;
import uk.nhs.digital.test.TestLoggerRule;
import uk.nhs.digital.test.mockito.MockitoSessionTestBase;

import java.util.Set;

@RunWith(DataProviderRunner.class)
public class AdminCommandEventListenerTest extends MockitoSessionTestBase {

    public static final String VALID_DOCUMENT_PATH = "/content/documents/administration/admin-command-interface";

    @Rule public TestLoggerRule logger = TestLoggerRule.targeting(AdminCommandEventListener.class);

    @Mock AdminCommandProvider adminCommandProvider;
    @Mock AdminCommandEvenHandler adminCommandEvenHandlerA;
    @Mock AdminCommandEvenHandler adminCommandEvenHandlerB;
    @Mock AdminCommandEvenHandler adminCommandEvenHandlerC;

    private Set<AdminCommandEvenHandler> adminCommandEvenHandlers;
    private AdminCommandEventListener listener;

    @Before
    public void setUp() throws Exception {
        adminCommandEvenHandlers = ImmutableSet.of(
            adminCommandEvenHandlerA,
            adminCommandEvenHandlerB,
            adminCommandEvenHandlerC
        );
    }

    @Test
    public void newInstanceIsConfiguredWithCorrectInitialValues() {

        // given
        // setUp()

        // when
        listener = new AdminCommandEventListener(adminCommandProvider, adminCommandEvenHandlers);

        // then
        assertThat(
            "Event category makes the listener respond only to document workflow events.",
            listener.getEventCategory(),
            is(HippoEventConstants.CATEGORY_WORKFLOW)
        );

        assertThat(
            "Listener listens on event channel with a node-unique name.",
            listener.getChannelName(),
            is("admin-commands")
        );

        assertThat(
            "Listeners consumes event emitted before after its registration",
            listener.onlyNewEvents(),
            is(true)
        );
    }

    @Test
    public void testsAllHandlersForEligibleEvent() {

        // given
        listener = new AdminCommandEventListener(adminCommandProvider, adminCommandEvenHandlers);

        final AdminCommand expectedCommand = AdminCommand.with("TEST-ADMIN-COMMAND", ImmutableList.of());

        given(adminCommandProvider.currentCommandFromAdminCommandInterface())
            .willReturn(expectedCommand);

        final String documentId = randomString();
        final String userId = randomString();

        @SuppressWarnings("rawtypes") final HippoEvent eligibleEvent = new HippoEvent("cms")
            .set("documentType", "common:stringlist")
            .set("action", "publish")
            .set("exception", null)
            .set("subjectId", documentId)
            .set("subjectPath", VALID_DOCUMENT_PATH)
            .set("user", userId)
            ;

        // when
        listener.onHippoEvent(eligibleEvent);

        // then
        then(adminCommandEvenHandlerA).should().supports(expectedCommand);
        then(adminCommandEvenHandlerB).should().supports(expectedCommand);
        then(adminCommandEvenHandlerC).should().supports(expectedCommand);

        logger.shouldReceive(
            debug(format("Admin Command Interface document has been published: %s (%s).", VALID_DOCUMENT_PATH, documentId)),
            info(format("Admin Command '%s' received, issued by user '%s'.", expectedCommand, userId))
        );
    }

    @Test
    public void invokesSupportingHandlersForEligibleEvent() {

        // given
        listener = new AdminCommandEventListener(adminCommandProvider, adminCommandEvenHandlers);

        final AdminCommand expectedCommand = AdminCommand.with("TEST-ADMIN-COMMAND", ImmutableList.of());

        given(adminCommandProvider.currentCommandFromAdminCommandInterface())
            .willReturn(expectedCommand);

        when(adminCommandEvenHandlerA.supports(expectedCommand)).thenReturn(true);
        when(adminCommandEvenHandlerB.supports(expectedCommand)).thenReturn(false);
        when(adminCommandEvenHandlerC.supports(expectedCommand)).thenReturn(true);

        final String documentId = randomString();
        final String userId = randomString();

        @SuppressWarnings("rawtypes") final HippoEvent eligibleEvent = new HippoEvent("cms")
            .set("documentType", "common:stringlist")
            .set("action", "publish")
            .set("exception", null)
            .set("subjectId", documentId)
            .set("subjectPath", VALID_DOCUMENT_PATH)
            .set("user", userId)
            ;

        // when
        listener.onHippoEvent(eligibleEvent);

        // then
        then(adminCommandEvenHandlerA).should().execute(expectedCommand);
        then(adminCommandEvenHandlerB).should(never()).execute(any());
        then(adminCommandEvenHandlerC).should().execute(expectedCommand);

        logger.shouldReceive(
            debug(format("Admin Command Interface document has been published: %s (%s).", VALID_DOCUMENT_PATH, documentId)),
            info(format("Admin Command '%s' received, issued by user '%s'.", expectedCommand, userId))
        );
    }

    @Test
    @UseDataProvider("ineligibleEvents")
    public void ignoresIneligibleEventsWith(final String documentType, final String action, final String documentPath, final Exception exception) {

        // given
        listener = new AdminCommandEventListener(adminCommandProvider, adminCommandEvenHandlers);

        final String documentId = randomString();

        @SuppressWarnings("rawtypes")
        final HippoEvent nonEligibleEvent = new HippoEvent("cms")
            .set("documentType", documentType)
            .set("action", action)
            .set("exception", exception)
            .set("subjectId", documentId)
            .set("subjectPath", documentPath);

        // when
        listener.onHippoEvent(nonEligibleEvent);

        // then
        then(adminCommandEvenHandlerA).shouldHaveNoInteractions();
        then(adminCommandEvenHandlerB).shouldHaveNoInteractions();
        then(adminCommandEvenHandlerC).shouldHaveNoInteractions();

        logger.shouldReceive(
            debug(format("Ignoring ineligible event '%s' for document %s", action, documentPath))
        );
    }

    @DataProvider
    public static Object[][] ineligibleEvents() {
        // @formatter:off
        return new Object[][]{
            // documentType           action                  documentPath              exception
            {"unsupported:doctype",   "publish",              VALID_DOCUMENT_PATH,      null},
            {"common:stringlist",     "unsupportedaction",    VALID_DOCUMENT_PATH,      null},
            {"common:stringlist",     "publish",              "invalid/document/path",  null},
            {"common:stringlist",     "publish",              VALID_DOCUMENT_PATH,      new Exception()}
        };
        // @formatter:on
    }

    @Test
    public void invokesWorkingHandlers_evenIfOneOfTheSupportChecksFails() {

        // given
        listener = new AdminCommandEventListener(adminCommandProvider, adminCommandEvenHandlers);

        final AdminCommand expectedCommand = AdminCommand.with("TEST-ADMIN-COMMAND", ImmutableList.of());

        given(adminCommandProvider.currentCommandFromAdminCommandInterface())
            .willReturn(expectedCommand);

        when(adminCommandEvenHandlerA.supports(expectedCommand)).thenReturn(true);
        when(adminCommandEvenHandlerB.supports(expectedCommand)).thenThrow(new RuntimeException("CHECK FAILURE"));
        when(adminCommandEvenHandlerC.supports(expectedCommand)).thenReturn(true);

        final String documentId = randomString();
        final String userId = randomString();

        @SuppressWarnings("rawtypes") final HippoEvent eligibleEvent = new HippoEvent("cms")
            .set("documentType", "common:stringlist")
            .set("action", "publish")
            .set("exception", null)
            .set("subjectId", documentId)
            .set("subjectPath", VALID_DOCUMENT_PATH)
            .set("user", userId)
            ;

        // when
        listener.onHippoEvent(eligibleEvent);

        // then
        then(adminCommandEvenHandlerA).should().execute(expectedCommand);
        then(adminCommandEvenHandlerB).should(never()).execute(any());
        then(adminCommandEvenHandlerC).should().execute(expectedCommand);

        logger.shouldReceive(
            debug(format("Admin Command Interface document has been published: %s (%s).", VALID_DOCUMENT_PATH, documentId)),
            info(format("Admin Command '%s' received, issued by user '%s'.", expectedCommand, userId)),
            error("Failed to determine whether admin command is supported by given handler; command: AdminCommand[keyword=TEST-ADMIN-COMMAND,arguments=[]].")
                .withException("CHECK FAILURE")
        );
    }

    @Test
    public void invokesWorkingHandlers_evenIfExecutionOfOneOfTheHandlersFails() {

        // given
        listener = new AdminCommandEventListener(adminCommandProvider, adminCommandEvenHandlers);

        final AdminCommand expectedCommand = AdminCommand.with("TEST-ADMIN-COMMAND", ImmutableList.of());

        given(adminCommandProvider.currentCommandFromAdminCommandInterface())
            .willReturn(expectedCommand);

        when(adminCommandEvenHandlerA.supports(expectedCommand)).thenReturn(true);
        when(adminCommandEvenHandlerB.supports(expectedCommand)).thenReturn(true);
        when(adminCommandEvenHandlerC.supports(expectedCommand)).thenReturn(true);

        doThrow(new RuntimeException("EXECUTION FAILURE"))
            .when(adminCommandEvenHandlerB)
            .execute(expectedCommand);

        final String documentId = randomString();
        final String userId = randomString();

        @SuppressWarnings("rawtypes") final HippoEvent eligibleEvent = new HippoEvent("cms")
            .set("documentType", "common:stringlist")
            .set("action", "publish")
            .set("exception", null)
            .set("subjectId", documentId)
            .set("subjectPath", VALID_DOCUMENT_PATH)
            .set("user", userId)
            ;

        // when
        listener.onHippoEvent(eligibleEvent);

        // then
        then(adminCommandEvenHandlerA).should().execute(expectedCommand);
        then(adminCommandEvenHandlerC).should().execute(expectedCommand);

        logger.shouldReceive(
            debug(format("Admin Command Interface document has been published: %s (%s).", VALID_DOCUMENT_PATH, documentId)),
            info(format("Admin Command '%s' received, issued by user '%s'.", expectedCommand, userId)),
            error("Failed to handle command: AdminCommand[keyword=TEST-ADMIN-COMMAND,arguments=[]].")
                .withException("EXECUTION FAILURE")
        );
    }
}