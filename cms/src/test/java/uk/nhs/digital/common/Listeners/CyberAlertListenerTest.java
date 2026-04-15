package uk.nhs.digital.common.listeners;

import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;

import org.hippoecm.repository.api.HippoNode;
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
public class CyberAlertListenerTest {


    private static final String METHOD_NAME_KEY         = "methodName";
    private static final String PUBLISH_METHOD          = "publish";
    private static final String SUBJECT_ID              = "test-subject-id";

    @Mock private HippoWorkflowEvent event;
    @Mock private Session            daemonSession;
    @Mock private Session            impersonatedSession;
    @Mock private HippoNode handle;

    private CyberAlertListener listener;

    @Before
    public void setUp() {
        listener = new CyberAlertListener();
        listener.initialize(daemonSession);
    }


    @Test
    public void handleEvent_doesNothing_whenEventIsNotSuccessful() throws RepositoryException {
        given(event.success()).willReturn(false);

        listener.handleEvent(event);

        then(daemonSession).should(never()).impersonate(any());
    }

    @Test
    public void handleEvent_doesNothing_whenMethodNameIsNotPublish() throws RepositoryException {
        given(event.success()).willReturn(true);
        given(event.get(METHOD_NAME_KEY)).willReturn("depublish");

        listener.handleEvent(event);

        then(daemonSession).should(never()).impersonate(any());
    }


    // Session lifecycle
    @Test
    public void handleEvent_impersonatesSession_forSuccessfulPublishEvent() throws RepositoryException {
        givenValidPublishEvent();
        given(handle.hasNodes()).willReturn(false);

        listener.handleEvent(event);

        then(daemonSession).should().impersonate(any());
    }

    @Test
    public void handleEvent_savesAndLogsOutSession_afterProcessing() throws RepositoryException {
        givenValidPublishEvent();
        given(handle.hasNodes()).willReturn(false);

        listener.handleEvent(event);

        then(impersonatedSession).should().save();
        then(impersonatedSession).should().logout();
    }

    @Test
    public void handleEvent_logsOutSession_evenWhenRepositoryExceptionThrown() throws RepositoryException {
        givenValidPublishEvent();
        given(impersonatedSession.getNodeByIdentifier(SUBJECT_ID)).willThrow(new RepositoryException("test error"));

        listener.handleEvent(event);

        then(impersonatedSession).should().logout();
    }

    private void givenValidPublishEvent() throws RepositoryException {
        given(event.success()).willReturn(true);
        given(event.get(METHOD_NAME_KEY)).willReturn(PUBLISH_METHOD);
        given(event.subjectId()).willReturn(SUBJECT_ID);
        given(daemonSession.impersonate(any())).willReturn(impersonatedSession);
        given(impersonatedSession.getNodeByIdentifier(SUBJECT_ID)).willReturn(handle);
    }


}
