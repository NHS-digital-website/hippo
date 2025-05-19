package uk.nhs.digital.website.beans;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "cyberAlert")
@XmlAccessorType(XmlAccessType.FIELD)
public class ThreatIds {

    public ThreatIds() {
        super();
    }

    @XmlElementWrapper(name = "threatids")
    @XmlElement(name = "threatidandDate")
    public List<ThreatIdDate> threatids;


    public void setThreatids(List<ThreatIdDate> threatids) {
        this.threatids = threatids;
    }


    public List<ThreatIdDate> getThreatids() {
        return this.threatids;
    }

}
