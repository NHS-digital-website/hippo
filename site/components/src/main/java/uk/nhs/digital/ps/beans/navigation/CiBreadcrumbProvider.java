package uk.nhs.digital.ps.beans.navigation;

import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.constraint;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.or;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.builder.Constraint;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoFolder;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.onehippo.forge.breadcrumb.om.BreadcrumbItem;

import uk.nhs.digital.ps.beans.*;

import java.util.*;

/**
 * <p>
 * Default Hippo Breadcrumb plugin uses menu/folder structure to generate breadcrumbs,
 * but this doesn't fit for clinical indicator content (ciLanding, publication,
 * series, datasets) as they have a very specific setup in the CMS.
 * </p>
 */
public class CiBreadcrumbProvider  {

    private static final String SEPARATOR = "/";
    private CiLanding ciLandingBean = null;
    private boolean isClinicalIndicator = false;
    private final HstRequestContext ctx;
    private final HippoBean currentDocumentBean;

    public CiBreadcrumbProvider(final HstRequest request) {
        ctx = request.getRequestContext();
        currentDocumentBean = request.getRequestContext().getContentBean();
    }

    public CiBreadcrumb getBreadcrumb() {

        if (currentDocumentBean == null || currentDocumentBean instanceof HippoFolder) {
            return null;
        }

        loadCiLandingBean();

        List<BreadcrumbItem> ciBreadcrumbItems = new ArrayList<>();

        if (currentDocumentBean instanceof CiLanding) {
            isClinicalIndicator = true;
        } else {
            addCiLandingPageBreadcrumbItem(ciBreadcrumbItems);

            // If Publication or dataset, find parent links for breadcrumb trail
            // (note: not required for series since series documents are
            // already top level breadcrumb)
            if (currentDocumentBean instanceof Publication) {
                addPublicationBreadcrumbItem(ciBreadcrumbItems);
            } else if (currentDocumentBean instanceof Dataset) {
                addDatasetBreadcrumbItem(ciBreadcrumbItems);
            }
        }

        // finally, add navigation for THIS document
        ciBreadcrumbItems.add(createBreadcrumbItem(ctx, currentDocumentBean));

        return new CiBreadcrumb(ciBreadcrumbItems, SEPARATOR, isClinicalIndicator);
    }

    private BreadcrumbItem createBreadcrumbItem(final HstRequestContext ctx, final HippoBean bean) {
        return new BreadcrumbItem(
            ctx.getHstLinkCreator().create(bean, ctx),
            ((BaseDocument) bean).getTitle());
    }

    /**
     * <p>
     * Query the repository to see if this document sits under one of the Clinical Indicator
     * folders, and if so load in the CiLanding bean (which is used to create the breadcrumb).
     * The CiLanding document has a property called urlNameOfContentFolder which links
     * the CiLanding page to the appropriate content folder
     * </p>
     */
    private void loadCiLandingBean() {

        HstQueryBuilder queryBuilder = HstQueryBuilder.create(RequestContextProvider.get().getSiteContentBaseBean());
        final HstQueryResult hstQueryResult;

        Constraint[] constraints = Arrays.stream(currentDocumentBean.getPath().split("/"))
            .map((pathSegment) -> constraint("publicationsystem:urlNameOfContentFolder").equalTo(pathSegment))
            .toArray(Constraint[]::new);

        final HstQuery query = queryBuilder
            .ofTypes(CiLanding.class)
            .where(or(
                constraints
            ))
            .build();

        try {
            hstQueryResult = query.execute();
        } catch (QueryException queryException) {
            throw new HstComponentException(
                "Exception occurred during ci folder search.", queryException);
        }

        if (hstQueryResult.getHippoBeans().hasNext()) {
            isClinicalIndicator = true;
            ciLandingBean = (CiLanding) hstQueryResult.getHippoBeans().nextHippoBean();
        }
    }

    private void addCiLandingPageBreadcrumbItem(List<BreadcrumbItem> ciBreadcrumbItems) {
        if (ciLandingBean != null) {
            ciBreadcrumbItems.add(createBreadcrumbItem(ctx, ciLandingBean));
        }
    }

    private void addPublicationBreadcrumbItem(List<BreadcrumbItem> ciBreadcrumbItems) {
        // Is publication part of archive/series?
        HippoBean publicationParent =  ((Publication) currentDocumentBean).getParentDocument();
        if (publicationParent != null) {

            // Create Archive/Series navigation
            BaseDocument seriesOrArchiveDocument = (BaseDocument) publicationParent;
            ciBreadcrumbItems.add(createBreadcrumbItem(ctx, seriesOrArchiveDocument));
        }
    }

    private void addDatasetBreadcrumbItem(List<BreadcrumbItem> ciBreadcrumbItems) {
        // get Dataset's parent publication
        HippoBean datasetParent =  ((Dataset) currentDocumentBean).getParentPublication();
        if (datasetParent != null) {

            Publication publication = (Publication) datasetParent;

            // Is publication part of archive/series?
            HippoBean publicationParent =  publication.getParentDocument();
            if (publicationParent != null) {

                // Create Archive/Series navigation
                BaseDocument seriesOrArchiveDocument = (BaseDocument) publicationParent;
                ciBreadcrumbItems.add(createBreadcrumbItem(ctx, seriesOrArchiveDocument));
            }

            // Create Publication navigation
            ciBreadcrumbItems.add(createBreadcrumbItem(ctx, publication));
        }
    }
}
