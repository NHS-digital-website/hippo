package uk.nhs.digital.website.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.indices.StickySection;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@HippoEssentialsGenerated(internalName = "website:navigation")
@Node(jcrType = "website:navigation")
public class Navigation extends BaseCompound implements StickySection {

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:optionalHeading")
    public String getTitle() {
        return getSingleProperty("website:optionalHeading");
    }

    @Override
    @JsonIgnore
    public String getHeading() {
        return this.getTitle();
    }

    @Override
    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:headingType")
    public String getHeadingLevel() {
        return getSingleProperty("website:headingType");
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:sectionIntroduction")
    public HippoHtml getSectionIntroduction() {
        return getHippoHtml("website:sectionIntroduction");
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:columnAlignment")
    public String getColumnAlignment() {
        return getSingleProperty("website:columnAlignment");
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:imageType")
    public String getImageType() {
        return getSingleProperty("website:imageType");
    }

    @HippoEssentialsGenerated(internalName = "website:navigationTiles")
    public List<HippoBean> getNavigationTiles() {
        return getChildBeansByName("website:navigationTiles");
    }

    @JsonProperty
    public String getSectionType() {
        return "navigation";
    }
}
