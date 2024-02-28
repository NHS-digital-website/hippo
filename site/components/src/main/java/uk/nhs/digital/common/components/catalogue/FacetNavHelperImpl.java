package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocumentBean;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.content.beans.standard.HippoFolderBean;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.onehippo.cms7.essentials.components.info.EssentialsFacetsComponentInfo;

import java.util.*;
import java.util.stream.Collectors;

public class FacetNavHelperImpl implements FacetNavHelper {
    private final EssentialsFacetsComponentInfo paramInfo;
    private final String queryParam;

    public FacetNavHelperImpl(EssentialsFacetsComponentInfo paramInfo, String queryParam) {
        this.paramInfo = paramInfo;
        this.queryParam = queryParam;
    }

    public Set<String> getAllTags() {
        HippoFacetNavigationBean facetNav = getFacetNav();
        List<HippoFolderBean> tagFolders = facetNav.getFolders().stream().findFirst().get().getFolders();
        return tagFolders.stream().map(HippoBean::getDisplayName).collect(Collectors.toSet());
    }

    public List<String> getAllTagsForLink(CatalogueLink link) {
        String title = link.getTitleOfLink();
        return tagsForDocsWithGivenTitle(title);
    }

    private List<String> tagsForDocsWithGivenTitle(String title) {
        HippoFacetNavigationBean facetNav = getFacetNav();
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

    private HippoFacetNavigationBean getFacetNav() {
        return ContentBeanUtils.getFacetNavigationBean(paramInfo.getFacetPath(), queryParam);
    }
}
