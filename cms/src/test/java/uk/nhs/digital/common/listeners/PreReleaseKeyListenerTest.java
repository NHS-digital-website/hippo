package uk.nhs.digital.common.listeners;

import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.onehippo.repository.events.HippoWorkflowEvent;
import uk.nhs.digital.common.earlyaccesskey.ProcessSearch;
import uk.nhs.digital.test.util.ReflectionTestUtils;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

@RunWith(MockitoJUnitRunner.class)
public class PreReleaseKeyListenerTest {

    @Mock private Session daemonSession;
    @Mock private Session impersonatedSession;
    @Mock private ProcessSearch processSearch;
    @Mock private HippoWorkflowEvent event;

    private PreReleaseKeyListener listener;

    @Before
    public void setUp() {
        listener = new PreReleaseKeyListener();
        ReflectionTestUtils.setField(listener, "processSearch", processSearch);
    }

    @Test
    public void handleEvent_delegatesToProcessSearch_forSuccessfulPublishEvent() throws RepositoryException {

        listener.initialize(daemonSession);
        given(event.success()).willReturn(true);
        given(event.get("methodName")).willReturn("publish");
        given(daemonSession.impersonate(any())).willReturn(impersonatedSession);

        listener.handleEvent(event);

        then(processSearch).should().processPreReleaseContentSearch(event, impersonatedSession);
    }

    @Test
    public void handleEvent_doesNotDelegateToProcessSearch_forIneligibleEvent() {

        listener.initialize(daemonSession);
        given(event.success()).willReturn(false);

        listener.handleEvent(event);

        then(processSearch).should(never()).processPreReleaseContentSearch(any(), any());
    }

    @Test
    public void initialize_storesProvidedSession() {

        listener.initialize(daemonSession);

        assertSame(daemonSession, ReflectionTestUtils.readField(listener, "session"));
    }

    @Test
    public void handleEvent_impersonatesSession_forSuccessfulPublishEvent() throws RepositoryException {

        listener.initialize(daemonSession);
        given(event.success()).willReturn(true);
        given(event.get("methodName")).willReturn("publish");
        given(daemonSession.impersonate(any())).willReturn(impersonatedSession);

        listener.handleEvent(event);

        then(daemonSession).should().impersonate(any());
        then(processSearch).should().processPreReleaseContentSearch(event, impersonatedSession);
    }

    @Test
    public void handleEvent_logsOutImpersonatedSession_afterSuccessfulProcessing() throws RepositoryException {

        listener.initialize(daemonSession);
        given(event.success()).willReturn(true);
        given(event.get("methodName")).willReturn("publish");
        given(daemonSession.impersonate(any())).willReturn(impersonatedSession);

        listener.handleEvent(event);

        then(impersonatedSession).should().logout();
    }

    @Test
    public void handleEvent_logsOutImpersonatedSession_whenProcessingFails() throws RepositoryException {

        listener.initialize(daemonSession);
        given(event.success()).willReturn(true);
        given(event.get("methodName")).willReturn("publish");
        given(daemonSession.impersonate(any())).willReturn(impersonatedSession);
        willThrow(new RuntimeException("boom"))
            .given(processSearch).processPreReleaseContentSearch(event, impersonatedSession);

        try {
            listener.handleEvent(event);
        } catch (RuntimeException expected) {
            // Expected until production code handles the new session lifecycle.
        }

        then(impersonatedSession).should().logout();
    }
}
