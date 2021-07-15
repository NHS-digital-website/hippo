package uk.nhs.digital.toolbox.secrets;

import static org.junit.Assert.*;

import org.junit.Test;
import uk.nhs.digital.toolbox.secrets.RemoteSecrets;

public class RemoteSecretsTest {

    RemoteSecrets secrets = new RemoteSecrets() {
        @Override
        public String getRemoteValue(String key) {
            return null;
        }
    };

    @Test
    public void isRemote() {
        assertTrue(secrets.isRemoteValue("REMOTE::/address/of/a/remote/value"));
        assertTrue(secrets.isRemoteValue("remote::/address/of/a/remote/value"));
        assertTrue(secrets.isRemoteValue("Remote::/address/of/a/remote/value"));
    }

    @Test
    public void isNotSeenAsRemote() {
        assertFalse(secrets.isRemoteValue("NOT_THE_CORRECT_REMOTE_SCHEMA::/address/of/a/remote/value"));
        assertFalse(secrets.isRemoteValue("REMOTE/address/of/a/remote/value"));
        assertFalse(secrets.isRemoteValue("the-value"));
    }

    @Test
    public void removesRemote() {
        assertEquals(secrets.toRemoteAddress("REMOTE::/address/of/a/remote/value"), "/address/of/a/remote/value");
    }

    @Test
    public void doesNotRemoveAnythingIfRemoteMissing() {
        assertEquals(secrets.toRemoteAddress("NOT_THE_CORRECT_REMOTE_SCHEMA::/address/of/a/remote/value"), "NOT_THE_CORRECT_REMOTE_SCHEMA::/address/of/a/remote/value");
        assertEquals(secrets.toRemoteAddress("normal_value"), "normal_value");
    }

}
