package uk.nhs.digital.ps.migrator.report;

import static java.util.Collections.emptyList;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

public class IncidentLogEntry {
    private final String pCode;
    private final IncidentType incidentType;
    private final List<String> supportingData;

    IncidentLogEntry(final String pCode, final IncidentType incidentType, final List<String> supportingData) {
        this.pCode = pCode;
        this.incidentType = incidentType;
        this.supportingData = supportingData;
    }

    String getPCode() {
        return pCode;
    }

    IncidentType getIncidentType() {
        return incidentType;
    }

    List<String> getSupportingData() {
        return supportingData == null ? emptyList() : supportingData;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
