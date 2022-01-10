package uk.nhs.digital.admin;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.spy;
import static uk.nhs.digital.admin.AdminCommand.NULL;
import static uk.nhs.digital.test.util.RandomTestUtils.randomString;

import org.apache.sling.testing.mock.jcr.MockJcr;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.nhs.digital.test.mockito.MockitoSessionTestBase;
import uk.nhs.digital.toolbox.jcr.SessionProvider;

import java.util.ArrayList;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class AdminCommandProviderTest extends MockitoSessionTestBase {

    public static final String ADMIN_COMMAND_INTERFACE_DOC_PATH =
        "/content/documents/administration/admin-command-interface";
    @Mock SessionProvider sessionProvider;

    private Session session;

    private AdminCommandProvider adminCommandProvider;

    @Before
    public void setUp() throws Exception {
        session = spy(MockJcr.newSession());
        given(sessionProvider.session()).willReturn(session);

        adminCommandProvider = new AdminCommandProvider(sessionProvider);
    }

    @Test
    public void producesAdminCommand_fromCurrentVersionOfAdminCommandInterfaceDocument_withArgsWhenSpecified() throws RepositoryException {

        // given
        final AdminCommand expectedAdminCommand = AdminCommand.with(
            randomString(),
            asList(randomString(), randomString())
        );

        givenCmsContainsAdminCommandInterfaceDocumentWith(session, expectedAdminCommand);

        // when
        final AdminCommand actualAdminCommand = adminCommandProvider.currentCommandFromAdminCommandInterface();

        // then
        assertThat(
            "Command matches current state of the Admin Command Interface document.",
            actualAdminCommand,
            is(expectedAdminCommand)
        );

        then(session).should().logout();
    }

    @Test
    public void producesAdminCommand_fromCurrentVersionOfAdminCommandInterfaceDocument_withNoArgsWhenNotSpecified() throws RepositoryException {

        // given
        final AdminCommand expectedAdminCommand = AdminCommand.with(
            randomString(),
            null
        );

        givenCmsContainsAdminCommandInterfaceDocumentWith(session, expectedAdminCommand);

        // when
        final AdminCommand actualAdminCommand = adminCommandProvider.currentCommandFromAdminCommandInterface();

        // then
        assertThat(
            "Command matches current state of the Admin Command Interface document.",
            actualAdminCommand,
            is(expectedAdminCommand)
        );

        then(session).should().logout();
    }

    @Test
    public void producesNullAdminCommand_fromCurrentVersionOfAdminCommandInterfaceDocument_whenNoKeywordSpecified() throws RepositoryException {

        // given
        final AdminCommand expectedAdminCommand = NULL;

        givenCmsContainsAdminCommandInterfaceDocumentWith(session, expectedAdminCommand);

        // when
        final AdminCommand actualAdminCommand = adminCommandProvider.currentCommandFromAdminCommandInterface();

        // then
        assertThat(
            "Command matches current state of the Admin Command Interface document.",
            actualAdminCommand,
            is(expectedAdminCommand)
        );

        then(session).should().logout();
    }

    @Test
    public void throwsExceptionOnFailure() throws RepositoryException {

        // given
        final RepositoryException cause = new RepositoryException("FAILED TO OBTAIN JCR SESSION");

        given(session.nodeExists(ADMIN_COMMAND_INTERFACE_DOC_PATH)).willThrow(cause);

        // when
        final RuntimeException actualException = Assert.assertThrows(
            RuntimeException.class,
            () -> adminCommandProvider.currentCommandFromAdminCommandInterface()
        );

        // then
        assertThat(
            "Error message",
            actualException.getMessage(),
            is("Failed to create AdminCommand from Admin Command Interface document /content/documents/administration/admin-command-interface.")
        );

        assertThat("Cause", actualException.getCause(), sameInstance(cause));

        then(session).should().logout();
    }

    private void givenCmsContainsAdminCommandInterfaceDocumentWith(final Session session, final AdminCommand adminCommand) throws RepositoryException {

        final Node repositoryRootNode = session.getRootNode();
        final Node documentHandleNode = repositoryRootNode.addNode(ADMIN_COMMAND_INTERFACE_DOC_PATH);
        final Node publishedVariantNode = documentHandleNode.addNode("admin-command-interface", "common:stringlist");
        publishedVariantNode.setProperty("hippostd:state", "published");

        if (adminCommand.equals(NULL)) {
            publishedVariantNode.setProperty("common:entries", new String[0]);
            return;
        }

        String[] values = new String[]{adminCommand.keyword()};

        if (!adminCommand.arguments().isEmpty()) {
            final ArrayList<String> list = new ArrayList<>();
            list.add(adminCommand.keyword());
            list.addAll(adminCommand.arguments());
            values = list.toArray(new String[0]);
        }

        publishedVariantNode.setProperty("common:entries", values);

    }
}