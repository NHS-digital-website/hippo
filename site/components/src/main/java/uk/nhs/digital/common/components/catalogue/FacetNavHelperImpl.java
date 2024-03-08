package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocumentBean;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.content.beans.standard.HippoFolderBean;

import java.util.*;
import java.util.stream.Collectors;

public class FacetNavHelperImpl implements FacetNavHelper {
    private final HippoFacetNavigationBean facetNav;

    public FacetNavHelperImpl(HippoFacetNavigationBean facetNav) {
        this.facetNav = facetNav;
    }

    public Set<String> getAllTags() {
        List<HippoFolderBean> tagFolders = facetNav.getFolders().stream().findFirst().get().getFolders();
        return tagFolders.stream().map(HippoBean::getDisplayName).collect(Collectors.toSet());
    }

    public List<String> getAllTagsForLink(CatalogueLink link) {
        String title = link.getTitleOfLink();
        return tagsForDocsWithGivenTitle(title);
    }

    private List<String> tagsForDocsWithGivenTitle(String title) {
        List<HippoDocumentBean> documents = facetNav.getResultSet()
                .getDocuments()
                .stream()
                .filter(document -> document.getSingleProperty("website:title") != null)
                .filter(document -> document.getMultipleProperty("hippotaxonomy:keys") != null)
                .collect(Collectors.toList());

        return documents
                .stream()
                .filter(spec -> Objects.equals(spec.getSingleProperty("website:title"), title))
                .map(filteredSpec -> Arrays.stream(filteredSpec.getMultipleProperty("hippotaxonomy:keys")).toArray(String[]::new))
                .flatMap(stream -> Arrays.stream(stream).sequential()).distinct().collect(Collectors.toList());
    }
}
