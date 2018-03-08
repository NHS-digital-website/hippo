package uk.nhs.digital.ps.components;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.content.beans.standard.HippoFolder;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import uk.nhs.digital.ps.beans.Archive;
import uk.nhs.digital.ps.beans.Dataset;
import uk.nhs.digital.ps.beans.LegacyPublication;
import uk.nhs.digital.ps.beans.Publication;
import uk.nhs.digital.ps.beans.Series;

import java.util.List;

public class FolderComponent extends BaseHstComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
        HippoFolder folder = (HippoFolder) request.getRequestContext().getContentBean();

        List<Archive> archivesInFolder = folder.getChildBeans(Archive.class);
        List<Series> seriesInFolder = folder.getChildBeans(Series.class);
        List<Publication> publicationContentInFolder = folder.getChildBeansByName("content", Publication.class);
        List<LegacyPublication> legacyPublicationContentInFolder = folder.getChildBeansByName("content", LegacyPublication.class);

        if (archivesInFolder.size() > 0) {
            ArchiveComponent archiveComponent = new ArchiveComponent();
            archiveComponent.doBeforeRender(request, response);
        } else if (seriesInFolder.size() > 0) {
            SeriesComponent seriesComponent = new SeriesComponent();
            seriesComponent.doBeforeRender(request, response);
        } else if (publicationContentInFolder.size() > 0) {
            PublicationComponent publicationComponent = new PublicationComponent();
            publicationComponent.doBeforeRender(request, response);
        } else if (legacyPublicationContentInFolder.size() > 0) {
            LegacyPublicationComponent legacyPublicationComponent = new LegacyPublicationComponent();
            legacyPublicationComponent.doBeforeRender(request, response);
        } else {
            try {
                request.setAttribute("publications", findAllDocuments(folder));
            } catch (QueryException queryException) {
                throw new HstComponentException(
                    "Exception occurred during folder search.", queryException);
            }
        }
    }

    private HippoBeanIterator findAllDocuments(HippoBean scope) throws QueryException {
        HstQueryBuilder queryBuilder = HstQueryBuilder.create(scope);
        HstQuery hstQuery = queryBuilder
            .ofTypes(Publication.class, Series.class, Dataset.class)
            .limit(20)
            .offset(0)
            .build();

        return hstQuery.execute().getHippoBeans();
    }
}
