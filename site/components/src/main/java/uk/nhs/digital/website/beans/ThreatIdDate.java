package uk.nhs.digital.website.beans;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Calendar;
import java.util.List;

@XmlRootElement(name = "threatidandDate")
@XmlAccessorType(XmlAccessType.FIELD)
public class ThreatIdDate {

    public ThreatIdDate() {
        super();
    }

    @XmlElement(name = "threatid")
    public String threatid;

    @XmlElementWrapper(name = "responsedates")
    @XmlElement(name = "responsedatetime")
    public List<Calendar> responsedates;

    public void setThreatid(String threatid) {
        this.threatid = threatid;
    }

    public String getThreatid() {
        return this.threatid;
    }

    public void setResponsedates(List<Calendar> dates) {
        this.responsedates = dates;
    }

    public List<Calendar> getResponsedates() {
        return this.responsedates;
    }


}
