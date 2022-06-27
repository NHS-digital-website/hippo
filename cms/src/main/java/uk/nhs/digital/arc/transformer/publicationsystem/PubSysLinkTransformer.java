package uk.nhs.digital.arc.transformer.publicationsystem;

import org.onehippo.forge.content.pojo.model.ContentNode;
import uk.nhs.digital.arc.json.publicationsystem.PublicationsystemResourceOrExternalLink;
import uk.nhs.digital.arc.transformer.abs.AbstractSectionTransformer;

import javax.jcr.Session;

public class PubSysLinkTransformer extends AbstractSectionTransformer {

    private final PublicationsystemResourceOrExternalLink linkSection;
    private final String nodeType;

    public PubSysLinkTransformer(Session session, PublicationsystemResourceOrExternalLink section, String nodeType) {
        super(session);
        this.linkSection = section;
        this.nodeType = nodeType;
    }

    public ContentNode process() {
        ContentNode sectionNode = new ContentNode(nodeType, PUBLICATIONSYSTEM_RELATEDLINK);
        sectionNode.setProperty(PUBLICATIONSYSTEM_LINKTEXT, linkSection.getLinkTextReq());
        sectionNode.setProperty(PUBLICATIONSYSTEM_LINKURL, linkSection.getLinkUrlReq());

        return sectionNode;
    }
}
