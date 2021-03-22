package uk.nhs.digital;

import static ch.qos.logback.classic.Level.*;
import static java.util.stream.Collectors.joining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.TestLogger.LogAssertionBuilder.assertLogs;

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

public class TestLogger {

    private final Class<?> loggingClassUnderTest;

    @Mock
    private Appender<ILoggingEvent> appender;

    @Captor
    private ArgumentCaptor<ILoggingEvent> loggerArgCaptor;

    private TestLogger(final Class<?> loggingClassUnderTest) {
        this.loggingClassUnderTest = loggingClassUnderTest;

        initMocks(this);

        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(loggingClassUnderTest))
            .addAppender(appender);
    }

    public static TestLogger initialiseFor(final Class<?> loggingClassUnderTest) {
        return new TestLogger(loggingClassUnderTest);
    }

    public void reset() {
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(loggingClassUnderTest))
            .detachAppender(appender);
    }

    public void shouldReceive(final LogAssertionBuilder... expectedLogEntries) {
        assertLogs(appender, loggerArgCaptor, expectedLogEntries);
    }

    public static class LogAssertionBuilder {
        private final Level level;

        private final String logMessage;
        private String exceptionMessage;
        private String causeMessage;

        private LogAssertionBuilder(final Level level, final String logMessage) {
            this.level = level;
            this.logMessage = logMessage;
        }

        public static LogAssertionBuilder info(final String message) {
            return new LogAssertionBuilder(INFO, message);
        }

        public static LogAssertionBuilder error(final String message) {
            return new LogAssertionBuilder(ERROR, message);
        }

        public static LogAssertionBuilder warn(final String message) {
            return new LogAssertionBuilder(WARN, message);
        }

        public static LogAssertionBuilder debug(final String message) {
            return new LogAssertionBuilder(DEBUG, message);
        }

        public static LogAssertionBuilder trace(final String message) {
            return new LogAssertionBuilder(TRACE, message);
        }

        public LogAssertionBuilder withException(final String message) {
            exceptionMessage = message;
            return this;
        }

        public LogAssertionBuilder withCause(final String message) {
            causeMessage = message;
            return this;
        }

        public Optional<String> error() {
            return Optional.ofNullable(exceptionMessage);
        }

        public Optional<String> causeMessage() {
            return Optional.ofNullable(causeMessage);
        }

        @SuppressWarnings("StringBufferReplaceableByString")
        public static void assertLogs(final Appender<ILoggingEvent> appender,
                               final ArgumentCaptor<ILoggingEvent> loggerArgCaptor,
                               final LogAssertionBuilder... expectedLogEntries
        ) {
            then(appender).should(times(expectedLogEntries.length)).doAppend(loggerArgCaptor.capture());

            final String expectedLog = Arrays.stream(expectedLogEntries)
                .map(expected -> new StringBuilder()
                    .append(expected.level)
                    .append(": ")
                    .append(expected.logMessage)
                    .append(expected.error()
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
