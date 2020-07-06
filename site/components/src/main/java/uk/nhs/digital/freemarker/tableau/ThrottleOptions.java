package uk.nhs.digital.freemarker.tableau;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@JsonIgnoreProperties(ignoreUnknown = true)
@XmlAccessorType(XmlAccessType.FIELD)
public class ThrottleOptions {

    private float size;

    public ThrottleOptions() {
        /* For JAXB */
    }

    public ThrottleOptions(float size) {
        this.size = size;
    }

    public float getSize() {
        return size;
    }
}
