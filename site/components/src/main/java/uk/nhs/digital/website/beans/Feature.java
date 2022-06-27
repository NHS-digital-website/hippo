package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.intranet.enums.SearchResultType;
import uk.nhs.digital.intranet.model.IntranetSearchResult;

@HippoEssentialsGenerated(internalName = "website:feature")
@Node(jcrType = "website:feature")
public class Feature extends CaseStudyAndBlogBase implements IntranetSearchResult {

    // used to differentiate between different types of content blocks
    public String getArticleType() {
        return "feature";
    }

    @Override
    public String getSearchResultTitle() {
        return getTitle();
    }

    @Override
    public String getSearchResultType() {
        return SearchResultType.NEWS.getValue();
    }

    @HippoEssentialsGenerated(internalName = "website:headertype")
    public String getHeadertype() {
        return getSingleProperty("website:headertype");
    }

    public String getDocType() {
        return "Feature";
    }

    @HippoEssentialsGenerated(internalName = "website:thumbnailimage")
    public CorporateWebsiteImageset getThumbnailImage() {
        return getLinkedBean("website:thumbnailimage", CorporateWebsiteImageset.class);
    }

    @HippoEssentialsGenerated(internalName = "website:socialMediaBar")
    public SocialMediaBar getSocialMediaBar() {
        return getBean("website:socialMediaBar", SocialMediaBar.class);
    }
}

