package uk.nhs.digital;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.site.HstServices;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.nhs.digital.test.mockito.MockitoSessionTestBase;
import uk.nhs.digital.toolbox.jcr.SessionProvider;

import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class HstSessionUtilsTest extends MockitoSessionTestBase {

    @Mock ComponentManager componentManager;
    @Mock Credentials credentials;
    @Mock Repository repository;
    @Mock Session session;

    private ComponentManager originalComponentManager;

    @Before
    public void setUp() throws Exception {
        originalComponentManager = HstServices.getComponentManager();
        HstServices.setComponentManager(componentManager);

        given(componentManager.getComponent("javax.jcr.Credentials.default")).willReturn(credentials);
        given(componentManager.getComponent("javax.jcr.Repository")).willReturn(repository);
    }

    @After
    public void tearDown() {
        HstServices.setComponentManager(originalComponentManager);
    }

    @Test
    public void returnsContentReadOnlySession() throws RepositoryException {

        // given
        given(repository.login(credentials)).willReturn(session);

        // when
        final Session actualSession = HstSessionUtils.contentReadOnlySession();

        // then
        assertThat(
            "Returns pooled JCR session obtained for 'liveuser'.",
            actualSession,
            sameInstance(session)
        );

        then(session).shouldHaveNoInteractions();
    }

    @Test
    public void throwsException_onFailureToObtainContentReadOnlySession() throws RepositoryException {

        // given
        final RepositoryException cause = new RepositoryException("FAILED TO LOGIN");

        given(repository.login(credentials)).willThrow(cause);

        // when
        final RuntimeException actualException = Assert.assertThrows(
            RuntimeException.class,
            HstSessionUtils::contentReadOnlySession
        );

        // then
        assertThat(
            "Error message",
            actualException.getMessage(),
            is("Failed to obtain pooled JCR session of type 'default'.")
        );

        assertThat(
            "Cause",
            actualException.getCause(),
            sameInstance(cause)
        );
    }

    @Test
    public void returnsContentReadOnlySessionProvider() throws RepositoryException {

        // given
        given(repository.login(credentials)).willReturn(session);

        // when
        final SessionProvider contentReadOnlySessionProvider = HstSessionUtils.contentReadOnlySessionProvider();

        // then
        final Session actualSession = contentReadOnlySessionProvider.session();

        assertThat(
            "Returns pooled JCR session obtained for 'liveuser'.",
            actualSession,
            sameInstance(session)
        );

        then(session).shouldHaveNoInteractions();
    }
}