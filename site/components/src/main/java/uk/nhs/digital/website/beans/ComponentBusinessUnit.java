package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:componentbusinessunit")
@Node(jcrType = "website:componentbusinessunit")
public class ComponentBusinessUnit extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:businessunit")
    public HippoBean getBusinessunit() {
        return getLinkedBean("website:businessunit", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:responsibleperson")
    public HippoBean getResponsibleperson() {
        return getLinkedBean("website:responsibleperson", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:interimappointment")
    public Boolean getInterimappointment() {
        return getProperty("website:interimappointment");
    }

}
