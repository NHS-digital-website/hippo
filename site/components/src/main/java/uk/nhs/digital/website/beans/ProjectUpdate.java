package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.ps.beans.HippoBeanHelper;
import uk.nhs.digital.ps.directives.DateFormatterDirective;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

@HippoEssentialsGenerated(internalName = "website:projectupdate")
@Node(jcrType = "website:projectupdate")
public class ProjectUpdate extends CommonFieldsBean {

    public Organisation getOrganisation() {
        return getLinkedBean("website:organisation", Organisation.class);
    }

    public HippoHtml getShortSummaryHtml() {
        return getHippoHtml("website:shortsummaryhtml");
    }

    public String getTypeOfUpdate() {
        return getProperty("website:typeofupdate");
    }

    @HippoEssentialsGenerated(internalName = "website:updatetimestamp", allowModifications = false)
    public Calendar getUpdateTimestamp() {
        return getProperty("website:updatetimestamp");
    }

    @HippoEssentialsGenerated(internalName = "website:expirydate", allowModifications = false)
    public Calendar getExpiryDate() {
        return getProperty("website:expirydate");
    }

    @HippoEssentialsGenerated(internalName = "website:sections")
    public List<HippoBean> getSections() {
        return getChildBeansByName("website:sections");
    }

    @HippoEssentialsGenerated(internalName = "website:serviceaffected")
    public List<HippoBean> getServiceAffected() {
        return getChildBeansByName("website:serviceaffected");
    }

    @HippoEssentialsGenerated(internalName = "hippotaxonomy:keys", allowModifications = false)
    public String[] getKeys() {
        return getProperty("hippotaxonomy:keys");
    }

    public String getWikiLink() {
        return getProperty("website:wikilink");
    }

    public String[] getTwitterHashtag() {
        return getProperty("website:twitterhashtag");
    }

    public boolean isPubliclyAccessible() {
        return !isBeforeUpdateDateTime() || hasCorrectAccessKey();
    }

    public ProjectFeed getParentFeed() {
        ProjectFeed parentBean = null;

        HippoBean folder = getParentBean();
        while (!HippoBeanHelper.isRootFolder(folder)) {
            Iterator<ProjectFeed> iterator = folder.getChildBeans(ProjectFeed.class).iterator();
            if (iterator.hasNext()) {
                parentBean = iterator.next();
                break;
            } else {
                folder = folder.getParentBean();
            }
        }

        return parentBean;
    }

    private boolean isBeforeUpdateDateTime() {
        Calendar updateTimestamp = getUpdateTimestamp();
        if (updateTimestamp == null) {
            return false;
        }
        ZonedDateTime updateDateTime = updateTimestamp.toInstant()
            .atZone(DateFormatterDirective.TIME_ZONE.toZoneId());

        ZonedDateTime currentDateTime = ZonedDateTime
            .now(DateFormatterDirective.TIME_ZONE.toZoneId());

        return currentDateTime.isBefore(updateDateTime);
    }

    private boolean hasCorrectAccessKey() {
        ProjectFeed parentFeed = getParentFeed();

        if (parentFeed == null) {
            return false;
        }

        return parentFeed.isCorrectAccessKey();
    }
}
