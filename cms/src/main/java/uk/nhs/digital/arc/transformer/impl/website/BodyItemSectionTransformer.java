package uk.nhs.digital.arc.transformer.impl.website;

import uk.nhs.digital.arc.json.PublicationBodyItem;
import uk.nhs.digital.arc.storage.ArcStorageManager;

import javax.jcr.Session;

public class BodyItemSectionTransformer extends PublicationWebsiteSectionTransformer {

    public BodyItemSectionTransformer(Session session, PublicationBodyItem section, String docbase, ArcStorageManager storageManager) {
        super(session, section, docbase, storageManager);

        // It's enough to set this property name to ensure that we get the correct node
        // name be created in the JCR
        this.setContentNodeName(PUBLICATION_SYSTEM + "bodySections");
    }
}
