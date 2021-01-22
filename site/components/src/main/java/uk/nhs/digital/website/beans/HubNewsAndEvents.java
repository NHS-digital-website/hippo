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

        HstRequestContext requestContext = RequestContextProvider.get();
        HippoBean scope = requestContext.getSiteContentBaseBean();

        HstQuery hstBlogQuery = HstQueryBuilder.create(scope).ofTypes(Blog.class).orderByDescending("website:dateofpublication")
            .build();

        HstQueryResult blogResult = hstBlogQuery.execute();
        return toList(blogResult.getHippoBeans());

    }

    public List<HippoBean> getLatestNewsArticles() throws QueryException {

        HstRequestContext requestContext = RequestContextProvider.get();
        HippoBean scope = requestContext.getSiteContentBaseBean().getBean("news-and-events/latest-news");

        HstQuery hstNewsQuery = HstQueryBuilder.create(scope).ofTypes(News.class).orderByDescending("website:publisheddatetime")
            .build();

        HstQueryResult newsResult = hstNewsQuery.execute();
        return toList(newsResult.getHippoBeans());

    }

}
