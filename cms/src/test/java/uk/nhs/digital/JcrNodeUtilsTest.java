package uk.nhs.digital;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.onehippo.repository.mock.MockNode;
import org.onehippo.repository.mock.MockNodeIterator;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;
import javax.jcr.*;

public class JcrNodeUtilsTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void streamOf_returnsNodesFromNodeIteratorAsStream() {

        // given
        final MockNode nodeA = new MockNode("nodeA");
        final MockNode nodeB = new MockNode("nodeB");
        final MockNode nodeC = new MockNode("nodeC");

        final NodeIterator nodeIterator = new MockNodeIterator(asList(nodeA, nodeB, nodeC));

        // when
        final Stream<Node> actualNodeStream = JcrNodeUtils.streamOf(nodeIterator);

        // then
        assertThat(
            "Returned stream contains nodes from the iterator, exactly.",
            actualNodeStream.collect(toList()),
            containsInAnyOrder(nodeA, nodeB, nodeC)
        );
    }

    @Test
    public void getSessionQuietly_returnsNodeSession() throws RepositoryException {

        // given
        final Session expectedSession = mock(Session.class);

        final Node node = mock(Node.class);
        given(node.getSession()).willReturn(expectedSession);

        // when
        final Session actualSession = JcrNodeUtils.getSessionQuietly(node);

        // then
        assertThat(
            "Actual session returned is the one associated with the node.",
            actualSession,
            sameInstance(expectedSession)
        );
    }

    @Test
    public void getSessionQuietly_throwsException_onFailure() throws RepositoryException {

        // given
        final RepositoryException repositoryException = new RepositoryException();

        final Node node = mock(Node.class);
        given(node.getSession()).willThrow(repositoryException);

        expectedException.expect(ExceptionUtils.UncheckedWrappingException.class);
        expectedException.expectCause(sameInstance(repositoryException));

        // when
        JcrNodeUtils.getSessionQuietly(node);

        // then
        // expectations set in 'given' are satisfied
    }

    @Test
    public void getStringPropertyQuietly_returnsValueOfGivenPropertyOnGivenNode() throws RepositoryException {

        // given
        final String expectedPropertyName = "expectedPropertyName";
        final String expectedPropertyValue = "expectedPropertyValue";

        final Property expectedProperty = mock(Property.class);
        given(expectedProperty.getString()).willReturn(expectedPropertyValue);

        final Node node = mock(Node.class);
        given(node.getProperty(expectedPropertyName)).willReturn(expectedProperty);
        given(node.hasProperty(expectedPropertyName)).willReturn(true);

        // when
        final Optional<String> actualPropertyValue = JcrNodeUtils.getStringPropertyQuietly(node,
            expectedPropertyName);

        // then
        assertThat(
            "Value returned was read from the given node",
            actualPropertyValue.get(),
            is(expectedPropertyValue)
        );
    }

    @Test
    public void getStringPropertyQuietly_throwsException_onFailure() throws RepositoryException {

        // given
        final RepositoryException repositoryException = new RepositoryException();

        final Node node = mock(Node.class);
        given(node.getProperty(any())).willThrow(repositoryException);
        given(node.hasProperty(any())).willReturn(true);

        expectedException.expect(ExceptionUtils.UncheckedWrappingException.class);
        expectedException.expectCause(sameInstance(repositoryException));

        // when
        JcrNodeUtils.getStringPropertyQuietly(node, "aProperty");

        // then
        // expectations set in 'given' are satisfied
    }

    @Test
    public void setStringPropertyQuietly_setsGivenValueOnGivenNodeInGivenProperty() throws RepositoryException {

        // given
        final Node node = mock(Node.class);

        final String expectedPropertyName = "expectedPropertyName";
        final String expectedPropertyValue = "expectedPropertyValue";

        // when
        JcrNodeUtils.setStringPropertyQuietly(node, expectedPropertyName, expectedPropertyValue);

        // then
        then(node).should().setProperty(expectedPropertyName, expectedPropertyValue);
    }

    @Test
    public void setStringPropertyQuietly_throwsException_onFailure() throws RepositoryException {

        // given
        final RepositoryException repositoryException = new RepositoryException();

        final Node node = mock(Node.class);
        given(node.setProperty(any(String.class), any(String.class))).willThrow(repositoryException);

        expectedException.expect(ExceptionUtils.UncheckedWrappingException.class);
        expectedException.expectCause(sameInstance(repositoryException));

        // when
        JcrNodeUtils.setStringPropertyQuietly(node, "aPropertyName", "aPropertyValue");

        // then
        // expectations set in 'given' are satisfied
    }

    @Test
    public void getInstantPropertyQuietly_returnsValueOfGivenPropertyOnGivenNode() throws RepositoryException {

        // given
        final String expectedPropertyName = "expectedPropertyName";
        final String expectedPropertyValue = "2020-05-04T10:30:00.000Z";

        final Property expectedProperty = mock(Property.class);
        given(expectedProperty.getString()).willReturn(expectedPropertyValue);

        final Node node = mock(Node.class);
        given(node.getProperty(expectedPropertyName)).willReturn(expectedProperty);
        given(node.hasProperty(expectedPropertyName)).willReturn(true);

        // when
        final Optional<Instant> actualValue =
            JcrNodeUtils.getInstantPropertyQuietly(node, expectedPropertyName);

        // then
        assertThat(
            "Value returned was read from the given node",
            actualValue.get(),
            is(Instant.parse(expectedPropertyValue))
        );
    }

    @Test
    public void getInstantPropertyQuietly_throwsException_onFailure() throws RepositoryException {

        // given
        final RepositoryException repositoryException = new RepositoryException();

        final Node node = mock(Node.class);
        given(node.getProperty(any(String.class))).willThrow(repositoryException);
        given(node.hasProperty(any(String.class))).willReturn(true);

        expectedException.expect(ExceptionUtils.UncheckedWrappingException.class);
        expectedException.expectCause(sameInstance(repositoryException));

        // when
        JcrNodeUtils.getInstantPropertyQuietly(node, "aPropertyName");

        // then
        // expectations set in 'given' are satisfied
    }

    @Test
    public void validateIsOfTypeHandle_deemsNodeValid_whenItsJcrPrimaryTypeIsHippoHandle() throws RepositoryException {

        // given
        final Node node = mock(Node.class);
        given(node.isNodeType("hippo:handle")).willReturn(true);

        // when
        JcrNodeUtils.validateIsOfTypeHandle(node);

        // then
        // no exception is thrown
    }

    @Test
    public void validateIsOfTypeHandle_throwsException_whenItsJcrPrimaryTypeIsNotHippoHandle() throws RepositoryException {

        // given
        final Node node = mock(Node.class);
        given(node.isNodeType("hippo:handle")).willReturn(false);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Node's 'jcr:primaryType' property has to be of type 'hippo:handle'");

        // when
        JcrNodeUtils.validateIsOfTypeHandle(node);

        // then
        // expectations set in 'given' are satisfied
    }
}
