package uk.nhs.digital.arc.transformer.publicationsystem;

import org.onehippo.forge.content.pojo.model.ContentNode;
import org.onehippo.forge.content.pojo.model.ContentPropertyType;
import uk.nhs.digital.arc.json.BasicBodyItem;
import uk.nhs.digital.arc.json.publicationsystem.PublicationsystemInteractivetool;
import uk.nhs.digital.arc.transformer.abs.AbstractSectionTransformer;
import uk.nhs.digital.arc.util.DateHelper;

import javax.jcr.Session;

public class PubSysInteractivetoolTransformer extends AbstractSectionTransformer {

    private final PublicationsystemInteractivetool interactivetool;

    public PubSysInteractivetoolTransformer(Session session, BasicBodyItem section) {
        super(session);
        interactivetool = (PublicationsystemInteractivetool) section;
    }

    @Override
    public ContentNode process() {
        ContentNode sectionNode = new ContentNode(PUBLICATIONSYSTEM_INTERACTIVETOOL, PUBLICATIONSYSTEM_INTERACTIVETOOL);
        sectionNode.setProperty(PUBLICATIONSYSTEM_DATE, ContentPropertyType.DATE, DateHelper.massageDate(interactivetool.getDate()));
        sectionNode.setProperty(PUBLICATIONSYSTEM_LINK, interactivetool.getLinkReq());
        sectionNode.setProperty(PUBLICATIONSYSTEM_TITLE, interactivetool.getTitleReq());
        sectionNode.setProperty(PUBLICATIONSYSTEM_ACCESSIBLE, ContentPropertyType.BOOLEAN, interactivetool.getAccessibleReq());

        ContentNode cmAtt = new ContentNode(PUBLICATIONSYSTEM_CONTENT, HIPPOSTD_HTML);
        cmAtt.setProperty(HIPPOSTD_CONTENT, interactivetool.getContent());
        sectionNode.addNode(cmAtt);

        if (interactivetool.getAccessibleLocation() != null) {
            ContentNode cmAccess = new ContentNode(PUBLICATIONSYSTEM_ACCESSIBLELOCATION, PUBLICATIONSYSTEM_ACCESSIBLELINK);
            cmAccess.setProperty(PUBLICATIONSYSTEM_LINK, interactivetool.getAccessibleLocation().getLinkReq());
            cmAccess.setProperty(PUBLICATIONSYSTEM_TITLE, interactivetool.getAccessibleLocation().getTitleReq());

            ContentNode cmContent = new ContentNode(PUBLICATIONSYSTEM_CONTENT, HIPPOSTD_HTML);
            cmContent.setProperty(HIPPOSTD_CONTENT, interactivetool.getAccessibleLocation().getContent());
            cmAccess.addNode(cmContent);

            sectionNode.addNode(cmAccess);
        }

        return sectionNode;
    }
}
