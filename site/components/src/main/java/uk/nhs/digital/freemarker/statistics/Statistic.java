package uk.nhs.digital.freemarker.statistics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@JsonIgnoreProperties(ignoreUnknown = true)
@XmlAccessorType(XmlAccessType.FIELD)
public class Statistic {

    private String number;
    private String prefix;
    private String suffix;
    private String trend;
    private String headline;
    private String information;

    public Statistic() {
        /* For JAXB */
    }

    public Statistic(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getTrend() {
        return trend;
    }

    public String getHeadline() {
        return headline;
    }

    public String getInformation() {
        return information;
    }
}
