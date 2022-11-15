package uk.nhs.digital.website.beans;

import static org.apache.commons.collections.IteratorUtils.toList;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.constraint;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.*;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.intranet.enums.SearchResultType;
import uk.nhs.digital.intranet.model.IntranetSearchResult;

import java.util.List;


@HippoEssentialsGenerated(internalName = "website:blog")
@Node(jcrType = "website:blog")
public class Blog extends CaseStudyAndBlogBase implements IntranetSearchResult {

    // used to differentiate between different types of content blocks
    public String getArticleType() {
        return "blog";
    }

    public List<Blog> getLatestBlogs() throws QueryException {

        HippoBean folder = getCanonicalBean().getParentBean();

        HippoBeanIterator hippoBeans = HstQueryBuilder.create(folder)
            .ofTypes(Blog.class)
            .where(constraint("jcr:uuid").notEqualTo(this.getIdentifier()))
            .orderByDescending("website:dateofpublication")
            .limit(3)
            .build()
            .execute()
            .getHippoBeans();

        return toList(hippoBeans);
    }

    @Override
    public String getSearchResultTitle() {
        return getTitle();
    }

    @Override
    public String getSearchResultType() {
        return SearchResultType.BLOG.getValue();
    }

    @HippoEssentialsGenerated(internalName = "website:headertype")
    public String getHeadertype() {
        return getSingleProperty("website:headertype");
    }

    public String getDocType() {
        return "Blog";
    }

    @HippoEssentialsGenerated(internalName = "website:socialMediaBar")
    public SocialMediaBar getSocialMediaBar() {
        return getBean("website:socialMediaBar", SocialMediaBar.class);
    }

    @HippoEssentialsGenerated(internalName = "website:primaryCtaButton")
    public CtaButtonSimplified getPrimaryCtaButton() {
        return getBean("website:primaryCtaButton", CtaButtonSimplified.class);
    }

    @HippoEssentialsGenerated(internalName = "website:secondaryCtaButton")
    public CtaButtonSimplified getSecondaryCtaButton() {
        return getBean("website:secondaryCtaButton", CtaButtonSimplified.class);
    }
}
