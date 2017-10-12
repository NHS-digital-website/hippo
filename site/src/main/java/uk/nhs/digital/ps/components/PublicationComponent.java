package uk.nhs.digital.ps.components;

import org.onehippo.taxonomy.api.Category;
import org.onehippo.taxonomy.api.Taxonomy;
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

import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PublicationComponent extends BaseHstComponent {

    public static final Logger log = LoggerFactory.getLogger(PublicationComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
        final HstRequestContext ctx = request.getRequestContext();
        final String DELIMITER = " => ";


        Publication publication = (Publication) ctx.getContentBean();

        if (publication != null) {
            request.setAttribute("document", publication);
        }

        String taxonomyName = "";
        try {
            taxonomyName = ctx.getSession().getNode("/hippo:namespaces/publicationsystem/publication/editor:templates/_default_/classifiable").getProperty("essentials-taxonomy-name").getString();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

        // For each taxonomy tag key, get the name and also include hierarchy context (ancestors)
        if (publication.getKeys() != null) {

            // Lookup Taxonomy Tree
            TaxonomyManager taxonomyManager = HstServices.getComponentManager().getComponent(TaxonomyManager.class.getName());
            Taxonomy taxonomyTree = taxonomyManager.getTaxonomies().getTaxonomy(taxonomyName);
            List<List<String>> taxonomyList = new ArrayList<>();

            for (String key : publication.getKeys()) {
                List<Category> ancestors = (List<Category>) taxonomyTree.getCategoryByKey(key).getAncestors();

                List<String> list = ancestors.stream()
                    .map(category -> category.getInfo("en").getName())
                    .collect(Collectors.toList());
                list.add(taxonomyTree.getCategoryByKey(key).getInfo("en").getName());
                taxonomyList.add(list);
            }

            request.setAttribute("taxonomyList", taxonomyList);
        }
    }
}

