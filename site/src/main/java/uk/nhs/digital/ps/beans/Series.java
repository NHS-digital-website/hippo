package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.component.HstComponentException;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.ps.beans.structuredText.StructuredText;

@HippoEssentialsGenerated(internalName = "publicationsystem:series")
@Node(jcrType = "publicationsystem:series")
public class Series extends BaseDocument {

    public HippoBean getSelfLinkBean() {
        return getCanonicalBean().getParentBean();
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:Title")
    public String getTitle() {
        return getProperty("publicationsystem:Title");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:Summary")
    public StructuredText getSummary() {
        return new StructuredText(getProperty("publicationsystem:Summary"));
    }

    public Publication getLatestPublication() throws HstComponentException, QueryException {
        HippoBean folder = getCanonicalBean().getParentBean();

        HippoBeanIterator hippoBeans = HstQueryBuilder.create(folder)
            .ofTypes(Publication.class)
            .orderByDescending("publicationsystem:NominalDate")
            .limit(1)
            .build()
            .execute()
            .getHippoBeans();
        if (hippoBeans.hasNext()) {
            return (Publication) hippoBeans.nextHippoBean();
        }

        return null;
    }
}
