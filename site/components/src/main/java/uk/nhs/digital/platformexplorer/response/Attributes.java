package uk.nhs.digital.platformexplorer.response;

import com.fasterxml.jackson.annotation.*;

import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder
    ({
        "Business Value",
        "Importance Current",
        "Importance Target",
        "Criticality Baseline",
        "Criticality Target"
    })
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attributes {

    @JsonIgnore
    @JsonProperty("Business Value")
    private String businessValue;

    @JsonIgnore
    @JsonProperty("Importance Current")
    private String importanceCurrent;

    @JsonIgnore
    @JsonProperty("Importance Target")
    private String importanceTarget;

    @JsonIgnore
    @JsonProperty("Criticality Baseline")
    private String criticalityBaseline;

    @JsonIgnore
    @JsonProperty("Criticality Target")
    private String criticalityTarget;

    @JsonIgnore
    @JsonProperty("Business Value")
    public Optional<String> getBusinessValue() {
        return Optional.ofNullable(businessValue);
    }

    @JsonProperty("Business Value")
    public void setBusinessValue(String businessValue) {
        this.businessValue = businessValue;
    }

    @JsonIgnore
    @JsonProperty("Importance Current")
    public Optional<String> getImportanceCurrent() {
        return Optional.ofNullable(importanceCurrent);
    }

    @JsonProperty("Importance Current")
    public void setImportanceCurrent(String importanceCurrent) {
        this.importanceCurrent = importanceCurrent;
    }

    @JsonIgnore
    @JsonProperty("Importance Target")
    public Optional<String> getImportanceTarget() {
        return Optional.ofNullable(importanceTarget);
    }

    @JsonProperty("Importance Target")
    public void setImportanceTarget(String importanceTarget) {
        this.importanceTarget = importanceTarget;
    }

    @JsonIgnore
    @JsonProperty("Criticality Baseline")
    public Optional<String> getCriticalityBaseline() {
        return Optional.ofNullable(criticalityBaseline);
    }

    @JsonProperty("Criticality Baseline")
    public void setCriticalityBaseline(String criticalityBaseline) {
        this.criticalityBaseline = criticalityBaseline;
    }

    @JsonIgnore
    @JsonProperty("Criticality Target")
    public Optional<String> getCriticalityTarget() {
        return Optional.ofNullable(criticalityTarget);
    }

    @JsonProperty("Criticality Target")
    public void setCriticalityTarget(String criticalityTarget) {
        this.criticalityTarget = criticalityTarget;
    }

}
