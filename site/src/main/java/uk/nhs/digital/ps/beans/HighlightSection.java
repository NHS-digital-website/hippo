package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.Calendar;
import java.util.List;

@HippoEssentialsGenerated(internalName = "publicationsystem:highlightSection")
@Node(jcrType = "publicationsystem:highlightSection")
public class HighlightSection extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "publicationsystem:publishedDate")
    public Calendar getPublishedDate() {
        return getProperty("publicationsystem:publishedDate");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:publicationTiles")
    public List<HighlightTile> getHighlightTiles() {
        return getChildBeansByName("publicationsystem:publicationTiles",
            HighlightTile.class);
    }

}
