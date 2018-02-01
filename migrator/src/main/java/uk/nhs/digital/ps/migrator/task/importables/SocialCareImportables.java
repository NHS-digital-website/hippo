package uk.nhs.digital.ps.migrator.task.importables;

import static java.util.stream.Collectors.toList;

import uk.nhs.digital.ps.migrator.model.hippo.*;
import uk.nhs.digital.ps.migrator.model.nesstar.Catalog;
import uk.nhs.digital.ps.migrator.model.nesstar.CatalogStructure;
import uk.nhs.digital.ps.migrator.task.NesstarImportableItemsFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SocialCareImportables {

    private final NesstarImportableItemsFactory nesstarImportableItemsFactory;

    public SocialCareImportables(final NesstarImportableItemsFactory nesstarImportableItemsFactory) {
        this.nesstarImportableItemsFactory = nesstarImportableItemsFactory;
    }

    public List<HippoImportableItem> create(final CatalogStructure catalogStructure, final Folder ciRootFolder) {

        // Target CMS structure:
        //
        // A)  Adult Social Care Outcomes Framework (ASCOF)         FOLDER
        // B)    Current                                            FOLDER
        // C)      content                                          PUBLICATION
        // D)        folders per node (domain x)                    FOLDER
        // E)          DataSet documents from subfolders            DATASET
        // F)    Archive                                            FOLDER
        // G)      content                                          SERIES
        // H)      2016                                             PUBLICATION (to be created manually by authors)

        // A)
        final Catalog rootCatalog = catalogStructure.findCatalogByLabel("Adult Social Care Outcomes Framework (ASCOF)");
        final Folder rootFolder = nesstarImportableItemsFactory.toFolder(ciRootFolder, rootCatalog);

        // B)
        final Folder currentPublicationFolder = nesstarImportableItemsFactory.newFolder(rootFolder, "Current");

        // D)
        final List<Catalog> domainCatalogs = rootCatalog.getChildCatalogs();

        final List<HippoImportableItem> domainsWithDatasets = domainCatalogs.stream()
            .flatMap(domainCatalog -> {
                final Folder domainFolder = nesstarImportableItemsFactory.toFolder(currentPublicationFolder, domainCatalog);

                return Stream.concat(
                    Stream.of(domainFolder),
                    // E)
                    domainCatalog.findPublishingPackages().stream().map(domainPublishingPackage ->
                        nesstarImportableItemsFactory.toDataSet(domainFolder, domainPublishingPackage)
                    )
                );

            }).collect(toList());

        // C)
        final Publication currentPublication = nesstarImportableItemsFactory.newPublication(
            currentPublicationFolder,
            "content",
            rootFolder.getLocalizedName(),
            domainsWithDatasets);

        // F)
        final Folder archiveFolder = nesstarImportableItemsFactory.newFolder(rootFolder, "Archive");

        // G
        final Series series = nesstarImportableItemsFactory.newArchivedSeries(archiveFolder, rootFolder.getLocalizedName());

        final List<HippoImportableItem> importableItems = new ArrayList<>();
        importableItems.add(rootFolder);
        importableItems.add(currentPublicationFolder);
        importableItems.add(currentPublication);
        importableItems.addAll(domainsWithDatasets);
        importableItems.add(archiveFolder);
        importableItems.add(series);

        return importableItems;
    }

}
