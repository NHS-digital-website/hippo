package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.hst.core.component.HstComponentException;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:businessunit")
@Node(jcrType = "website:businessunit")
public class BusinessUnit extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:portfoliocode")
    public String getPortfoliocode() {
        return getSingleProperty("website:portfoliocode");
    }

    @HippoEssentialsGenerated(internalName = "website:vision")
    public HippoHtml getVision() {
        return getHippoHtml("website:vision");
    }

    @HippoEssentialsGenerated(internalName = "website:purposes")
    public List<HippoHtml> getPurposes() {
        return getChildBeansByName("website:purposes", HippoHtml.class);
    }

    @HippoEssentialsGenerated(internalName = "website:sections")
    public List<HippoBean> getSections() {
        return getChildBeansByName("website:sections");
    }

    @HippoEssentialsGenerated(internalName = "website:ispartofbusinessunit")
    public HippoBean getIspartofbusinessunit() {
        return getLinkedBean("website:ispartofbusinessunit", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:responsiblerole")
    public HippoBean getResponsiblerole() {
        return getLinkedBean("website:responsiblerole", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:order")
    public String getOrder() {
        return getSingleProperty("website:order");
    }

    public List<BusinessUnit> getChildren() throws HstComponentException, QueryException {

        int limit = 50;
        return getRelatedDocuments(
           "website:ispartofbusinessunit/@hippo:docbase",
           limit,
           "website:order",
           "ascending",
           BusinessUnit.class,
           null);
    }
}
