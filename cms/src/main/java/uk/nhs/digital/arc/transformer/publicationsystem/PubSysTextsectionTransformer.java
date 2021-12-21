package uk.nhs.digital.arc.transformer.publicationsystem;

import org.onehippo.forge.content.pojo.model.ContentNode;
import uk.nhs.digital.arc.json.PublicationBodyItem;
import uk.nhs.digital.arc.json.publicationsystem.PublicationsystemTextsection;
import uk.nhs.digital.arc.storage.ArcStorageManager;
import uk.nhs.digital.arc.transformer.abs.AbstractSectionTransformer;

import javax.jcr.Session;

public class PubSysTextsectionTransformer extends AbstractSectionTransformer {

    private final PublicationsystemTextsection textSection;

    public PubSysTextsectionTransformer(Session session, PublicationBodyItem section, String docbase, ArcStorageManager storageManager) {
        super(session, docbase, storageManager);
        textSection = (PublicationsystemTextsection)section;
    }

    @Override
    public ContentNode process() {
        ContentNode sectionNode = new ContentNode(PUBLICATIONSYSTEM_BODYSECTIONS, PUBLICATIONSYSTEM_TEXTSECTION);
        sectionNode.setProperty(PUBLICATIONSYSTEM_HEADING, textSection.getHeading());

        ContentNode textContentNode = new ContentNode(PUBLICATIONSYSTEM_TEXT, HIPPOSTD_HTML);
        textContentNode.setProperty(HIPPOSTD_CONTENT, textSection.getText());
        sectionNode.addNode(textContentNode);

        return sectionNode;
    }
}
