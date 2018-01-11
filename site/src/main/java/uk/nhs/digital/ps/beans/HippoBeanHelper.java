package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.taxonomy.api.Category;
import org.onehippo.taxonomy.api.Taxonomy;
import org.onehippo.taxonomy.api.TaxonomyManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.jcr.RepositoryException;

/**
 * Static helper for {@linkplain org.hippoecm.hst.content.beans.standard.HippoBean}
 */
public class HippoBeanHelper {

    public static boolean isRootFolder(HippoBean folder) {
        HippoBean siteContentBaseBean = RequestContextProvider.get().getSiteContentBaseBean();

        return folder.isSelf(siteContentBaseBean);
    }

    public static String getTaxonomyName() throws HstComponentException {
        String taxonomyName;

        try {
            HstRequestContext ctx = RequestContextProvider.get();
            taxonomyName = ctx.getSession().getNode(
                "/hippo:namespaces/publicationsystem/publication/editor:templates/_default_/classifiable")
                .getProperty("essentials-taxonomy-name")
                .getString();
        } catch (RepositoryException repositoryException) {
            throw new HstComponentException(
                "Exception occurred during fetching taxonomy file name.", repositoryException);
        }

        return taxonomyName;
    }

    public static List<List<String>> getTaxonomyList(String[] keys) {
        List<List<String>> taxonomyList = new ArrayList<>();

        // For each taxonomy tag key, get the name and also include hierarchy context (ancestors)
        if (keys != null) {
            // Lookup Taxonomy Tree
            TaxonomyManager taxonomyManager = HstServices.getComponentManager().getComponent(TaxonomyManager.class.getName());
            Taxonomy taxonomyTree = taxonomyManager.getTaxonomies().getTaxonomy(getTaxonomyName());

            for (String key : keys) {
                List<Category> ancestors = (List<Category>) taxonomyTree.getCategoryByKey(key).getAncestors();

                List<String> list = ancestors.stream()
                    .map(category -> category.getInfo(Locale.UK).getName())
                    .collect(Collectors.toList());
                list.add(taxonomyTree.getCategoryByKey(key).getInfo(Locale.UK).getName());
                taxonomyList.add(list);
            }
        }

        return taxonomyList;
    }
}
