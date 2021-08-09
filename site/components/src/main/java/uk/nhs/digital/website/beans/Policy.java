package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.pagination.Paginated;
import uk.nhs.digital.pagination.Pagination;
import uk.nhs.digital.ps.beans.IndexPageImpl;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:corporatepolicy")
@Node(jcrType = "website:corporatepolicy")
public class Policy extends CommonFieldsBean implements Paginated {

    public String getSectionType() {
        return "corporatepolicy";
    }

    @HippoEssentialsGenerated(internalName = "website:pubstylesection")
    public PublicationStylePolicy getPublicationStyle() {
        return getBean("website:pubstylesection", PublicationStylePolicy.class);
    }

    @HippoEssentialsGenerated(internalName = "website:InformationType")
    public String[] getInformationType() {
        return getMultipleProperty("website:InformationType");
    }

    @HippoEssentialsGenerated(internalName = "website:childPage")
    public List<HippoBean> getLinks() {
        return getLinkedBeans("website:childPage", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:bodySection")
    public List<HippoBean> getSections() {
        return getChildBeansByName("website:bodySection");
    }

    @HippoEssentialsGenerated(internalName = "website:highlightsection")
    public HighlightSection getHighlightSection() {
        return getBean("website:highlightsection", HighlightSection.class);
    }

    @Override
    public Pagination paginate() {
        return new Pagination(null, getLinks().stream().findFirst().map(i -> new IndexPageImpl(i.getDisplayName(), i)).orElse(null));
    }
}
