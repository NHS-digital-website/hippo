package uk.nhs.digital.ps.components;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoFolder;
import org.onehippo.taxonomy.api.Category;
import org.onehippo.taxonomy.api.Taxonomy;
import uk.nhs.digital.ps.beans.Dataset;
import uk.nhs.digital.ps.beans.Publication;
import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.taxonomy.api.TaxonomyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.beans.Series;

import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class PublicationComponent extends BaseHstComponent {

    private static final Logger log = LoggerFactory.getLogger(PublicationComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
        final HstRequestContext ctx = request.getRequestContext();
        Publication publication = getPublication(ctx);

        request.setAttribute("publication", publication);
        request.setAttribute("taxonomyList", getTaxonomyList(ctx, publication));
        request.setAttribute("parentSeries", getParentSeries(ctx, publication));
        request.setAttribute("datasets", getDatasets(publication));
    }

    private Publication getPublication(HstRequestContext ctx) throws HstComponentException {
        HippoBean content = ctx.getContentBean();

        if (content.getClass().equals(Publication.class)) {
            return (Publication) content;
        }
        if (content.getClass().equals(HippoFolder.class)) {
            return Publication.getPublicationInFolder((HippoFolder)content);
        }

        log.warn("Cannot find Publication document for: {}", content.getPath());
        throw new HstComponentException("Cannot find Publication document based on request content");
    }

    private List getTaxonomyList(HstRequestContext ctx, Publication publication) {
        List<List<String>> taxonomyList = new ArrayList<>();

        // For each taxonomy tag key, get the name and also include hierarchy context (ancestors)
        if (publication.getKeys() != null) {

            // Lookup Taxonomy Tree
            TaxonomyManager taxonomyManager = HstServices.getComponentManager().getComponent(TaxonomyManager.class.getName());
            Taxonomy taxonomyTree = taxonomyManager.getTaxonomies().getTaxonomy(getTaxonomyName(ctx));


            for (String key : publication.getKeys()) {
                List<Category> ancestors = (List<Category>) taxonomyTree.getCategoryByKey(key).getAncestors();

                List<String> list = ancestors.stream()
                    .map(category -> category.getInfo("en").getName())
                    .collect(Collectors.toList());
                list.add(taxonomyTree.getCategoryByKey(key).getInfo("en").getName());
                taxonomyList.add(list);
            }
        }

        return taxonomyList;
    }

    private String getTaxonomyName(HstRequestContext ctx) throws HstComponentException {
        String taxonomyName;

        try {
            taxonomyName = ctx.getSession().getNode(
                "/hippo:namespaces/publicationsystem/publication/editor:templates/_default_/classifiable")
                .getProperty("essentials-taxonomy-name")
                .getString();
        } catch (RepositoryException e) {
            throw new HstComponentException(
                "Exception occurred during fetching taxonomy file name.", e);
        }

        return taxonomyName;
    }

    private Series getParentSeries(HstRequestContext ctx, Publication publication) {
        Series seriesBean = null;

        HippoBean folder = publication.getParentBean();
        while (!isRootFolder(folder, ctx)) {
            Iterator<Series> iterator = folder.getChildBeans(Series.class).iterator();
            if (iterator.hasNext()) {
                seriesBean = iterator.next();
                break;
            } else {
                folder = folder.getParentBean();
            }
        }

        return seriesBean;
    }

    private List<Dataset> getDatasets(Publication publication) {
        return publication.getParentBean().getChildBeans(Dataset.class);
    }

    private boolean isRootFolder(HippoBean folder, HstRequestContext ctx) {
        return folder.isSelf(ctx.getSiteContentBaseBean());
    }
}
