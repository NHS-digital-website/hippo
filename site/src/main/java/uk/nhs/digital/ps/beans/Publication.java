package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.pagination.Paginated;
import uk.nhs.digital.pagination.Pagination;
import uk.nhs.digital.ps.beans.structuredText.StructuredText;

import java.util.ArrayList;
import java.util.List;

@HippoEssentialsGenerated(internalName = "publicationsystem:publication")
@Node(jcrType = "publicationsystem:publication")
public class Publication extends PublicationBase implements Paginated {

    @HippoEssentialsGenerated(internalName = PublicationBase.PropertyKeys.SUMMARY)
    public StructuredText getSummary() {
        assertPropertyPermitted(PublicationBase.PropertyKeys.SUMMARY);

        return new StructuredText(getProperty(PublicationBase.PropertyKeys.SUMMARY, ""));
    }

    @HippoEssentialsGenerated(internalName = PublicationBase.PropertyKeys.KEY_FACTS)
    public StructuredText getKeyFacts() {
        assertPropertyPermitted(PublicationBase.PropertyKeys.KEY_FACTS);

        return new StructuredText(getProperty(PublicationBase.PropertyKeys.KEY_FACTS, ""));
    }

    @HippoEssentialsGenerated(internalName = PublicationBase.PropertyKeys.KEY_FACT_IMAGES)
    public List<ImageSection> getKeyFactImages() {
        return getChildBeansIfPermitted(PropertyKeys.KEY_FACT_IMAGES, ImageSection.class);
    }

    public List<PublicationPage> getPages() {
        assertPropertyPermitted(PublicationBase.PropertyKeys.PAGES);

        return getParentBean().getChildBeans(PublicationPage.class);
    }

    public List<IndexPage> getPageIndex() {
        List<IndexPage> pages = new ArrayList<>(getPages());

        if (!pages.isEmpty()) {
            // Add the Publication itself as the first link
            pages.add(0, new IndexPageImpl("Overview", this));
        }

        return pages;
    }

    @Override
    public Pagination paginate() {
        return new Pagination(null, getPageIndex().stream().skip(1).findFirst().orElse(null));
    }
}
