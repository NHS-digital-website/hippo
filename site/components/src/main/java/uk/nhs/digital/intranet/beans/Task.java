package uk.nhs.digital.intranet.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import uk.nhs.digital.website.beans.BannerControl;
import uk.nhs.digital.website.beans.PriorityAction;
import uk.nhs.digital.website.beans.Team;

import java.util.Calendar;
import java.util.List;

@Node(jcrType = "intranet:task")
public class Task extends HippoDocument {

    public String getTitle() {
        return getProperty("intranet:title");
    }

    public List<String> getAlternativeNames() {
        return getChildBeansByName("intranet:alternativenames");
    }

    public HippoHtml getIntroduction() {
        return getHippoHtml("intranet:introduction");
    }

    public HippoHtml getShortSummary() {
        return getHippoHtml("intranet:shortsummary");
    }

    public List<Team> getResponsibleTeam() {
        return getLinkedBeans("intranet:responsibleteam", Team.class);
    }

    public Task getChildOf() {
        return getLinkedBean("intranet:childof", Task.class);
    }

    public Calendar getReviewDate() {
        return getProperty("intranet:reviewdate");
    }

    public Calendar getExpiryDate() {
        return getProperty("intranet:expirydate");
    }

    public List<PriorityAction> getPriorityActions() {
        return getChildBeansByName("intranet:priorityactions", PriorityAction.class);
    }

    public List<HippoBean> getSections() {
        return getChildBeansByName("intranet:sections");
    }

    public BannerControl getBannercontrols() {
        return getBean("intranet:bannercontrols", BannerControl.class);
    }

    public String[] getKeys() {
        return getProperty("hippotaxonomy:keys");
    }
}
