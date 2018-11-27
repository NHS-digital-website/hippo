package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


@HippoEssentialsGenerated(internalName = "website:threataffect")
@Node(jcrType = "website:threataffect")
public class ThreatAffect extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:platformtext")
    public HippoHtml getPlatformText() {
        return getHippoHtml("website:platformtext");
    }

    @HippoEssentialsGenerated(internalName = "website:platformaffected")
    public HippoBean getPlatformAffected() {
        return getLinkedBean("website:platformaffected", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:versionsaffected")
    public String[] getVersionsAffected() {
        return getProperty("website:versionsaffected");
    }

}
