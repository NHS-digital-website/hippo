package uk.nhs.digital;

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
 *     import static uk.nhs.digital.TestLogger.LogAssertor.*;
 *
 *     import org.junit.Rule;
 *     import uk.nhs.digital.TestLoggerRule;
 *
 *     class MyTestClass {
 *
 *         private TestLogger logger;
 *
 *         &#64;Rule
 *         public TestLoggerRule logger = TestLoggerRule.targeting(ApiSpecificationPublicationService.class);
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

    private TestLoggerRule(final Class<?> loggingClassUnderTest) {

        this.loggingClassUnderTest = loggingClassUnderTest;
    }

    public static TestLoggerRule targeting(final Class<?> loggingClassUnderTest) {
        return new TestLoggerRule(loggingClassUnderTest);
    }

    @Override public Statement apply(final Statement statement, final Description description) {
        return new Statement() {

            @Override public void evaluate() {

                try {
                    logger = TestLogger.initialiseFor(loggingClassUnderTest);

                    statement.evaluate();

                } catch (final Throwable throwable) {
                    Optional.ofNullable(logger).ifPresent(TestLogger::reset);
                }
            }
        };
    }

    public void shouldReceive(final TestLogger.LogAssertor... logEntries) {
        logger.shouldReceive(logEntries);
    }
}
