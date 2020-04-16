package uk.nhs.digital.intranet.beans;

import static org.apache.commons.collections.IteratorUtils.toList;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.ContentBeanUtils;
import uk.nhs.digital.ps.beans.HippoBeanHelper;
import uk.nhs.digital.website.beans.BannerControl;
import uk.nhs.digital.website.beans.PriorityAction;
import uk.nhs.digital.website.beans.Team;

import java.util.Calendar;
import java.util.List;

@Node(jcrType = "intranet:task")
public class Task extends BaseDocument {

    public String getTitle() {
        return getProperty("intranet:title");
    }

    public String[] getAlternativeNames() {
        return getProperty("intranet:alternativenames");
    }

    public HippoHtml getIntroduction() {
        return getHippoHtml("intranet:introduction");
    }

    public HippoHtml getShortSummary() {
        return getHippoHtml("intranet:shortsummary");
    }

    public List<Team> getResponsibleTeams() {
        return getLinkedBeans("intranet:responsibleteams", Team.class);
    }

    public List<Task> getChildren() {
        return getLinkedBeans("intranet:relateddocuments", Task.class);
    }

    public List<Task> getParents() throws QueryException {
        return getRelatedDocuments("intranet:relateddocuments/@hippo:docbase",
            Task.class);
    }

    public Calendar getReviewDate() {
        return getProperty("intranet:reviewdate");
    }

    public Calendar getExpiryDate() {
        return getProperty("intranet:expirydate");
    }

    public List<PriorityAction> getPriorityActions() {
        return getChildBeansByName("intranet:priorityactions",
            PriorityAction.class);
    }

    public List<HippoBean> getSections() {
        return getChildBeansByName("intranet:sections");
    }

    public Boolean getPriorityAction() {
        return getProperty("intranet:prioritytask");
    }

    public BannerControl getBannercontrols() {
        return getBean("intranet:bannercontrols", BannerControl.class);
    }

    public String[] getKeys() {
        return getProperty("hippotaxonomy:keys");
    }

    public String[] getTopics() {
        return getProperty("common:SearchableTags");
    }

    public List<String> getFullTaxonomyList() {
        return HippoBeanHelper.getFullTaxonomyList(this);
    }

    private <T extends HippoBean> List<T> getRelatedDocuments(String property,
        Class<T> beanClass) throws HstComponentException, QueryException {

        final HstRequestContext context = RequestContextProvider.get();

        HstQuery query = ContentBeanUtils.createIncomingBeansQuery(
            this.getCanonicalBean(), context.getSiteContentBaseBean(),
            property, beanClass, false);

        return toList(query.execute().getHippoBeans());
    }
}
