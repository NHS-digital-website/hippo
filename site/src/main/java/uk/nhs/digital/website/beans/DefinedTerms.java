package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "definedterms")
@XmlAccessorType(XmlAccessType.NONE)
@HippoEssentialsGenerated(internalName = "website:definedterms")
@Node(jcrType = "website:definedterms")
public class DefinedTerms extends CommonFieldsBean {

    @XmlAnyElement
    @HippoEssentialsGenerated(internalName = "website:terms")
    public List<HippoBean> getTerms() {
        return getChildBeansByName("website:terms");
    }

}
