package uk.nhs.digital.ps.migrator.task.importables;

import uk.nhs.digital.ps.migrator.model.hippo.Folder;
import uk.nhs.digital.ps.migrator.model.hippo.HippoImportableItem;
import uk.nhs.digital.ps.migrator.model.hippo.Publication;
import uk.nhs.digital.ps.migrator.model.hippo.Series;
import uk.nhs.digital.ps.migrator.model.nesstar.Catalog;
import uk.nhs.digital.ps.migrator.model.nesstar.CatalogStructure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static uk.nhs.digital.ps.migrator.task.ImportableItemsFactory.*;

public class CcgImportables {

    static public List<HippoImportableItem> create(final CatalogStructure catalogStructure, final Folder ciRootFolder) {

        // Target CMS structure:
        //
        // A)  CCG Outcomes Indicator Set                           FOLDER
        // B)    Current                                            FOLDER
        // C)      content                                          PUBLICATION
        // D)        folders per node (domain x)                    FOLDER
        // E)          DataSet documents from subfolders            DATASET
        // F)    Archive                                            FOLDER
        // G)      content                                          SERIES
        // H)      2016                                             PUBLICATION (to be created manually by editors)

        // A)
        final Catalog ccgRootCatalog = catalogStructure.findCatalogByLabel("CCG Outcomes Indicator Set");
        final Folder ccgRootFolder = toFolder(ccgRootCatalog, ciRootFolder);

        // B)
        final String currentQuarterFolderName = "Current";
        final Folder quarterlyPublicationFolder = newFolder(currentQuarterFolderName, ccgRootFolder);

        // C)
        final Publication quarterlyPublication = newPublication("content", ccgRootCatalog.getLabel(), quarterlyPublicationFolder);

        // D)
        // There is only one level of Domains under CCG so it's enough to just iterate over them rather than walk a tree
        final List<Catalog> domainCatalogs = ccgRootCatalog.getChildCatalogs();

        final List<HippoImportableItem> domainsWithDatasets = domainCatalogs.stream()
            .flatMap(domainCatalog -> {
                final Folder domainFolder = toFolder(domainCatalog, quarterlyPublicationFolder);

                return Stream.concat(
                    Stream.of(domainFolder),
                    // E)
                    domainCatalog.findPublishingPackages().stream().map(domainPublishingPackage ->
                        toDataSet(domainPublishingPackage, domainFolder)
                    )
                );

            }).collect(toList());

        // F)
        final String archiveFolderName = "archive";
        final Folder archiveFolder = newFolder(archiveFolderName, ccgRootFolder);
        archiveFolder.setLocalizedName("Archive");

        // G
        final Series series = toSeries(archiveFolder, "Archive CCG Outcomes Indicators");

        final List<HippoImportableItem> importableItems = new ArrayList<>();
        importableItems.add(ccgRootFolder);
        importableItems.add(quarterlyPublicationFolder);
        importableItems.add(quarterlyPublication);
        importableItems.addAll(domainsWithDatasets);
        importableItems.add(archiveFolder);
        importableItems.add(series);

        return importableItems;
    }
}
