package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.website.beans.Organisation;

import java.util.List;

@HippoEssentialsGenerated(internalName = "publicationsystem:releaseSubject")
@Node(jcrType = "publicationsystem:releaseSubject")
public class ReleaseSubject extends HippoDocument {


    @HippoEssentialsGenerated(internalName = "publicationsystem:additonalDetail")
    public HippoHtml getAdditionalDetail() {
        return getHippoHtml("publicationsystem:additonalDetail");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:organisation")
    public Organisation getOrganisation() {
        return getLinkedBean("publicationsystem:organisation", Organisation.class);
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:recepients")
    public List<JobRole> getRecipients() {
        return getChildBeansIfPermitted("publicationsystem:recepients", JobRole.class);
    }

    protected <T extends HippoBean> List<T> getChildBeansIfPermitted(final String propertyName, final Class<T> beanMappingClass) {
        assertPropertyPermitted(propertyName);
        return getChildBeansByName(propertyName, beanMappingClass);
    }

    protected void assertPropertyPermitted(String propertyName) {
        // To be overwritten by subclasses for specific implementation
    }
}
