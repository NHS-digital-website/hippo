package uk.nhs.digital.website.beans;

import static org.apache.commons.collections.IteratorUtils.toList;

import org.hippoecm.hst.container.RequestContextProvider;

import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

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

        return getRelatedDocuments(property, NO_LIMIT, null, "descending", beanClass );

    }

    protected <T extends HippoBean> List<T> getRelatedDocuments(
        String property, 
        int limit, 
        Class<T> beanClass
    ) throws HstComponentException, QueryException {
      
        return getRelatedDocuments(property, limit, null, "descending", beanClass );

    }

    protected <T extends HippoBean> List<T> getRelatedDocuments(
        String property, 
        int limit, 
        String orderBy, 
        Class<T> beanClass
    ) throws HstComponentException, QueryException {

        return getRelatedDocuments(property, limit, orderBy, "descending", beanClass );

    }

    protected <T extends HippoBean> List<T> getRelatedDocuments(
        String property, 
        int limit, 
        String orderBy, 
        String orderDirection, 
        Class<T> beanClass
    ) throws HstComponentException, QueryException {

        final HstRequestContext context = RequestContextProvider.get();

        HstQuery query = ContentBeanUtils.createIncomingBeansQuery(
            this.getCanonicalBean(), context.getSiteContentBaseBean(),
            property, beanClass, false);

        applyRestrictionsToQuery(query, limit, orderBy, orderDirection);

        return toList(query.execute().getHippoBeans());
    }

    private void applyRestrictionsToQuery(HstQuery query, int limit, String orderBy, String orderDirection) {

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
    }

}
