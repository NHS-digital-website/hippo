package uk.nhs.digital.website.beans;

import static org.apache.commons.collections.IteratorUtils.toList;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@HippoEssentialsGenerated(internalName = "website:hubnewsandevents")
@Node(jcrType = "website:hubnewsandevents")
public class HubNewsAndEvents extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:publicationdate")
    public Calendar getPublicationDate() {
        return getSingleProperty("website:publicationdate");
    }

    @HippoEssentialsGenerated(internalName = "website:socialmedia")
    public SocialMedia getSocialmedia() {
        return getBean("website:socialmedia", SocialMedia.class);
    }

    @HippoEssentialsGenerated(internalName = "website:signupsummary")
    public String getSignupsummary() {
        return getSingleProperty("website:signupsummary");
    }

    @HippoEssentialsGenerated(internalName = "website:ctabutton")
    public CtaButton getCtabutton() {
        return getBean("website:ctabutton", CtaButton.class);
    }

    @HippoEssentialsGenerated(internalName = "website:sections")
    public List<HippoBean> getSections() {
        return getChildBeansByName("website:sections");
    }

    @HippoEssentialsGenerated(internalName = "website:contactdetails")
    public HippoHtml getContactDetails() {
        return getHippoHtml("website:contactdetails");
    }

    public List<HippoBean> getNewsData() throws HstComponentException, QueryException {
        return getLatestNewsArticles();
    }

    public List<HippoBean> getBlogData() throws HstComponentException, QueryException {
        return getLatestBlog();
    }

    public List<HippoBean> getLatestBlog() throws QueryException {

        final int sizeLimit = 10;
        HstRequestContext requestContext = RequestContextProvider.get();
        HippoBean scope = requestContext.getSiteContentBaseBean();
        if (scope == null) { return Collections.emptyList(); }

        HstQuery hstBlogQuery = HstQueryBuilder.create(scope)
            .ofTypes(Blog.class)
            .orderByDescending("website:dateofpublication")
            .limit(sizeLimit)
            .build();

        HstQueryResult blogResult = hstBlogQuery.execute();
        return toList(blogResult.getHippoBeans());

    }

    public List<HippoBean> getLatestNewsArticles() throws QueryException {

        final int sizeLimit = 10;
        HstRequestContext requestContext = RequestContextProvider.get();
        HippoBean scope = requestContext.getSiteContentBaseBean().getBean("news/latest-news");
        if (scope == null) { return Collections.emptyList(); }

        HstQuery hstNewsQuery = HstQueryBuilder.create(scope)
            .ofTypes(News.class)
            .orderByDescending("website:publisheddatetime")
            .limit(sizeLimit)
            .build();

        HstQueryResult newsResult = hstNewsQuery.execute();
        return toList(newsResult.getHippoBeans());

    }

}
