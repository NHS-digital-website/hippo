package uk.nhs.digital.website.beans;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.pagination.Paginated;
import uk.nhs.digital.pagination.Pagination;
import uk.nhs.digital.ps.beans.IndexPage;
import uk.nhs.digital.ps.beans.IndexPageImpl;

import java.util.List;
import java.util.stream.IntStream;

@HippoEssentialsGenerated(internalName = "website:publishedworkchapter")
@Node(jcrType = "website:publishedworkchapter")
public class Publishedworkchapter extends CommonFieldsBean implements Paginated {

    private static final Logger log = LoggerFactory.getLogger(Publishedworkchapter.class);

    @HippoEssentialsGenerated(internalName = "hippotaxonomy:keys")
    public String[] getKeys() {
        return getProperty("hippotaxonomy:keys");
    }

    @HippoEssentialsGenerated(internalName = "website:friendlyurls")
    public Friendlyurls getFriendlyurls() {
        return getBean("website:friendlyurls", Friendlyurls.class);
    }

    @HippoEssentialsGenerated(internalName = "website:publicationStyle")
    public String getPublicationStyle() {
        return getProperty("website:publicationStyle");
    }

    @HippoEssentialsGenerated(internalName = "website:bannerImage")
    public HippoGalleryImageSet getBannerImage()  {
        return getLinkedBean("website:bannerImage", HippoGalleryImageSet.class);
    }

    @HippoEssentialsGenerated(internalName = "website:bannerImageAltText")
    public String getBannerImageAltText() {
        return getProperty("website:bannerImageAltText");
    }

    @HippoEssentialsGenerated(internalName = "website:button")
    public String getButton() {
        return getProperty("website:button");
    }

    @HippoEssentialsGenerated(internalName = "website:sections")
    public List<HippoBean> getSections() {
        return getChildBeansByName("website:sections");
    }

    public Publishedwork getPublishedWork() {
        final HstRequestContext context = RequestContextProvider.get();
        //  Publishedworkchapter publishedWorkChapter = context.getContentBean(Publishedworkchapter.class);
        try {
            HstQuery linkedBeanQuery = ContentBeanUtils.createIncomingBeansQuery(
                this.getCanonicalBean(), context.getSiteContentBaseBean(),
                "website:links/@hippo:docbase",
                Publishedwork.class, false);
            linkedBeanQuery.setLimit(1);
            return (Publishedwork) linkedBeanQuery.execute().getHippoBeans().nextHippoBean();
        } catch (QueryException queryException) {
            log.warn("QueryException ", queryException);
        }
        return null;
    }

    @Override
    public Pagination paginate() {
        Publishedwork publishedwork = getPublishedWork();
        if ( publishedwork != null) {
            int index = IntStream
                .range(0,  publishedwork.getLinks().size())
                .filter(i -> ((String) publishedwork.getLinks().get(i).getProperty("website:title")).equalsIgnoreCase(getTitle()))
                .findFirst()
                .orElse(-1);
            if (0 <= index && index < publishedwork.getLinks().size()) {
                if (index == 0) {
                    return new Pagination(new IndexPageImpl(publishedwork.getDisplayName(), publishedwork), getIndexPage(publishedwork, 1));
                } else if (index < publishedwork.getLinks().size() - 1) {
                    return new Pagination(getIndexPage(publishedwork, index - 1), getIndexPage(publishedwork, index + 1));
                } else {
                    return new Pagination(getIndexPage(publishedwork, index - 1), null);
                }
            }
        }
        return null;
    }

    private static IndexPage getIndexPage(Publishedwork publishedwork, int index) {
        HippoBean hippoBean = publishedwork.getLinks().get(index);
        return new IndexPageImpl(hippoBean.getDisplayName(), hippoBean);
    }

}
