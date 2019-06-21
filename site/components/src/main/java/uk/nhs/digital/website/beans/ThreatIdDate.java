package uk.nhs.digital.website.beans;

import java.util.Calendar;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
//import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
