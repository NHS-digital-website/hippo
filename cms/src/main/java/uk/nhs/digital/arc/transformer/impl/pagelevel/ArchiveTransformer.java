package uk.nhs.digital.arc.transformer.impl.pagelevel;

import org.onehippo.forge.content.pojo.model.ContentNode;
import uk.nhs.digital.arc.json.Archive;
import uk.nhs.digital.arc.transformer.abs.AbstractPageLevelTransformer;

public class ArchiveTransformer extends AbstractPageLevelTransformer {

    @Override
    public ContentNode process() {
        Archive archive = (Archive)doctype;
        ContentNode contentNode = new ContentNode(archive.getTitleReq(), doctype.getDoctypeReq().toLowerCase());

        contentNode.setProperty(PUBLICATIONSYSTEM_TITLE_UC, archive.getTitleReq());
        contentNode.setProperty(PUBLICATIONSYSTEM_SUMMARY,archive.getSummaryReq());
        contentNode.setProperty(PUBLICATIONSYSTEM_ADMINISTRATIVESOURCES, archive.getAdministrativeSources());
        setMultipleProp(contentNode,PUBLICATIONSYSTEM_GEOGRAPHICCOVERAGE, archive.getGeographicCoverage());
        setMultipleProp(contentNode,PUBLICATIONSYSTEM_INFORMATIONTYPE, archive.getInformationType());

        return contentNode;
    }
}
