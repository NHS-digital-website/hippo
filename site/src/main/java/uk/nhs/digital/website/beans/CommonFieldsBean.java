package uk.nhs.digital.website.beans;

import static org.apache.commons.collections.IteratorUtils.toList;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is a generic bean containing most of the used fields accross all the
 * document types. And it is mainly making the cdp.xml happy
 */
public class CommonFieldsBean extends BaseDocument {

    protected static int NO_LIMIT = 0;

    @HippoEssentialsGenerated(internalName = "website:seosummary", allowModifications = false)
    public HippoHtml getSeosummary() {
        return getHippoHtml("website:seosummary");
    }

    @HippoEssentialsGenerated(internalName = "website:shortsummary", allowModifications = false)
    public String getShortsummary() {
        return getProperty("website:shortsummary");
    }

    @HippoEssentialsGenerated(internalName = "website:summary", allowModifications = false)
    public HippoHtml getSummary() {
        return getHippoHtml("website:summary");
    }

    @HippoEssentialsGenerated(internalName = "website:title", allowModifications = false)
    public String getTitle() {
        return getProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:bannercontrols", allowModifications = false)
    public BannerControl  getBannercontrols() {
        return getBean("website:bannercontrols", BannerControl.class);
    }

    //================================================================================
    //getReleatedDocuemnts queries for reference documents where current document
    //is equal object of class beanClass

    protected <T extends HippoBean> List<T> getRelatedDocuments(
        String property,
        Class<T> beanClass
    ) throws HstComponentException, QueryException {

        return getRelatedDocuments(property, NO_LIMIT, null, "descending", beanClass, null );

    }

    protected <T extends HippoBean> List<T> getRelatedDocuments(
        String property,
        int limit,
        Class<T> beanClass
    ) throws HstComponentException, QueryException {

        return getRelatedDocuments(property, limit, null, "descending", beanClass, null );

    }

    protected <T extends HippoBean> List<T> getRelatedDocuments(
        String property,
        int limit,
        String orderBy,
        Class<T> beanClass
    ) throws HstComponentException, QueryException {

        return getRelatedDocuments(property, limit, orderBy, "descending", beanClass, null );

    }

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

        int maxElements = 6; //based on DW-1105 acceptance criteria
        return getRelatedDocuments(linkPaths, maxElements, null, null, News.class,filters);
    }

    public List<News> getRelatedNews() throws HstComponentException, QueryException {
        return getNews(false).stream().sorted(
            (n1, n2) -> n1.getPublisheddatetime().compareTo(n2.getPublisheddatetime())
        ).collect(Collectors.toList());
    }

    public List<News> getLatestNews() throws HstComponentException, QueryException {
        return getNews(true).stream().sorted(
            (n1, n2) -> n1.getPublisheddatetime().compareTo(n2.getPublisheddatetime())
        ).collect(Collectors.toList());
    }

}
