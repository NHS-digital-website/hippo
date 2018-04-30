package uk.nhs.digital.nil.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.ps.beans.BaseDocument;

@HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:nilanding")
@Node(jcrType = "nationalindicatorlibrary:nilanding")
public class NiLanding extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:title")
    public String getTitle() {
        return getProperty("nationalindicatorlibrary:title");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:mainContent")
    public HippoHtml getMainContent() {
        return getHippoHtml("nationalindicatorlibrary:mainContent");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:adviceTitle")
    public String getAdviceTitle() {
        return getProperty("nationalindicatorlibrary:adviceTitle");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:adviceContent")
    public HippoHtml getAdviceContent() {
        return getHippoHtml("nationalindicatorlibrary:adviceContent");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:adviceForm")
    public HippoCompound getAdviceForm() {
        return getAttachment("nationalindicatorlibrary:adviceForm");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:addTitle")
    public String getAddTitle() {
        return getProperty("nationalindicatorlibrary:addTitle");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:addContent")
    public HippoHtml getAddContent() {
        return getHippoHtml("nationalindicatorlibrary:addContent");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:addForm")
    public HippoCompound getAddForm() {
        return getAttachment("nationalindicatorlibrary:addForm");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:applyTitle")
    public String getApplyTitle() {
        return getProperty("nationalindicatorlibrary:applyTitle");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:applyContent")
    public HippoHtml getApplyContent() {
        return getHippoHtml("nationalindicatorlibrary:applyContent");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:applyForm")
    public HippoCompound getApplyForm() {
        return getAttachment("nationalindicatorlibrary:applyForm");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:applyGuidanceForm")
    public HippoCompound getApplyGuidanceForm() {
        return getAttachment("nationalindicatorlibrary:applyGuidanceForm");
    }
}
