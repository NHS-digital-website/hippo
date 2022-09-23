package uk.nhs.digital.arc.transformer.publicationsystem;

import org.onehippo.forge.content.pojo.model.ContentNode;
import uk.nhs.digital.arc.exception.ArcException;
import uk.nhs.digital.arc.json.BasicBodyItem;
import uk.nhs.digital.arc.json.publicationsystem.PublicationsystemChangeNotice;
import uk.nhs.digital.arc.transformer.abs.AbstractSectionTransformer;
import uk.nhs.digital.arc.util.DateHelper;

import javax.jcr.Session;

public class PubSysChangenoticeTransformer extends AbstractSectionTransformer {

    private final PublicationsystemChangeNotice changeNotice;

    public PubSysChangenoticeTransformer(Session session, BasicBodyItem section) {
        super(session);
        changeNotice = (PublicationsystemChangeNotice)section;
    }

    @Override
    public ContentNode process() throws ArcException {
        ContentNode sectionNode = new ContentNode(PUBLICATIONSYSTEM_CHANGENOTICE, PUBLICATIONSYSTEM_CHANGENOTICE);
        setSingleRequiredDateProp(sectionNode, PUBLICATIONSYSTEM_DATE, "date_REQ", DateHelper.massageDate(changeNotice.getDateReq()));
        setSingleProp(sectionNode, PUBLICATIONSYSTEM_TITLE, changeNotice.getTitle());

        ContentNode contentData = new ContentNode(PUBLICATIONSYSTEM_CONTENT, HIPPOSTD_HTML);
        contentData.setProperty(HIPPOSTD_CONTENT, changeNotice.getContentReq());
        sectionNode.addNode(contentData);

        return sectionNode;
    }
}
