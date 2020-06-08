package uk.nhs.digital.website.beans;

import static org.apache.commons.collections.IteratorUtils.toList;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.constraint;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.*;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:bloghub")
@Node(jcrType = "website:bloghub")
public class BlogHub extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:subtitle")
    public String getSubtitle() {
        return getSingleProperty("website:subtitle");
    }

    @HippoEssentialsGenerated(internalName = "website:publisher")
    public HippoBean getPublisher() {
        return getLinkedBean("website:publisher", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:pageicon")
    public CorporateWebsiteImageset getPageIcon()  {
        return getLinkedBean("website:pageicon", CorporateWebsiteImageset.class);
    }

    @HippoEssentialsGenerated(internalName = "website:blogheadingimage")
    public CorporateWebsiteImageset getBlogheadingimage()  {
        return getLinkedBean("website:blogheadingimage", CorporateWebsiteImageset.class);
    }

    @HippoEssentialsGenerated(internalName = "website:issn")
    public String getIssn() {
        return getSingleProperty("website:issn");
    }

    @HippoEssentialsGenerated(internalName = "website:leadimage")
    public CorporateWebsiteImageset getLeadImage()  {
        return getLinkedBean("website:leadimage", CorporateWebsiteImageset.class);
    }

    @HippoEssentialsGenerated(internalName = "website:subject")
    public HippoBean getSubject() {
        return getLinkedBean("website:subject", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "common:SearchableTags")
    public String[] getTaxonomyTags() {
        return getMultipleProperty("common:SearchableTags");
    }

    @HippoEssentialsGenerated(internalName = "website:author")
    public HippoBean getAuthor() {
        return getLinkedBean("website:author", HippoBean.class);
    }

    public List<Blog> getLatestBlogs() throws QueryException {

        HippoBean folder = getCanonicalBean().getParentBean();

        HippoBeanIterator hippoBeans = HstQueryBuilder.create(folder)
            .ofTypes(Blog.class)
            .where(constraint("jcr:uuid").notEqualTo(this.getIdentifier()))
            .orderByDescending("website:dateofpublication")
            .build()
            .execute()
            .getHippoBeans();

        return toList(hippoBeans);

    }

}
