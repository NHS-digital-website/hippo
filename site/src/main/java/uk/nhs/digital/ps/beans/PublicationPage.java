package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "publicationsystem:publication")
@Node(jcrType = "publicationsystem:publicationPage")
public class PublicationPage extends BaseDocument implements IndexPage {

    @HippoEssentialsGenerated(internalName = "publicationsystem:Title")
    public String getTitle() {
        return getProperty("publicationsystem:Title");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:bodySections")
    public List<HippoBean> getBodySections() {
        return getChildBeansIfPermitted("publicationsystem:bodySections", null);
    }

    public Publication getPublication() {
        return HippoBeanHelper.getParentPublication(this);
    }

    public String getSeosummary() {
        return getPublication().getSeosummary();
    }

    @Override
    public HippoBean getLinkedBean() {
        return this;
    }
}
