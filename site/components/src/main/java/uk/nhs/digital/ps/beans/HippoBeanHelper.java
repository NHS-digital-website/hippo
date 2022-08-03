package uk.nhs.digital.ps.beans;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoFolder;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.taxonomy.api.Category;
import org.onehippo.taxonomy.api.Taxonomy;
import org.onehippo.taxonomy.api.TaxonomyManager;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Static helper for {@linkplain org.hippoecm.hst.content.beans.standard.HippoBean}
 */
public class HippoBeanHelper {

    public static final String PUBLICATION_TAXONOMY = "publication_taxonomy";

    public static boolean isRootFolder(HippoBean folder) {
        HippoBean siteContentBaseBean = RequestContextProvider.get().getSiteContentBaseBean();

        return folder.isSelf(siteContentBaseBean);
    }

    public static List<String> getFullTaxonomyList(HippoBean bean) {
        String[] fullTaxonomy = bean.getMultipleProperty("common:FullTaxonomy");
        if (isEmpty(fullTaxonomy)) {
            return emptyList();
        }

        // Lookup Taxonomy Tree
        TaxonomyManager taxonomyManager = HstServices.getComponentManager().getComponent(TaxonomyManager.class.getName());
        Taxonomy taxonomyTree = taxonomyManager.getTaxonomies().getTaxonomy(PUBLICATION_TAXONOMY);

        return Arrays.stream(fullTaxonomy)
            .map(key -> taxonomyTree.getCategoryByKey(key).getInfo(Locale.UK).getName())
            .collect(Collectors.toList());
    }

    public static List<String> getTaxonomyList(HippoBean bean) {
        String[] fullTaxonomy = bean.getMultipleProperty("common:FullTaxonomy");
        if (isEmpty(fullTaxonomy)) {
            return null;
        }

        return new ArrayList<>(getFinalTaxonomyKeysAndNames(fullTaxonomy).values());
    }

    /**
     * Return distinct collection of taxonomy in the format of [Key, Name]
     */
    public static Map<String, String> getTaxonomyKeysAndNames(String[] keys) {
        Map<String, String> keyNamePairs = new HashMap<String, String>();

        // For each taxonomy tag key, get the name and also include hierarchy context (ancestors)
        if (keys != null) {
            // Lookup Taxonomy Tree
            TaxonomyManager taxonomyManager = HstServices.getComponentManager().getComponent(TaxonomyManager.class.getName());
            Taxonomy taxonomyTree = taxonomyManager.getTaxonomies().getTaxonomy(PUBLICATION_TAXONOMY);

            for (String key : keys) {
                List<Category> ancestors = (List<Category>) taxonomyTree.getCategoryByKey(key).getAncestors();

                // collect the ancestors
                Map<String, String> map = ancestors.stream().distinct()
                    .collect(Collectors.toMap(Category::getKey, category -> category.getInfo(Locale.UK).getName()));

                // add the current node
                map.putIfAbsent(key, taxonomyTree.getCategoryByKey(key).getInfo(Locale.UK).getName());

                // combine with master collection if haven't been collected already
                map.forEach(keyNamePairs::putIfAbsent);
            }
        }

        return keyNamePairs;
    }

    public static Map<String, String> getFinalTaxonomyKeysAndNames(String[] keys) {
        Map<String, String> keyNamePairs = new HashMap<String, String>();

        // For each taxonomy tag key, get the name and also include hierarchy context (ancestors)
        if (keys != null) {
            // Lookup Taxonomy Tree
            TaxonomyManager taxonomyManager = HstServices.getComponentManager().getComponent(TaxonomyManager.class.getName());
            Taxonomy taxonomyTree = taxonomyManager.getTaxonomies().getTaxonomy(PUBLICATION_TAXONOMY);

            for (String key : keys) {
                Map<String, String> map = new HashMap<String, String>();

                // add the current node if it is not a folder
                if (taxonomyTree.getCategoryByKey(key).getChildren().size() == 0) {
                    map.put(key, taxonomyTree.getCategoryByKey(key).getInfo(Locale.UK).getName());

                    // combine with master collection if haven't been collected already
                    map.forEach(keyNamePairs::putIfAbsent);
                }
            }
        }

        return keyNamePairs;
    }

    public static Publication getParentPublication(HippoBean child) {
        HippoFolder folder = (HippoFolder) child.getParentBean();

        while (!HippoBeanHelper.isRootFolder(folder)) {
            Publication publication = Publication.getPublicationInFolder(folder, Publication.class);
            if (publication != null) {
                return publication;
            }

            folder = (HippoFolder) folder.getParentBean();
        }

        return null;
    }
}
