package uk.nhs.digital.test;

import static ch.qos.logback.classic.Level.*;
import static org.hamcrest.Matchers.is;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.AppenderBase;
import org.hamcrest.MatcherAssert;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * Provides convenient DSL and assertion to aid in validating that desired statements have been logged.
 * <p>
 * Can be used directly or via {@linkplain TestLoggerRule} (<b>recommended</b>).
 * <p>
 * <b>If using directly, remember to call {@linkplain #reset()} method after each test, else you're
 * risking breaking tests unrelated to your current one, in a way that is hard to debug!</b>
 * The aforementioned rule takes care of that for you.
 * <p>
 * Example of direct use:
 * <pre>
 *     import static uk.nhs.digital.test.TestLogger.LogAssertor.*;
 *
 *     import uk.nhs.digital.test.TestLogger;
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
 *                 info("Log message of level INFO"),
 *                 debug("Log message of level DEBUG"),
 *                 warn("Log message of level WARN"),
 *                 trace("Log message of level TRACE"),
 *                 error("Log message of level ERROR")
 *                     .withException("Message of the exception associated with the error log.")
 *                         .withCause("Message of the CAUSE exception associated with the exception logged as ERROR.")
 *             );
 *         }
 *     }
 * </pre>
 */
public class TestLogger {

    private final Class<?> loggingClassUnderTest;
    private final Level level;

    private MockAppender mockAppender;

    private TestLogger(final Class<?> loggingClassUnderTest, final Level level) {
        this.loggingClassUnderTest = loggingClassUnderTest;
        this.level = level;

        mockAppender = new MockAppender();

        registerMockAppenderFor(loggingClassUnderTest);
    }

    /**
     * Initialises logger to verify logs of the given level and above.
     * If you need to verify logs of all levels, use {@linkplain #initialiseFor(Class)}.
     */
    public static TestLogger initialiseFor(final Class<?> loggingClassUnderTest) {
        return initialiseFor(loggingClassUnderTest, ALL);
    }

    /**
     * Initialises logger to verify logs of ALL levels.
     * If you only need to verify logs of and above specific level, use {@linkplain #initialiseFor(Class, Level)}.
     */
    public static TestLogger initialiseFor(final Class<?> loggingClassUnderTest, final Level level) {
        return new TestLogger(loggingClassUnderTest, level);
    }

    private void registerMockAppenderFor(final Class<?> loggingClassUnderTest) {
        loggerFor(loggingClassUnderTest).addAppender(mockAppender);

        mockAppender.start();
    }

    public void reset() {
        loggerFor(loggingClassUnderTest).detachAppender(mockAppender);

        mockAppender.stop();
    }

    public void shouldReceive(final LogAssertor... expectedLogEntries) {
        LogAssertor.assertLogs(mockAppender, level, expectedLogEntries);
    }

    private Logger loggerFor(final Class<?> loggingClassUnderTest) {
        return (Logger) LoggerFactory.getLogger(loggingClassUnderTest);
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
        static void assertLogs(final MockAppender appender, final Level level, final LogAssertor... expectedLogEntries) {

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
                .collect(Collectors.joining("\n"));

            final String actualLog = appender.getAllValues().stream()
                .filter(iLoggingEvent -> iLoggingEvent.getLevel().isGreaterOrEqual(level))
                .map(actual -> new StringBuilder()
                    .append(actual.getLevel())
                    .append(": ")
                    .append(actual.getFormattedMessage())
                    .append(Optional.ofNullable(actual.getThrowableProxy())
                        .map(IThrowableProxy::getMessage).map(message -> "\n  EXCEPTION: " + message).orElse(""))
                    .append(Optional.ofNullable(actual.getThrowableProxy()).map(IThrowableProxy::getCause)
                        .map(IThrowableProxy::getMessage).map(message -> "\n    CAUSE: " + message).orElse(""))
                    .toString())
                .collect(Collectors.joining("\n"));

            MatcherAssert.assertThat("Key events are logged.", actualLog, is(expectedLog));
        }
    }

    static class MockAppender extends AppenderBase<ILoggingEvent> {

        private List<ILoggingEvent> loggingEvents = new ArrayList<>();

        @Override protected void append(final ILoggingEvent eventObject) {
            loggingEvents.add(eventObject);
        }

        Collection<ILoggingEvent> getAllValues() {
            return Collections.unmodifiableList(loggingEvents);
        }
    }
}
