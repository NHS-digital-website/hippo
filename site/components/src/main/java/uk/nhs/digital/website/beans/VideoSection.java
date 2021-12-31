package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:videoSection")
@Node(jcrType = "website:videoSection")
public class VideoSection extends HippoCompound {

    public String getSectionType() {
        return "videoSection";
    }

    @HippoEssentialsGenerated(internalName = "type")
    public String getType() {
        return getSingleProperty("website:type");
    }
    
    @HippoEssentialsGenerated(internalName = "videoUrl")
    public String getVideoUrl() {
        return getSingleProperty("website:videoUrl");
    }
    
    @HippoEssentialsGenerated(internalName = "caption")
    public String getCaption() {
        return getSingleProperty("website:caption");
    }
    
    @HippoEssentialsGenerated(internalName = "text")
    public String getText() {
        return getSingleProperty("website:text");
    }
    
    @HippoEssentialsGenerated(internalName = "behaviour")
    public String getBehaviour() {
        return getSingleProperty("website:behaviour");
    }
    
    @HippoEssentialsGenerated(internalName = "loop")
    public String getLoop() {
        return getSingleProperty("website:loop");
    }
}