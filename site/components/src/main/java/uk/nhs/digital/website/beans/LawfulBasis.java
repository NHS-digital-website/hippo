package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:lawfulbasis")
@Node(jcrType = "website:lawfulbasis")
public class LawfulBasis extends HippoCompound {

    public String getSectionType() {
        return "lawfulbasis";
    }

    @HippoEssentialsGenerated(internalName = "website:description")
    public HippoHtml getDescription() {
        return getHippoHtml("website:description");
    }

    @HippoEssentialsGenerated(internalName = "website:lawfulbasisfield")
    public HippoHtml getLawfulbasisfield() {
        return getHippoHtml("website:lawfulbasisfield");
    }

    @HippoEssentialsGenerated(internalName = "website:suppressdata")
    public String getSuppressdata() {
        return getSingleProperty("website:suppressdata");
    }
}
