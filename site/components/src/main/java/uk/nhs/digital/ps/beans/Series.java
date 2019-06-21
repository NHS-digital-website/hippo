package uk.nhs.digital.ps.beans;

import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.constraint;
import static uk.nhs.digital.ps.beans.PublicationBase.PropertyKeys.PUBLICLY_ACCESSIBLE;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.component.HstComponentException;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.ps.beans.structuredText.StructuredText;

import java.util.List;

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

    @HippoEssentialsGenerated(internalName = "publicationsystem:ShowLatest")
    public boolean getShowLatest() {
        return getProperty("publicationsystem:ShowLatest", false);
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:attachments")
    public List<ExtAttachment> getAttachments() {
        return getChildBeansIfPermitted("publicationsystem:attachments", ExtAttachment.class);
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:resourceLinks")
    public List<RelatedLink> getResourceLinks() {
        return getChildBeansIfPermitted("publicationsystem:resourceLinks", RelatedLink.class);
    }

    public Publication getLatestPublication() throws HstComponentException, QueryException {
        if (!getShowLatest()) {
            return null;
        }

        HippoBean folder = getCanonicalBean().getParentBean();

        HippoBeanIterator hippoBeans = HstQueryBuilder.create(folder)
            .ofTypes(Publication.class)
            .where(constraint(PUBLICLY_ACCESSIBLE).equalTo(true))
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
