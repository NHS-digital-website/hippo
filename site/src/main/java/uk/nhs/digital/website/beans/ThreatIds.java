package uk.nhs.digital.website.beans;

//import uk.nhs.digital.website.beans.ThreatIdDate;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
//import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
