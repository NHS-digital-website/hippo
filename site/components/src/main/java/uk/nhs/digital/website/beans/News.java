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

@HippoEssentialsGenerated(internalName = "website:news")
@Node(jcrType = "website:news")
public class News extends CommonFieldsBean {
    @HippoEssentialsGenerated(internalName = "website:creditbanner")
    public String getCreditBanner() {
        return getSingleProperty("website:creditbanner");
    }

    @HippoEssentialsGenerated(internalName = "website:leadimagesection")
    public LeadImageSection getLeadimagesection() {
        return getBean("website:leadimagesection", LeadImageSection.class);
    }

    @HippoEssentialsGenerated(internalName = "website:sections")
    public List<HippoBean> getSections() {
        return getChildBeansByName("website:sections");
    }

    @HippoEssentialsGenerated(internalName = "website:socialmediaimages")
    public SocialMediaImages getSocialmediaimages() {
        return getBean("website:socialmediaimages", SocialMediaImages.class);
    }

    @HippoEssentialsGenerated(internalName = "website:notesforeditors")
    public NotesForEditors getNotesforeditors() {
        return getBean("website:notesforeditors", NotesForEditors.class);
    }

    @HippoEssentialsGenerated(internalName = "website:peoplementioned")
    public List<HippoBean> getPeoplementioned() {
        return getLinkedBeans("website:peoplementioned", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:backstory")
    public HippoHtml getBackstory() {
        return getHippoHtml("website:backstory");
    }

    @HippoEssentialsGenerated(internalName = "website:mediacontact")
    public ContactDetail getMediacontact() {
        return getBean("website:mediacontact", ContactDetail.class);
    }

    @HippoEssentialsGenerated(internalName = "website:publisheddatetime")
    public Calendar getPublisheddatetime() {
        return getSingleProperty("website:publisheddatetime");
    }

    @HippoEssentialsGenerated(internalName = "website:twitterhashtag")
    public String[] getTwitterHashtag() {
        return getMultipleProperty("website:twitterhashtag");
    }

    @HippoEssentialsGenerated(internalName = "website:theme")
    public String getTheme() {
        return getSingleProperty("website:theme");
    }

    @HippoEssentialsGenerated(internalName = "website:type")
    public String getType() {
        return getSingleProperty("website:type");
    }

    @HippoEssentialsGenerated(internalName = "website:relateddocuments")
    public List<HippoBean> getRelateddocuments() {
        return getLinkedBeans("website:relateddocuments", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:display")
    public Boolean getDisplay() {
        return getSingleProperty("website:display");
    }

    @HippoEssentialsGenerated(internalName = "website:prioritynews")
    public Boolean getPrioritynews() {
        return getSingleProperty("website:prioritynews");
    }

    @HippoEssentialsGenerated(internalName = "common:SearchableTags")
    public String[] getTopics() {
        return getMultipleProperty("common:SearchableTags");
    }

    public List<News> getLatestNews() throws QueryException {

        HippoBean folder = getCanonicalBean().getParentBean();

        HippoBeanIterator hippoBeans = HstQueryBuilder.create(folder)
            .ofTypes(News.class)
            .where(constraint("jcr:uuid").notEqualTo(this.getIdentifier()))
            .orderByDescending("website:publisheddatetime")
            .limit(5)
            .build()
            .execute()
            .getHippoBeans();

        return toList(hippoBeans);

    }
}
