package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.website.utils.VideoPlayer;

import java.util.List;

@Node(jcrType = "website:video")
public class Video extends CommonFieldsBean {
    @HippoEssentialsGenerated(internalName = "website:introduction", allowModifications = false)
    public HippoHtml getIntroduction() {
        return getHippoHtml("website:introduction");
    }

    @HippoEssentialsGenerated(internalName = "website:externallinkbase")
    public String getVideoUri() {
        return VideoPlayer.getVideoUrl(getSingleProperty("website:externallinkbase"));
    }

    @HippoEssentialsGenerated(internalName = "website:relateddocuments")
    public List<HippoBean> getRelatedDocuments() {
        return getLinkedBeans("website:relateddocuments", HippoBean.class);
    }
}
