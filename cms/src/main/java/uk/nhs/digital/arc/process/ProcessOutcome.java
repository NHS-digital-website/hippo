package uk.nhs.digital.arc.process;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class ProcessOutcome {
    public static final boolean ERROR = true;

    private StringBuilder message = new StringBuilder();
    private boolean inError = false;
    private String stackTrace = null;

    public ProcessOutcome(String messageString) {
        this.addErrorMessageLine(messageString);
        this.inError = false;
    }

    public ProcessOutcome(String messageString, boolean isInError) {
        this.addErrorMessageLine(messageString);
        this.inError = isInError;
    }

    public void addMessageLine(String messageString) {
        message.append(messageString);
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public boolean hasStackTrace() {
        return stackTrace != null;
    }

    public void addErrorMessageLine(String messageString) {
        message.append(messageString);
        inError = true;
    }

    public void addStackTrace(Exception exception) {
        stackTrace = ExceptionUtils.getStackTrace(exception);
        inError = true;
    }

    public String getMessageLine() {
        return message.toString();
    }

    public boolean isInError() {
        return inError;
    }

    public void addIndentedMessageLine(String messageString) {
        addMessageLine("\t" + messageString);
    }

    public void addIndentedErrorMessageLine(String messageString) {
        addErrorMessageLine("\t" + messageString);
    }
}
