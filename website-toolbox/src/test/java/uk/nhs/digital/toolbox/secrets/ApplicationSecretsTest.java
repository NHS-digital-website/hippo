package uk.nhs.digital.toolbox.secrets;

import static ch.qos.logback.classic.Level.WARN;
import static org.junit.Assert.assertEquals;
import static uk.nhs.digital.test.TestLogger.LogAssertor.warn;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import uk.nhs.digital.test.TestLoggerRule;

import java.util.HashMap;
import java.util.Map;

public class ApplicationSecretsTest {

    private Map<String, String> cache;
    private ApplicationSecrets applicationSecrets;

    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @Rule
    public TestLoggerRule logger = TestLoggerRule.targeting(ApplicationSecrets.class, WARN);

    @Before
    public void setUp() throws Exception {
        cache = new HashMap<>();
        applicationSecrets = new ApplicationSecrets(cache);
    }

    @Test
    public void retrieveEnvironmentVariableViaApplicationSecrets() {
        // Set a mock environment variable
        environmentVariables.set("GOOGLE_CAPTCHA_SECRET", "123456789");

        // Check the value of the environment variable is retrievable via ApplicationSecrets
        assertEquals("123456789", applicationSecrets.getValue("GOOGLE_CAPTCHA_SECRET"));
    }

    @Test
    public void validateApplicationSecretsCache() {
        // Set a mock environment variable
        environmentVariables.set("GOOGLE_CAPTCHA_SECRET", "123456789");

        // First the cache is empty
        assertEquals(0, cache.size());

        // Then when retrieving a value this should set the cache too
        applicationSecrets.getValue("GOOGLE_CAPTCHA_SECRET");
        assertEquals(1, cache.size());
        assertEquals("123456789", cache.get("GOOGLE_CAPTCHA_SECRET"));
    }

    @Test
    public void missingKeysAreLoggedByApplicationSecrets() {
        String key = "GOOGLE_CAPTCHA_SECRET";

        // Then look for something that does not exist
        applicationSecrets.getValue(key);

        // And make sure the missing key was logged
        logger.shouldReceive(
            warn("The key/value (or address of a remote value) for '"
                + key
                + "', should be set as a Java Property or an Environment variable or a file in {catalina.base}/conf.")
        );
    }

    @Test
    public void maskAllWhenShorterThan10() {
        assertEquals("", ApplicationSecrets.mask(""));
        assertEquals("*", ApplicationSecrets.mask("1"));
        assertEquals("**********", ApplicationSecrets.mask("123456789a"));
    }

    @Test
    public void maskAllButLast1When11Long() {
        assertEquals("**********b", ApplicationSecrets.mask("123456789ab"));
    }

    @Test
    public void maskAllButLast2When12Long() {
        assertEquals("**********bc", ApplicationSecrets.mask("123456789abc"));
    }

    @Test
    public void maskAllButLast3When13Long() {
        assertEquals("**********bcd", ApplicationSecrets.mask("123456789abcd"));
    }

    @Test
    public void maskAllButLast4When14Long() {
        assertEquals("**********bcde", ApplicationSecrets.mask("123456789abcde"));
    }

    @Test
    public void maskAllExceptLast4When15Longer() {
        assertEquals("***********cdef", ApplicationSecrets.mask("123456789abcdef"));
        assertEquals("************defg", ApplicationSecrets.mask("123456789abcdefg"));
        assertEquals("*******************************wxyz", ApplicationSecrets.mask("123456789abcdefghijklmnopqrstuvwxyz"));
    }
}