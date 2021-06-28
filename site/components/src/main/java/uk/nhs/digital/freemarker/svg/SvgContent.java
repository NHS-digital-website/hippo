package uk.nhs.digital.freemarker.svg;

import java.util.Base64;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class SvgContent {
    private String data;

    public String getData() {
        data = new String(Base64.getDecoder().decode(this.data));
        return data;
    }
}
