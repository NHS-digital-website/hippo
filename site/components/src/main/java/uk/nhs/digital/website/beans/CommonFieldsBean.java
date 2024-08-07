package uk.nhs.digital.website.beans;

import static org.apache.commons.collections.IteratorUtils.toList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.query.filter.BaseFilter;
import org.hippoecm.hst.content.beans.query.filter.Filter;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.hippoecm.repository.util.DateTools;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.common.util.DocumentUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This is a generic bean containing most of the used fields accross all the
 * document types. And it is mainly making the cdp.xml happy
 */
public class CommonFieldsBean extends BaseDocument {

    protected static int NO_LIMIT = 0;

    @JsonProperty
    public String getSeosummaryJson() {
        return getHippoHtmlContent("website:seosummary");
    }

    @HippoEssentialsGenerated(internalName = "website:seosummary", allowModifications = false)
    public HippoHtml getSeosummary() {
        return getHippoHtml("website:seosummary");
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:shortsummary", allowModifications = false)
    public String getShortsummary() {
        return getSingleProperty("website:shortsummary");
    }

    @JsonProperty("summary")
    public String getSummaryJson() {
        return getHippoHtmlContent("website:summary");
    }

    @HippoEssentialsGenerated(internalName = "website:summary", allowModifications = false)
    public HippoHtml getSummary() {
        return getHippoHtml("website:summary");
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:title", allowModifications = false)
    public String getTitle() {
        return getSingleProperty("website:title");
    }

    @JsonIgnore
    @HippoEssentialsGenerated(internalName = "website:bannercontrols", allowModifications = false)
    public BannerControl  getBannercontrols() {
        return getBean("website:bannercontrols", BannerControl.class);
    }

    @HippoEssentialsGenerated(internalName = "website:publisheddate")
    public Date getPublishedDateCommon() {
        try {
            GregorianCalendar publishedAt = getSingleProperty("hippostdpubwf:publicationDate");
            return publishedAt.getTime();
        } catch (Exception e) {
            return Calendar.getInstance().getTime();
        }
    }

    public List<Update> getUpdates() throws QueryException {
        List<Update> allLinkedUpdates = getRelatedDocuments(
            "website:relateddocument/@hippo:docbase", Update.class);
        return DocumentUtils.getFilteredAndSortedUpdates(allLinkedUpdates);
    }

    //================================================================================
    // JSON-output related helper functions

    @JsonIgnore
    protected String getHippoHtmlContent(String property) {
        HippoHtml html = getHippoHtml(property);
        if (html != null) {
            return html.getContent();
        }
        return null;
    }

    //================================================================================
    //getReleatedDocuemnts queries for reference documents where current document
    //is equal object of class beanClass

    @JsonIgnore
    protected <T extends HippoBean> List<T> getRelatedDocuments(
        String property,
        Class<T> beanClass
    ) throws HstComponentException, QueryException {

        return getRelatedDocuments(property, NO_LIMIT, null, "descending", beanClass, null );

    }

    @JsonIgnore
    protected <T extends HippoBean> List<T> getRelatedDocuments(
        String property,
        int limit,
        Class<T> beanClass
    ) throws HstComponentException, QueryException {

        return getRelatedDocuments(property, limit, null, "descending", beanClass, null );

    }

    @JsonIgnore
    protected <T extends HippoBean> List<T> getRelatedDocuments(
        String property,
        int limit,
        String orderBy,
        Class<T> beanClass
    ) throws HstComponentException, QueryException {

        return getRelatedDocuments(property, limit, orderBy, "descending", beanClass, null );

    }

    @JsonIgnore
    protected <T extends HippoBean> List<T> getRelatedDocuments(
        String property,
        int limit,
        String orderBy,
        String orderDirection,
        Class<T> beanClass,
        List<BaseFilter> andFilters
    ) throws HstComponentException, QueryException {

        List<String> linkPaths  = new ArrayList<>();
        linkPaths.add(property);
        return getRelatedDocuments(linkPaths, limit, orderBy, orderDirection, beanClass, andFilters);
    }

    protected <T extends HippoBean> List<T> getRelatedDocuments(
        List<String> properties,
        int limit,
        String orderBy,
        String orderDirection,
        Class<T> beanClass,
        List<BaseFilter> andFilters
    ) throws HstComponentException, QueryException {

        HstQuery query = getInitialQuery(properties, beanClass);

        applyRestrictionsToQuery(query, limit, orderBy, orderDirection, andFilters);

        return toList(query.execute().getHippoBeans());
    }

    protected <T extends HippoBean> HstQuery getInitialQuery(List<String> properties , Class<T> beanClass) throws HstComponentException, QueryException {

        final HstRequestContext context = RequestContextProvider.get();

        HstQuery query = ContentBeanUtils.createIncomingBeansQuery(
            this.getCanonicalBean(), context.getSiteContentBaseBean(),
            properties, beanClass, false);

        return query;
    }

    private void applyRestrictionsToQuery(HstQuery query, int limit, String orderBy, String orderDirection, List<BaseFilter> andFilters) {

        if (limit > NO_LIMIT) {
            query.setLimit(limit);
        }

        if (orderBy != null) {
            if (orderDirection == "ascending") {
                query.addOrderByAscending(orderBy);
            } else {
                query.addOrderByDescending(orderBy);
            }
        }

        //applying AND filters
        if (andFilters != null && andFilters.size() > 0) {
            Filter initFilter = (Filter)query.getFilter();
            Filter mainFilter = query.createFilter();
            mainFilter.addAndFilter(initFilter);
            for (BaseFilter filter : andFilters) {
                mainFilter.addAndFilter(filter);
            }
            query.setFilter(mainFilter);
        }
    }

    public List<Event> getRelatedEvents() throws HstComponentException, QueryException {

        List<String> linkPaths = new ArrayList<>();
        linkPaths.add("website:relatedDocuments/@hippo:docbase");
        linkPaths.add("website:peoplementioned/@hippo:docbase");

        Filter filter = getInitialQuery(linkPaths, Event.class).createFilter();
        Calendar today = Calendar.getInstance();
        filter.addGreaterOrEqualThan("website:events/website:enddatetime", today, DateTools.Resolution.DAY);

        List<BaseFilter> filters = new ArrayList<BaseFilter>();
        filters.add(filter);

        return getRelatedDocuments(linkPaths, NO_LIMIT, null, null, Event.class, filters);
    }

    private List<News> getNews(boolean isLatestNews) throws HstComponentException, QueryException {

        List<String> linkPaths = new ArrayList<>();
        linkPaths.add("website:relateddocuments/@hippo:docbase");
        linkPaths.add("website:peoplementioned/@hippo:docbase");

        Filter filter = getInitialQuery(linkPaths, News.class).createFilter();
        Calendar thresholdDate = Calendar.getInstance();
        thresholdDate.add(Calendar.MONTH, -2);
        if (isLatestNews) {
            filter.addGreaterOrEqualThan("website:publisheddatetime", thresholdDate, DateTools.Resolution.DAY);
        } else {
            filter.addLessThan("website:publisheddatetime", thresholdDate, DateTools.Resolution.DAY);
        }

        List<BaseFilter> filters = new ArrayList<BaseFilter>();
        filters.add(filter);

        int maxElements = 7; //originally based on DW-1105 acceptance criteria - changed to fit DW-2238
        return getRelatedDocuments(linkPaths, maxElements, null, null, News.class,filters);
    }

    public List<News> getRelatedNews() throws HstComponentException, QueryException {
        return getNews(false).stream().sorted(
            (n1, n2) -> n2.getPublisheddatetime().compareTo(n1.getPublisheddatetime())
        ).collect(Collectors.toList());
    }

    public List<News> getLatestNews() throws HstComponentException, QueryException {
        return getNews(true).stream().sorted(
            (n1, n2) -> n2.getPublisheddatetime().compareTo(n1.getPublisheddatetime())
        ).collect(Collectors.toList());
    }

    public String[] getKeys() {
        return getMultipleProperty("hippotaxonomy:keys");
    }

}
