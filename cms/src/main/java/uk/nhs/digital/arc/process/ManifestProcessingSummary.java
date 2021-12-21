package uk.nhs.digital.arc.process;

import java.util.ArrayList;
import java.util.List;

public class ManifestProcessingSummary {
    private List<ProcessOutcome> outcomes = new ArrayList<>();

    private boolean inError = false;

    public void addIndividualProcessOutcome(ProcessOutcome outcome) {
        outcomes.add(outcome);
        if (outcome.isInError()) {
            inError = true;
        }
    }

    public boolean isInError() {
        return inError;
    }

    public String getConcatenatedMessages() {
        StringBuilder builder = new StringBuilder();
        for (ProcessOutcome pm : outcomes) {
            builder.append(pm.getMessageLine());
        }

        return builder.toString();
    }
}
