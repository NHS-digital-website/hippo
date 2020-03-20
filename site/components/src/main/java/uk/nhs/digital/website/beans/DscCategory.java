package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:homepagesections")
@Node(jcrType = "website:homepagesections")
public class DscCategory extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:categorytitle")
    public String getCategoryTitle() {
        return getProperty("website:categorytitle");
    }

    @HippoEssentialsGenerated(internalName = "website:categorycontent")
    public HippoHtml getCategoryContent() {
        return getHippoHtml("website:categorycontent");
    }

    @HippoEssentialsGenerated(internalName = "website:categorylabel")
    public String getCategoryLabel() {
        return getProperty("website:categorylabel");
    }

    @HippoEssentialsGenerated(internalName = "website:link")
    public String getCategoryPageLink() {
        return getProperty("website:link");
    }

}
