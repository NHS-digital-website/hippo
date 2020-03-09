package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "publicationsystem:accessList")
@Node(jcrType = "publicationsystem:accessList")
public class AccessList extends HippoDocument {

    @HippoEssentialsGenerated(internalName = "publicationsystem:releaseSubject")
    public List<ReleaseSubject> getReleaseSubjects() {
        return getChildBeansIfPermitted("publicationsystem:releaseSubject", ReleaseSubject.class);
    }

    protected <T extends HippoBean> List<T> getChildBeansIfPermitted(final String propertyName, final Class<T> beanMappingClass) {

        assertPropertyPermitted(propertyName);

        return getChildBeansByName(propertyName, beanMappingClass);
    }

    protected void assertPropertyPermitted(String propertyName) {
        // To be overwritten by subclasses for specific implementation
    }


}
