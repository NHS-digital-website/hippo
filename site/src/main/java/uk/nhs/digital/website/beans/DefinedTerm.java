package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "definedterm")
@XmlAccessorType(XmlAccessType.FIELD)
@HippoEssentialsGenerated(internalName = "website:definedterm")
@Node(jcrType = "website:definedterm")
public class DefinedTerm extends HippoCompound {

    @XmlElement
    @HippoEssentialsGenerated(internalName = "website:term")
    public String getTerm() {
        return getProperty("website:term");
    }

    @XmlElement
    @HippoEssentialsGenerated(internalName = "website:colorrgb")
    public String getColorrgb() {
        return getProperty("website:colorrgb");
    }
}
