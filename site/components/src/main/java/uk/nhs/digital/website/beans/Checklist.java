package uk.nhs.digital.website.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@HippoEssentialsGenerated(internalName = "website:checklist")
@Node(jcrType = "website:checklist")
public class Checklist extends HippoCompound {

    @JsonProperty
    public String getSectionType() {
        return "checklist";
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:heading")
    public String getHeading() {
        return getSingleProperty("website:heading");
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:icon")
    public String getIcon() {
        return getSingleProperty("website:icon");
    }


    @JsonIgnore
    @HippoEssentialsGenerated(internalName = "website:customicon")
    public CorporateWebsiteImageset getCustomicon() {
        return getLinkedBean("website:customicon", CorporateWebsiteImageset.class);
    }

    @JsonProperty("listentries")
    public List<String> getListentriesJson() {
        List<HippoHtml> entries = getChildBeansByName("website:listentries", HippoHtml.class);
        List<String> entriesStrings = new ArrayList<>();
        for (HippoHtml entry : entries) {
            if (entry != null) {
                entriesStrings.add(entry.getContent());
            } else {
                entriesStrings.add((String)null);
            }
        }
        return entriesStrings;
    }

    @HippoEssentialsGenerated(internalName = "website:listentries")
    public List<HippoHtml> getListentries() {
        return getChildBeansByName("website:listentries", HippoHtml.class);
    }
}
