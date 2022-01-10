package uk.nhs.digital;

import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.site.HstServices;
import uk.nhs.digital.toolbox.jcr.SessionProvider;

import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * <p>
 * Convenience methods that simplify use of session-related operations within the delivery tier
 * ('site' application).
 * <p>
 * References:
 * <ul>
 * <li><a href="https://documentation.bloomreach.com/14/library/concepts/web-application/retrieving-a-pooled-session.html">Delivery tier: Retrieving a pooled session</a>
 * </li>
 * <li><a href="https://documentation.bloomreach.com/14/library/concepts/security/hst-users.html">Delivery tier: Delivery Tier Users</a></li>
 * </ul>
 */
public class HstSessionUtils {

    /**
     * <p>
     * Obtains a 'read only' session from a JCR session pool.
     * <p>
     * The session uses credentials of 'liveuser'; for details see References in the class' JavaDoc.
     * @return Session that permits read-only querying of JCR repo.
     */
    public static Session contentReadOnlySession() {
        return getPooledSessionForCredentialsOfType("default");
    }

    /**
     * <p>
     * Obtains one of the pooled sessions identified by {@code credentialsType}.
     * <p>
     * For more details see References in the class' JavaDoc.
     *
     * @param credentialsType Type of one of the 'standard' credentials to obtain the session for.
     * @return One of the pooled sessions identified by {@code credentialsType}.
     */
    private static Session getPooledSessionForCredentialsOfType(final String credentialsType) {

        try {
            final ComponentManager componentManager = HstServices.getComponentManager();

            final Credentials credentials = componentManager.getComponent(Credentials.class.getName() + "." + credentialsType);

            final Repository repository = componentManager.getComponent(Repository.class.getName());

            return repository.login(credentials);

        } catch (final RepositoryException e) {
            throw new RuntimeException("Failed to obtain pooled JCR session of type '" + credentialsType + "'.", e);
        }
    }

    /**
     * @return Session provider that returns {@linkplain #contentReadOnlySession()}.
     */
    public static SessionProvider contentReadOnlySessionProvider() {
        return HstSessionUtils::contentReadOnlySession;
    }
}
