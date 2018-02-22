package uk.nhs.digital.nil.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:contactAuthor")
@Node(jcrType = "nationalindicatorlibrary:contactAuthor")
public class ContactAuthor extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:contactAuthorName")
    public String getContactAuthorName() {
        return getProperty("nationalindicatorlibrary:contactAuthorName");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:contactAuthorEmail")
    public String getContactAuthorEmail() {
        return getProperty("nationalindicatorlibrary:contactAuthorEmail");
    }
}
