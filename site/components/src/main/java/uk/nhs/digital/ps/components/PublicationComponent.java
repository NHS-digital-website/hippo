package uk.nhs.digital.ps.components;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.ContentRewriterComponent;
import uk.nhs.digital.ps.beans.Archive;
import uk.nhs.digital.ps.beans.Publication;
import uk.nhs.digital.ps.beans.Series;

import java.util.ArrayList;
import java.util.List;

public class PublicationComponent extends ContentRewriterComponent {

    private static final String SUMMARY_ID = "Summary";
    private static final String HIGHLIGHTS_ID = "Highlights";
    private static final String KEY_FACTS_ID = "Key facts";
    private static final String ADMIN_SOURCES_ID = "Administrative sources";
    private static final String DATASETS_ID = "Data sets";
    private static final String RESOURCES_ID = "Resources";
    private static final String SUPPLEMENTARY_INFO_ID = "Supplementary information requests";

    private static final Logger LOG = LoggerFactory.getLogger(PublicationComponent.class);

    private final PageSectionGrouper pageSectionGrouper = new PageSectionGrouper();

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
        final HstRequestContext ctx = request.getRequestContext();
        Publication publication = (Publication) ctx.getContentBean();

        request.setAttribute("publication", publication);
        request.setAttribute("index", getIndex(publication));
        request.setAttribute("keyFactImageSections", pageSectionGrouper.groupSections(publication.getKeyFactImages()));
    }

    private List<String> getIndex(Publication publication) {
        List<String> index = new ArrayList<>();

        // No contents for upcoming publications
        if (!publication.isPubliclyAccessible()) {
            return index;
        }

        if (!publication.getSummary().isEmpty()) {
            index.add(SUMMARY_ID);
        }

        if (!publication.getSections().isEmpty()) {
            index.add(HIGHLIGHTS_ID);
        }

        if (!publication.getKeyFacts().isEmpty()) {
            index.add(KEY_FACTS_ID);
        }

        HippoBean seriesCollection = publication.getParentSeriesCollectionDocument();
        if (isSeriesAndAdminSourcesNotBlank(seriesCollection) || isArchiveAndAdminSourcesNotBlank(seriesCollection)) {
            index.add(ADMIN_SOURCES_ID);
        }

        if (isNotEmpty(publication.getDatasets())) {
            index.add(DATASETS_ID);
        }

        if (isNotEmpty(publication.getAttachments())
            || isNotEmpty(publication.getResourceLinks())) {
            index.add(RESOURCES_ID);
        }

        try {
            if (isNotEmpty(publication.getSupplementaryInformation())) {
                index.add(SUPPLEMENTARY_INFO_ID);
            }
        } catch (QueryException e) {
            LOG.error("Error getting related supplementary info", e);
        }

        return index;
    }

    private boolean isSeriesAndAdminSourcesNotBlank(Object series) {
        return series instanceof Series && isNotBlank(((Series) series).getAdministrativeSources());
    }

    private boolean isArchiveAndAdminSourcesNotBlank(Object archive) {
        return archive instanceof Archive && isNotBlank(((Archive) archive).getAdministrativeSources());
    }
}
