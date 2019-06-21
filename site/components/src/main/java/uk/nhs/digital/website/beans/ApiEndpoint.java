package uk.nhs.digital.website.beans;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:apiendpoint")
@Node(jcrType = "website:apiendpoint")
public class ApiEndpoint extends CommonFieldsBean {

    private static final Logger log = LoggerFactory.getLogger(Publishedworkchapter.class);

    @HippoEssentialsGenerated(internalName = "website:requestname")
    public String getRequestname() {
        return getProperty("website:requestname");
    }

    @HippoEssentialsGenerated(internalName = "website:apimethod")
    public String getApimethod() {
        return getProperty("website:apimethod");
    }

    @HippoEssentialsGenerated(internalName = "website:uriaddress")
    public String getUriaddress() {
        return getProperty("website:uriaddress");
    }

    @HippoEssentialsGenerated(internalName = "website:authnauths")
    public AuthnAndAuths getAuthnauths() {
        return getBean("website:authnauths", AuthnAndAuths.class);
    }

    @HippoEssentialsGenerated(internalName = "website:samplerequest")
    public List<HippoBean> getSamplerequest() {
        return getChildBeansByName("website:samplerequest");
    }

    @HippoEssentialsGenerated(internalName = "website:apiendpointparams")
    public List<HippoBean> getApiendpointparams() {
        return getChildBeansByName("website:apiendpointparams");
    }

    @HippoEssentialsGenerated(internalName = "website:sampleresponse")
    public List<HippoBean> getSampleresponse() {
        return getChildBeansByName("website:sampleresponse");
    }

    @HippoEssentialsGenerated(internalName = "website:responsedefinitions")
    public List<HippoBean> getResponsedefinitions() {
        return getChildBeansByName("website:responsedefinitions");
    }

    @HippoEssentialsGenerated(internalName = "website:statuserrorcodes")
    public List<HippoBean> getStatuserrorcodes() {
        return getChildBeansByName("website:statuserrorcodes");
    }

    @HippoEssentialsGenerated(internalName = "website:sections")
    public List<HippoBean> getSections() {
        return getChildBeansByName("website:sections");
    }

    @HippoEssentialsGenerated(internalName = "website:releasestatus")
    public String getReleasestatus() {
        return getProperty("website:releasestatus");
    }

    @HippoEssentialsGenerated(internalName = "website:resdefcontent")
    public HippoHtml getResdefcontent() {
        return getHippoHtml("website:resdefcontent");
    }

    public ApiMaster getApiMasterParent() {
        final HstRequestContext context = RequestContextProvider.get();

        try {
            HstQuery linkedBeanQuery = ContentBeanUtils.createIncomingBeansQuery(
                this.getCanonicalBean(), context.getSiteContentBaseBean(),
                "website:apiendpointgroups/website:apirequest/@hippo:docbase",
                ApiMaster.class, false);
            linkedBeanQuery.setLimit(1);
            HstQueryResult hstQueryResult = linkedBeanQuery.execute();
            HippoBeanIterator hippoBeanIterator = hstQueryResult.getHippoBeans();
            if (hippoBeanIterator.getSize() > 0) {
                return (ApiMaster) hippoBeanIterator.nextHippoBean();
            } else {
                return null;
            }
        } catch (QueryException queryException) {
            log.warn("QueryException ", queryException);
        }
        return null;
    }

}
