package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.*;
import org.hippoecm.hst.content.beans.standard.*;
import org.onehippo.cms7.essentials.dashboard.annotations.*;
import uk.nhs.digital.externalstorage.beans.*;

@HippoEssentialsGenerated(internalName = "publicationsystem:extattachment")
@Node(jcrType = "publicationsystem:extattachment")
public class ExtAttachment extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "publicationsystem:displayName")
    public String getText() {
        String text = getProperty("publicationsystem:displayName");
        if (text == null || text.isEmpty()) {
            text = getResource().getFilename();
        }
        return text;
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:attachmentResource")
    public Resource getResource() {
        return getChildBeansByName("publicationsystem:attachmentResource", Resource.class).get(0);
    }
}
