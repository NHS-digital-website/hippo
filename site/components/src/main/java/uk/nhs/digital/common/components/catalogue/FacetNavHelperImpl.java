package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.content.beans.standard.*;

import java.util.*;
import java.util.stream.Collectors;

public class FacetNavHelperImpl implements FacetNavHelper {
    private final HippoResultSetBean resultSet;
    private final Set<String> tagsSet;

    public FacetNavHelperImpl(HippoFacetNavigationBean facetNav) {
        this.resultSet = facetNav.getResultSet();
        this.tagsSet = getTagsFromFacet(facetNav);
    }

    public Set<String> getAllTags() {
        return tagsSet;
    }

    public List<String> getAllTagsForLink(CatalogueLink link) {
        return tagsForDocsWithGivenId(link.getIdOfLink());
    }

    private List<String> tagsForDocsWithGivenId(String id) {
        List<HippoDocumentBean> documents = resultSet
                .getDocuments()
                .stream()
                .filter(document -> document.getSingleProperty("website:title") != null)
                .filter(document -> document.getMultipleProperty("common:FullTaxonomy") != null)
                .collect(Collectors.toList());

        return documents
                .stream()
                .filter(spec -> Objects.equals(spec.getSingleProperty("hippo:uuid"), id))
                .map(filteredSpec -> Arrays.stream(filteredSpec.getMultipleProperty("common:FullTaxonomy")).toArray(String[]::new))
                .flatMap(stream -> Arrays.stream(stream).sequential()).distinct().collect(Collectors.toList());
    }

    private Set<String> getTagsFromFacet(HippoFacetNavigationBean facetNav) {
        List<HippoFolderBean> tagFolders = facetNav.getFolders().stream().findFirst().get().getFolders();
        return tagFolders.stream().map(HippoBean::getDisplayName).collect(Collectors.toSet());
    }
}
