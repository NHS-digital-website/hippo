package uk.nhs.digital.arc.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ArcSeriesArchiveCommonDoc extends ArcDoctype {
    @JsonProperty("information_type")
    private List<String> informationType = null;
    @JsonProperty("geographic_coverage")
    private List<String> geographicCoverage = null;
    @JsonProperty("granularity")
    private List<String> granularity = null;
    @JsonProperty("administrative_sources")
    private String administrativeSources;

    @JsonProperty("geographic_coverage")
    public List<String> getGeographicCoverage() {
        return geographicCoverage;
    }

    @JsonProperty("geographic_coverage")
    public void setGeographicCoverage(List<String> geographicCoverage) {
        this.geographicCoverage = geographicCoverage;
    }

    @JsonProperty("granularity")
    public List<String> getGranularity() {
        return granularity;
    }

    @JsonProperty("granularity")
    public void setGranularity(List<String> granularity) {
        this.granularity = granularity;
    }

    @JsonProperty("administrative_sources")
    public String getAdministrativeSources() {
        return administrativeSources;
    }

    @JsonProperty("administrative_sources")
    public void setAdministrativeSources(String administrativeSources) {
        this.administrativeSources = administrativeSources;
    }

    @JsonProperty("information_type")
    public List<String> getInformationType() {
        return informationType;
    }

    @JsonProperty("information_type")
    public void setInformationType(List<String> informationType) {
        this.informationType = informationType;
    }

}
