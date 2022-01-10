package uk.nhs.digital.toolbox.jcr;

import javax.jcr.Session;

/**
 * <p>
 * Makes it easier to test classes that require a session normally provided
 * through static methods.
 * <p>
 * Mocking this class is easier than doing so with static methods.
 */
public interface SessionProvider {

    Session session();
}
