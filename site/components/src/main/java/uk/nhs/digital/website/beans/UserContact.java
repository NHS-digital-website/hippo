package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:userContact")
@Node(jcrType = "website:userContact")
public class UserContact extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:userContactPhone")
    public String getUserContactPhone() {
        return getSingleProperty("website:userContactPhone");
    }

    @HippoEssentialsGenerated(internalName = "website:userContactemail")
    public String getUserContactemail() {
        return getSingleProperty("website:userContactemail");
    }

    @HippoEssentialsGenerated(internalName = "website:userContactname")
    public String getUserContactname() {
        return getSingleProperty("website:userContactname");
    }

    @HippoEssentialsGenerated(internalName = "website:userContactprimarycontact")
    public Boolean getUserContactprimarycontact() {
        return getSingleProperty("website:userContactprimarycontact");
    }

    @HippoEssentialsGenerated(internalName = "website:userContactnotes")
    public HippoHtml getUserContactnotes() {
        return getHippoHtml("website:userContactnotes");
    }
}
