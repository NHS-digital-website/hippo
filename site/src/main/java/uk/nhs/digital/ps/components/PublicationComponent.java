package uk.nhs.digital.ps.components;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.onehippo.cms7.essentials.components.EssentialsContentComponent;
import uk.nhs.digital.ps.beans.Publication;

import java.util.ArrayList;
import java.util.List;

public class PublicationComponent extends EssentialsContentComponent {

    private static final String SUMMARY_ID = "Summary";
    private static final String KEY_FACTS_ID = "Key Facts";
    private static final String ADMIN_SOURCES_ID = "Administrative Sources";
    private static final String DATASETS_ID = "Data Sets";
    private static final String RESOURCES_ID = "Resources";

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
        final HstRequestContext ctx = request.getRequestContext();
        Publication publication = (Publication) ctx.getContentBean();

        request.setAttribute("publication", publication);
        request.setAttribute("index", getIndex(publication));
    }

    private List<String> getIndex(Publication publication) {
        List<String> index = new ArrayList<>();

        if (!publication.getSummary().isEmpty()) {
            index.add(SUMMARY_ID);
        }

        if (!publication.getKeyFacts().isEmpty()) {
            index.add(KEY_FACTS_ID);
        }

        if (isNotBlank(publication.getAdministrativeSources())) {
            index.add(ADMIN_SOURCES_ID);
        }

        if (isNotEmpty(publication.getDatasets())) {
            index.add(DATASETS_ID);
        }

        if (isNotEmpty(publication.getAttachments())
            || isNotEmpty(publication.getResourceLinks())) {
            index.add(RESOURCES_ID);
        }

        return index;
    }
}
