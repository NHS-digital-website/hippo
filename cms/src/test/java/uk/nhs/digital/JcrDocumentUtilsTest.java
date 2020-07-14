package uk.nhs.digital;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import org.hippoecm.repository.util.WorkflowUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.onehippo.forge.content.exim.core.DocumentManager;
import org.onehippo.forge.content.exim.core.impl.WorkflowDocumentManagerImpl;
import org.onehippo.repository.documentworkflow.DocumentWorkflow;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WorkflowUtils.class})
public class JcrDocumentUtilsTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void publish_publishesDocumentOfGivenHandle_usingDefaultWorkflow() throws Exception {

        // given
        final DocumentWorkflow workflow = mock(DocumentWorkflow.class);

        mockStatic(WorkflowUtils.class);
        given(WorkflowUtils.getWorkflow(any(Node.class), any(String.class), any()))
            .willReturn(Optional.of(workflow));

        final Node documentHandleNode = validDocumentHandleNode();

        // when
        JcrDocumentUtils.publish(documentHandleNode);

        // then
        verifyStatic(WorkflowUtils.class);
        WorkflowUtils.getWorkflow(documentHandleNode, "default", DocumentWorkflow.class);

        then(workflow).should().publish();
    }

    @Test
    public void publish_throwsException_onFailure() throws RepositoryException {

        // given

        final RuntimeException collaboratorException = new RuntimeException();

        mockStatic(WorkflowUtils.class);
        given(WorkflowUtils.getWorkflow(any(Node.class), any(String.class), any()))
            .willThrow(collaboratorException);

        final Node documentHandleNode = validDocumentHandleNode();

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(startsWith("Failed to publish document "));
        expectedException.expectCause(sameInstance(collaboratorException));

        // when
        JcrDocumentUtils.publish(documentHandleNode);

        // then
        // expectations set in 'given' are satisfied
    }

    @Test
    public void publish_throwsException_whenGivenNodeNotOfTypeHandle() throws RepositoryException {

        // given
        final RuntimeException collaboratorException = new RuntimeException();

        mockStatic(WorkflowUtils.class);
        given(WorkflowUtils.getWorkflow(any(Node.class), any(String.class), any()))
            .willThrow(collaboratorException);

        final Node documentHandleNode = notAValidDocumentHandleNode();

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(startsWith("Failed to publish document "));
        expectedException.expectCause(isA(IllegalArgumentException.class));

        // when
        JcrDocumentUtils.publish(documentHandleNode);

        // then
        // expectations set in 'given' are satisfied
    }

    @Test
    public void saveQuietly_savesGivenSession() throws RepositoryException {

        // given
        final Session session = mock(Session.class);

        // when
        JcrDocumentUtils.saveQuietly(session);

        // then
        then(session).should().save();
    }

    @Test
    public void saveQuietly_throwsException_onError() throws RepositoryException {

        // given
        final RepositoryException repositoryException = new RepositoryException();

        final Session session = mock(Session.class);
        doThrow(repositoryException).when(session).save();

        expectedException.expect(ExceptionUtils.UncheckedWrappingException.class);
        expectedException.expectCause(sameInstance(repositoryException));

        // when
        JcrDocumentUtils.saveQuietly(session);

        // then
        // expectations set in 'given' are satisfied
    }

    @Test
    public void documentManagerFor_returnsInstanceOfWorkflowDocumentManagerImpl_initialisedWithGivenSession() {

        // given
        final Session expectedSession = mock(Session.class);

        // when
        final DocumentManager actualDocumentManager =
            JcrDocumentUtils.documentManagerFor(expectedSession);

        // then
        assertThat(
            "Document manager returned is of correct type.",
            actualDocumentManager,
            isA(WorkflowDocumentManagerImpl.class));

        final Session actualSession = actualDocumentManager.getSession();
        assertThat(
            "Document manager returned is initialised with given session.",
            actualSession,
            sameInstance(expectedSession));
    }

    private Node validDocumentHandleNode() throws RepositoryException {
        final Node documentHandleNode = mock(Node.class);

        given(documentHandleNode.isNodeType("hippo:handle")).willReturn(true);
        return documentHandleNode;
    }

    private Node notAValidDocumentHandleNode() throws RepositoryException {
        final Node documentHandleNode = mock(Node.class);

        given(documentHandleNode.isNodeType("hippo:handle")).willReturn(false);
        return documentHandleNode;
    }
}
