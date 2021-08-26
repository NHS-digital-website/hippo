package uk.nhs.digital.ps.components;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import uk.nhs.digital.common.components.ContentRewriterComponent;
import uk.nhs.digital.ps.beans.Publication;
import uk.nhs.digital.ps.beans.PublicationPage;

import java.util.ArrayList;
import java.util.List;

public class PublicationPageComponent extends ContentRewriterComponent {

    private static final String RELATED_LINKS_ID = "Related links";

    private final PageSectionGrouper pageSectionGrouper = new PageSectionGrouper();

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
        PublicationPage page = (PublicationPage) request.getRequestContext().getContentBean();

        request.setAttribute("page", page);
        request.setAttribute("pageSections", pageSectionGrouper.groupSections(page.getSections()));
        request.setAttribute("index", getIndex(page.getPublication()));
    }

    private List<String> getIndex(Publication publication) {
        List<String> index = new ArrayList<>();

        if (isNotEmpty(publication.getRelatedLinks())) {
            index.add(RELATED_LINKS_ID);
        }

        return index;
    }
}
