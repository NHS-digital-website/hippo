package uk.nhs.digital.arc.process;

import uk.nhs.digital.arc.util.FilePathData;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains meta data concerning the parsing and processing of content from a manifest
 */
public class ManifestProcessingSummary {
    private List<ProcessOutcome> outcomes = new ArrayList<>();

    private boolean inError = false;

    private String outputFileLocation = null;
    private String docbase = null;
    private boolean parsing;

    public void addIndividualProcessOutcome(ProcessOutcome outcome) {
        outcomes.add(outcome);
        if (outcome.isInError()) {
            inError = true;
        }
    }

    public boolean isInError() {
        return inError;
    }

    /**
     * Shortcut method to get all messages, including stack traces
     *
     * @return that strigified set of messages
     */
    public String getExtendedConcatenatedMessages() {
        return getMessagesAsString(true);
    }

    /**
     * Shortcut method to get all messages, specifically excluding stack traces
     *
     * @return that strigified set of messages
     */
    public String getConcatenatedMessages() {
        return getMessagesAsString(false);
    }

    /**
     * Builds a string with all the messages that we have accumulated during the process
     *
     * @param includeStackTraces will flag whetehr we want stack trace information to be included
     *
     * @return the stringfied version of the outcomes
     */
    private String getMessagesAsString(boolean includeStackTraces) {
        StringBuilder builder = new StringBuilder();

        if (outputFileLocation != null && docbaseWasFound()) {
            FilePathData filePathData = new FilePathData(docbase, outputFileLocation);
            builder.append(String.format("All messages for this activity have been written to the file '%s'\n\n", filePathData.getFilePath()));
        }

        for (ProcessOutcome processOutcome : outcomes) {
            builder.append(processOutcome.getMessageLine());
            if (includeStackTraces && processOutcome.hasStackTrace()) {
                builder.append(processOutcome.getStackTrace());
            }
        }

        return builder.toString();
    }

    public void setDocbase(String docbase) {
        this.docbase = docbase;
    }

    public String getDocbase() {
        return this.docbase;
    }

    public String getOutputFileLocation() {
        return outputFileLocation;
    }

    public void setOutputFileLocation(String outputFileLocation) {
        this.outputFileLocation = outputFileLocation;
    }

    public void setParsing(boolean parsing) {
        this.parsing = parsing;
    }

    public boolean isParsing() {
        return parsing;
    }

    /*
    * For convenience - setting the docbase indicates that we were able to access the manifest file in S3
     */
    public boolean docbaseWasFound() {
        return docbase != null;
    }

}
