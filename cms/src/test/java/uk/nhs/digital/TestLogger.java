package uk.nhs.digital;

import static ch.qos.logback.classic.Level.*;
import static java.util.stream.Collectors.joining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.TestLogger.LogAssertor.assertLogs;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.Appender;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;

/**
 * <p>
 * Provides convenient DSL and assertion to aid in validating that desired statements have been logged.
 * <p>
 * Can be used directly or via {@linkplain TestLoggerRule} (recommended).
 * <p>
 * If using directly, remember to call {@linkplain #reset()} method after each test!
 * <p>
 * Example of direct use:
 * <pre>
 *     import static uk.nhs.digital.TestLogger.LogAssertor.*;
 *
 *     import uk.nhs.digital.TestLogger;
 *
 *     class MyTestClass {
 *
 *         private TestLogger logger;
 *
 *         &#64;Before
 *         public void setUp() {
 *             logger = TestLogger.initialiseFor(MyLoggingClassUnderTest.class);
 *         }
 *
 *         &#64;After
 *         public void tearDown() {
 *             logger.reset();
 *         }
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
*              );
 *         }
 *     }
 * </pre>
 */
public class TestLogger {

    private final Class<?> loggingClassUnderTest;

    @Mock
    private Appender<ILoggingEvent> appender;

    @Captor
    private ArgumentCaptor<ILoggingEvent> loggerArgCaptor;

    private TestLogger(final Class<?> loggingClassUnderTest) {
        this.loggingClassUnderTest = loggingClassUnderTest;

        initMocks(this);

        registerMockAppenderFor(loggingClassUnderTest);
    }

    public static TestLogger initialiseFor(final Class<?> loggingClassUnderTest) {
        return new TestLogger(loggingClassUnderTest);
    }

    public void reset() {
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(loggingClassUnderTest))
            .detachAppender(appender);
    }

    public void shouldReceive(final LogAssertor... expectedLogEntries) {
        assertLogs(appender, loggerArgCaptor, expectedLogEntries);
    }

    private void registerMockAppenderFor(final Class<?> loggingClassUnderTest) {
        deregisterMockAppenderFor(loggingClassUnderTest);
    }

    private void deregisterMockAppenderFor(final Class<?> loggingClassUnderTest) {
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(loggingClassUnderTest))
            .addAppender(appender);
    }

    public static class LogAssertor {
        private final Level level;

        private final String logMessage;
        private String exceptionMessage;
        private String causeMessage;

        private LogAssertor(final Level level, final String logMessage) {
            this.level = level;
            this.logMessage = logMessage;
        }

        public static LogAssertor info(final String message) {
            return new LogAssertor(INFO, message);
        }

        public static LogAssertor error(final String message) {
            return new LogAssertor(ERROR, message);
        }

        public static LogAssertor warn(final String message) {
            return new LogAssertor(WARN, message);
        }

        public static LogAssertor debug(final String message) {
            return new LogAssertor(DEBUG, message);
        }

        public static LogAssertor trace(final String message) {
            return new LogAssertor(TRACE, message);
        }

        public LogAssertor withException(final String message) {
            exceptionMessage = message;
            return this;
        }

        public LogAssertor withCause(final String message) {
            causeMessage = message;
            return this;
        }

        public Optional<String> exceptionMessage() {
            return Optional.ofNullable(exceptionMessage);
        }

        public Optional<String> causeMessage() {
            return Optional.ofNullable(causeMessage);
        }

        @SuppressWarnings("StringBufferReplaceableByString")
        static void assertLogs(final Appender<ILoggingEvent> appender,
                               final ArgumentCaptor<ILoggingEvent> loggerArgCaptor,
                               final LogAssertor... expectedLogEntries
        ) {
            then(appender).should(times(expectedLogEntries.length)).doAppend(loggerArgCaptor.capture());

            final String expectedLog = Arrays.stream(expectedLogEntries)
                .map(expected -> new StringBuilder()
                    .append(expected.level)
                    .append(": ")
                    .append(expected.logMessage)
                    .append(expected.exceptionMessage()
                        .map(errorMessage -> "\n  EXCEPTION: " + errorMessage).orElse(""))
                    .append(expected.causeMessage()
                        .map(causeMessage -> "\n    CAUSE: " + causeMessage).orElse(""))
                    .toString())
                .collect(joining("\n"));

            final String actualLog = loggerArgCaptor.getAllValues().stream()
                .map(actual -> new StringBuilder()
                    .append(actual.getLevel())
                    .append(": ")
                    .append(actual.getFormattedMessage())
                    .append(Optional.ofNullable(actual.getThrowableProxy())
                        .map(IThrowableProxy::getMessage).map(message -> "\n  EXCEPTION: " + message).orElse(""))
                    .append(Optional.ofNullable(actual.getThrowableProxy()).map(IThrowableProxy::getCause)
                        .map(IThrowableProxy::getMessage).map(message -> "\n    CAUSE: " + message).orElse(""))
                    .toString())
                .collect(joining("\n"));

            assertThat("Key events are logged.", actualLog, is(expectedLog));
        }
    }
}
