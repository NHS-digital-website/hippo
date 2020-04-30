package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.common.components.CompoundTitleComparator;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:glossarylist")
@Node(jcrType = "website:glossarylist")
public class GlossaryList extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:indexpage")
    public Boolean getIndexPage() {
        return getSingleProperty("website:indexpage");
    }

    @HippoEssentialsGenerated(internalName = "website:glossaryitems")
    public List<GlossaryItem> getGlossaryItems() {

        List<GlossaryItem> hippoBeans =  getChildBeansByName("website:glossaryitems");
        hippoBeans.sort(CompoundTitleComparator.COMPARATOR);
        return hippoBeans;
    }

}
