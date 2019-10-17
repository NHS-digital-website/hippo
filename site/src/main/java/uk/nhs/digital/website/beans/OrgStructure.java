package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:orgstructure")
@Node(jcrType = "website:orgstructure")
public class OrgStructure extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:introduction")
    public HippoHtml getIntroduction() {
        return getHippoHtml("website:introduction");
    }

    @HippoEssentialsGenerated(internalName = "website:navigationembed")
    public Boolean getNavigationembed() {
        return getProperty("website:navigationembed");
    }

    @HippoEssentialsGenerated(internalName = "website:directorates")
    public List<HippoBean> getDirectorates() {
        return getChildBeansByName("website:directorates");
    }

}
