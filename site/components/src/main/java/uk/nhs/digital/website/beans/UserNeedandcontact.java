package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:userNeedandcontact")
@Node(jcrType = "website:userNeedandcontact")
public class UserNeedandcontact extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:portfolioCode")
    public String getPortfolioCode() {
        return getSingleProperty("website:portfolioCode");
    }

    @HippoEssentialsGenerated(internalName = "website:responsibleTeam")
    public String getResponsibleTeam() {
        return getSingleProperty("website:responsibleTeam");
    }

    @HippoEssentialsGenerated(internalName = "website:userNeedismet")
    public String[] getUserNeedismet() {
        return getMultipleProperty("website:userNeedismet");
    }

    @HippoEssentialsGenerated(internalName = "website:briefDescription")
    public HippoHtml getBriefDescription() {
        return getHippoHtml("website:briefDescription");
    }
}
