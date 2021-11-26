package uk.nhs.digital.test;

import static ch.qos.logback.classic.Level.ALL;

import ch.qos.logback.classic.Level;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.Optional;

/**
 * <p>
 * Provides convenient DSL and assertion to aid in validating that desired statements have been logged.
 * <p>
 * Example of use:
 * <pre>
 *     import static uk.nhs.digital.test.TestLogger.LogAssertor.*;
 *
 *     import org.junit.Rule;
 *     import uk.nhs.digital.test.TestLoggerRule;
 *
 *     class MyTestClass {
 *
 *         private TestLogger logger;
 *
 *         &#64;Rule
 *         public TestLoggerRule logger = TestLoggerRule.targeting(MyClassUnderTest.class);
 *
 *         &#64;Test
 *         public void myTestMethod() {
 *
 *              // given
 *              ...
 *
 *              // when
 *              // Invoke your method under test that generates logs; if your method contains something like:
 *              //
 *              //   log.info("Log message of level {}", "INFO"),
 *              //   log.debug("Log message of level DEBUG"),
 *              //   log.warn("Log message of level WARN"),
 *              //   log.trace("Log message of level TRACE"),
 *              //   log.error("Log message of level ERROR",
 *              //     new RuntimeException("Message of the exception associated with the error log.",
 *              //       new RuntimeException("Message of the CAUSE exception associated with the exception logged as ERROR.")
 *              //     )
 *              //   );
 *             ...
 *
 *             // then
 *             // ...assert that expected statements have been logged:
 *             logger.shouldReceive(
 *                  info("Log message of level INFO"),
 *                  debug("Log message of level DEBUG"),
 *                  warn("Log message of level WARN"),
 *                  trace("Log message of level TRACE"),
 *                  error("Log message of level ERROR")
 *                      .withException("Message of the exception associated with the error log.")
 *                          .withCause("Message of the CAUSE exception associated with the exception logged as ERROR.")
 *             );
 *         }
 *     }
 * </pre>
 */
public class TestLoggerRule implements TestRule {

    private final Class<?> loggingClassUnderTest;
    private TestLogger logger;
    private Level level;

    private TestLoggerRule(final Class<?> loggingClassUnderTest, final Level level) {
        this.loggingClassUnderTest = loggingClassUnderTest;
        this.level = level;
    }

    /**
     * Initialises logger to verify logs of ALL levels.
     * If you only need to verify logs of and above specific level, use {@linkplain #targeting(Class, Level)}.
     */
    public static TestLoggerRule targeting(final Class<?> loggingClassUnderTest) {
        return new TestLoggerRule(loggingClassUnderTest, ALL);
    }

    /**
     * Initialises logger to verify logs of the given level and above.
     * If you need to verify logs of all levels, use {@linkplain #targeting(Class)}.
     */
    public static TestLoggerRule targeting(final Class<?> loggingClassUnderTest, final Level level) {
        return new TestLoggerRule(loggingClassUnderTest, level);
    }

    @Override public Statement apply(final Statement statement, final Description description) {
        return new Statement() {

            @Override public void evaluate() throws Throwable {

                try {
                    logger = TestLogger.initialiseFor(loggingClassUnderTest, level);

                    statement.evaluate();

                } finally {
                    Optional.ofNullable(logger).ifPresent(TestLogger::reset);
                }
            }
        };
    }

    /**
     * <p>
     * Asserts that the actual log contains entries corresponding to the expected ones
     * given by the parameter.
     * <p>
     * Use static factory methods to construct expectations:
     * <pre>
     *   logger.shouldReceive(
     *        info("Log message of level INFO"),
     *        debug("Log message of level DEBUG"),
     *        warn("Log message of level WARN"),
     *        trace("Log message of level TRACE"),
     *        error("Log message of level ERROR")
     *            .withException("Message of the exception associated with the error log.")
     *                .withCause("Message of the CAUSE exception associated with the exception logged as ERROR.")
     *   );
     * </pre>
     * @param logEntries Expected log entries built using static factory methods from {@linkplain TestLogger.LogAssertor}.
     *
     * @throws AssertionError When the logs did not contain expected entries.
     */
    public void shouldReceive(final TestLogger.LogAssertor... logEntries) {
        logger.shouldReceive(logEntries);
    }
}
