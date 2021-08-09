package uk.nhs.digital.website.beans;


import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
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

@HippoEssentialsGenerated(internalName = "website:policypage")
@Node(jcrType = "website:policypage")
public class PolicyPage extends CommonFieldsBean implements Paginated {

    private static final Logger log = LoggerFactory.getLogger(PolicyPage.class);

    @HippoEssentialsGenerated(internalName = "website:pubstylepolicypage")
    public PublicationStylePolicyPage getPublicationStyle() {
        return getBean("website:pubstylepolicypage", PublicationStylePolicyPage.class);
    }

    @HippoEssentialsGenerated(internalName = "website:bodySection")
    public List<HippoBean> getSections() {
        return getChildBeansByName("website:bodySection");
    }

    public Policy getPolicy() {
        final HstRequestContext context = RequestContextProvider.get();
        try {
            HstQuery linkedBeanQuery = ContentBeanUtils.createIncomingBeansQuery(
                this.getCanonicalBean(), context.getSiteContentBaseBean(),
                "website:childPage/@hippo:docbase",
                Policy.class, false);
            linkedBeanQuery.setLimit(1);
            return (Policy) linkedBeanQuery.execute().getHippoBeans().nextHippoBean();
        } catch (QueryException queryException) {
            log.warn("QueryException ", queryException);
        }
        return null;
    }

    @Override
    public Pagination paginate() {
        Policy corporatePolicy = getPolicy();
        if (corporatePolicy != null) {
            int index = IntStream
                .range(0, corporatePolicy.getLinks().size())
                .filter(i -> ((String) corporatePolicy.getLinks().get(i).getSingleProperty("website:title")).equalsIgnoreCase(getTitle()))
                .findFirst()
                .orElse(-1);
            // If the index is set and you're not on the last page...
            if (0 <= index && index < corporatePolicy.getLinks().size()) {
                if (index == 0 && corporatePolicy.getLinks().size() < 2) {
                    // ... and you're on the first page/there isn't more than 1 extra page,
                    // then set an empty next page
                    return new Pagination(new IndexPageImpl(corporatePolicy.getDisplayName(), corporatePolicy), null);
                } else if (index == 0) {
                    // ... and you're on the first page
                    // then set a populated next page
                    return new Pagination(new IndexPageImpl(corporatePolicy.getDisplayName(), corporatePolicy), getIndexPage(corporatePolicy, index + 1));
                } else if (index < corporatePolicy.getLinks().size() - 1) {
                    // ... and you're not on the first or last page,
                    // then set the next page to the index + 1
                    return new Pagination(getIndexPage(corporatePolicy, index - 1), getIndexPage(corporatePolicy, index + 1));
                } else {
                    // else you're on the last page
                    // then set an empty next page:
                    return new Pagination(getIndexPage(corporatePolicy, index - 1), null);
                }
            }
        }
        return null;
    }

    private static IndexPage getIndexPage(Policy policy, int index) {
        HippoBean hippoBean = policy.getLinks().get(index);
        return new IndexPageImpl(hippoBean.getDisplayName(), hippoBean);
    }
}
