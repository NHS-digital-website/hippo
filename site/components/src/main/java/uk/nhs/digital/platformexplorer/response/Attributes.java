package uk.nhs.digital.platformexplorer.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder
    ({
        "Business Value",
        "Importance Current",
        "Importance Target",
        "Criticality Baseline",
        "Criticality Target"
    })
public class Attributes {

    @JsonProperty("Business Value")
    private Object businessValue;
    @JsonProperty("Importance Current")
    private Object importanceCurrent;
    @JsonProperty("Importance Target")
    private Object importanceTarget;
    @JsonProperty("Criticality Baseline")
    private Object criticalityBaseline;
    @JsonProperty("Criticality Target")
    private Object criticalityTarget;

    @JsonProperty("Business Value")
    public Object getBusinessValue() {
        return businessValue;
    }

    @JsonProperty("Business Value")
    public void setBusinessValue(Object businessValue) {
        this.businessValue = businessValue;
    }

    @JsonProperty("Importance Current")
    public Object getImportanceCurrent() {
        return importanceCurrent;
    }

    @JsonProperty("Importance Current")
    public void setImportanceCurrent(Object importanceCurrent) {
        this.importanceCurrent = importanceCurrent;
    }

    @JsonProperty("Importance Target")
    public Object getImportanceTarget() {
        return importanceTarget;
    }

    @JsonProperty("Importance Target")
    public void setImportanceTarget(Object importanceTarget) {
        this.importanceTarget = importanceTarget;
    }

    @JsonProperty("Criticality Baseline")
    public Object getCriticalityBaseline() {
        return criticalityBaseline;
    }

    @JsonProperty("Criticality Baseline")
    public void setCriticalityBaseline(Object criticalityBaseline) {
        this.criticalityBaseline = criticalityBaseline;
    }

    @JsonProperty("Criticality Target")
    public Object getCriticalityTarget() {
        return criticalityTarget;
    }

    @JsonProperty("Criticality Target")
    public void setCriticalityTarget(Object criticalityTarget) {
        this.criticalityTarget = criticalityTarget;
    }

}
