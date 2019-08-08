package uk.nhs.digital.website.beans;

import static org.apache.commons.collections.IteratorUtils.toList;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.constraint;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.*;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.Calendar;
import java.util.List;


@HippoEssentialsGenerated(internalName = "website:blog")
@Node(jcrType = "website:blog")
public class Blog extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:dateofpublication")
    public Calendar getDateOfPublication() {
        return getProperty("website:dateofpublication");
    }

    @HippoEssentialsGenerated(internalName = "website:authors")
    public List<HippoBean> getAuthors() {
        return getLinkedBeans("website:authors", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:authorname")
    public String getAuthorName() {
        return getProperty("website:authorname");
    }

    @HippoEssentialsGenerated(internalName = "website:authorrole")
    public String getAuthorRole() {
        return getProperty("website:authorrole");
    }

    @HippoEssentialsGenerated(internalName = "website:authorjobtitle")
    public String getAuthorJobTitle() {
        return getProperty("website:authorjobtitle");
    }

    @HippoEssentialsGenerated(internalName = "website:authordescription")
    public HippoHtml getAuthorDescription() {
        return getHippoHtml("website:authordescription");
    }

    @HippoEssentialsGenerated(internalName = "website:authororganisation")
    public String getAuthorOrganisation() {
        return getProperty("website:authororganisation");
    }

    @HippoEssentialsGenerated(internalName = "website:leadimage")
    public HippoGalleryImageSet getLeadImage()  {
        return getLinkedBean("website:leadimage", HippoGalleryImageSet.class);
    }

    @HippoEssentialsGenerated(internalName = "website:leadimagealttext")
    public String getLeadImageAltText() {
        return getProperty("website:leadimagealttext");
    }

    @HippoEssentialsGenerated(internalName = "website:leadimagecaption")
    public HippoHtml getLeadImageCaption() {
        return getHippoHtml("website:leadimagecaption");
    }

    @HippoEssentialsGenerated(internalName = "website:leadparagraph")
    public HippoHtml getLeadParagraph() {
        return getHippoHtml("website:leadparagraph");
    }

    @HippoEssentialsGenerated(internalName = "website:sections")
    public List<HippoBean> getSections() {
        return getChildBeansByName("website:sections");
    }

    @HippoEssentialsGenerated(internalName = "website:backstory")
    public HippoHtml getBackstory() {
        return getHippoHtml("website:backstory");
    }

    @HippoEssentialsGenerated(internalName = "website:blogcategories")
    public String[] getBlogCategories() {
        return getProperty("website:blogcategories");
    }

    @HippoEssentialsGenerated(internalName = "website:twitterhashtag")
    public String[] getTwitterHashtag() {
        return getProperty("website:twitterhashtag");
    }

    @HippoEssentialsGenerated(internalName = "website:contactdetails")
    public HippoHtml getContactDetails() {
        return getHippoHtml("website:contactdetails");
    }

    @HippoEssentialsGenerated(internalName = "website:relatedsubjects")
    public List<HippoBean> getRelatedSubjects() {
        return getLinkedBeans("website:relatedsubjects", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "common:SearchableTags")
    public String[] getTaxonomyTags() {
        return getProperty("common:SearchableTags");
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

}
