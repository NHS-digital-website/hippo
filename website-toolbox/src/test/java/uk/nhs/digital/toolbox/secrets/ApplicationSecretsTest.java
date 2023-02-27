package uk.nhs.digital.toolbox.secrets;

import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;
import static uk.nhs.digital.test.TestLogger.LogAssertor.debug;
import static uk.nhs.digital.test.TestLogger.LogAssertor.warn;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.slf4j.Logger;
import uk.nhs.digital.test.TestLoggerRule;

import java.util.HashMap;
import java.util.Map;

public class ApplicationSecretsTest {

    private Map<String, String> cache;
    private ApplicationSecrets applicationSecrets;

    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @Rule
    public TestLoggerRule logger = TestLoggerRule.targeting(ApplicationSecrets.class);

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

    @Test
    public void getValueChained_chainsCorrectlyAtOneLevel() {
        environmentVariables.set("LEVEL1", "LEVEL2");

        String value1 = applicationSecrets.getValueChained("LEVEL1");
        assertEquals("LEVEL2", value1);
    }

    @Test
    public void getValueChained_chainsCorrectlyAtThreeLevels() {
        environmentVariables.set("LEVEL1", "LEVEL2");
        environmentVariables.set("LEVEL2", "LEVEL3");
        environmentVariables.set("LEVEL3", "LEVEL4");

        String value1 = applicationSecrets.getValueChained("LEVEL1");
        assertEquals("LEVEL4", value1);

        String value2 = applicationSecrets.getValueChained("LEVEL2");
        assertEquals("LEVEL4", value2);

        String value3 = applicationSecrets.getValueChained("LEVEL3");
        assertEquals("LEVEL4", value3);
    }

    /**
     * Don't set cache key LEVEL1 to value LEVEL4 in case a call to
     * {@link ApplicationSecrets#getValue} wants an intermediate value.
     */
    @Test
    public void getValueChained_cacheDoesNotShortCut() {
        environmentVariables.set("LEVEL1", "LEVEL2");
        environmentVariables.set("LEVEL2", "LEVEL3");
        environmentVariables.set("LEVEL3", "LEVEL4");

        applicationSecrets.getValueChained("LEVEL1");

        assertEquals(3, cache.size());
        assertEquals("LEVEL2", cache.get("LEVEL1"));
        assertEquals("LEVEL2", cache.get("LEVEL1"));
        assertEquals("LEVEL3", cache.get("LEVEL2"));
        assertEquals("LEVEL4", cache.get("LEVEL3"));
    }

    /**
     * Chaining stops by failing to find the next value. Don't log this as it can't
     * be known if it's unintentional.
     */
    @Test
    public void getValueChained_doesNotLogFinalGet() {
        environmentVariables.set("LEVEL1", "LEVEL2");
        environmentVariables.set("LEVEL2", "LEVEL3");
        environmentVariables.set("LEVEL3", "LEVEL4");

        applicationSecrets.getValueChained("LEVEL1");

        Logger classLogger = getLogger(ApplicationSecrets.class);
        classLogger.debug("End");

        logger.shouldReceive(debug("End"));
    }

    @Test
    public void getValueChained_logsInitialMiss() {
        environmentVariables.set("LEVEL1", "LEVEL2");
        environmentVariables.set("LEVEL2", "LEVEL3");
        environmentVariables.set("LEVEL3", "LEVEL4");

        applicationSecrets.getValueChained("LEVEL4");

        logger.shouldReceive(
            warn("The key/value (or address of a remote value) for '"
                + "LEVEL4"
                + "', should be set as a Java Property or an Environment variable or a file in {catalina.base}/conf.")
        );
    }
}