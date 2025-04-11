package uk.nhs.digital.freemarker.svg;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import uk.nhs.digital.svg.SvgProvider;

import java.util.Base64;

/**
 * @deprecated
 * For future SVG usage, this class should not be used. Please upload svg images on the repository.
 * <p> Use {@link SvgProvider#getSvgXmlFromBean(org.hippoecm.hst.content.beans.standard.HippoBean)} instead.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Deprecated
public class SvgContent {
    private String data;

    public String getData() {
        data = new String(Base64.getDecoder().decode(this.data));
        return data;
    }
}
