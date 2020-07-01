package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:financial")
@Node(jcrType = "website:financial")
public class Financial extends Publishedworkchapter {

    @HippoEssentialsGenerated(internalName = "website:introductionText")
    public HippoHtml getIntroductionText() {
        return getHippoHtml("website:introductionText");
    }

    @HippoEssentialsGenerated(internalName = "website:table")
    public List<HippoHtml> getTable() {
        return getChildBeansByName("website:table", HippoHtml.class);
    }
}
