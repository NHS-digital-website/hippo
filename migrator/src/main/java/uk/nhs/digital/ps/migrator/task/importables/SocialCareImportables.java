package uk.nhs.digital.ps.migrator.task.importables;

import uk.nhs.digital.ps.migrator.model.hippo.Folder;
import uk.nhs.digital.ps.migrator.model.hippo.HippoImportableItem;
import uk.nhs.digital.ps.migrator.model.hippo.Publication;
import uk.nhs.digital.ps.migrator.model.nesstar.Catalog;
import uk.nhs.digital.ps.migrator.model.nesstar.CatalogStructure;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static uk.nhs.digital.ps.migrator.task.ImportableItemsFactory.*;
import static uk.nhs.digital.ps.migrator.task.ImportableItemsFactory.toDataSet;
import static uk.nhs.digital.ps.migrator.task.ImportableItemsFactory.toFolder;

public class SocialCareImportables {

    static public List<HippoImportableItem> create(final CatalogStructure catalogStructure, final Folder ciRootFolder) {

        // Target CMS structure:
        //
        // A)  Adult Social Care Outcomes Framework (ASCOF)     FOLDER
        // B)  ASCOF publication 'content' document             PUBLICATION
        // C)    folders per node                               FOLDER
        // D)      DataSet documents                            DATASET

        // A)
        final Catalog rootCatalog = catalogStructure.findCatalogByLabel("Adult Social Care Outcomes Framework (ASCOF)");
        final Folder rootFolder = toFolder(rootCatalog, ciRootFolder);

        // B)
        final Publication publication = toPublication(rootCatalog, rootFolder);

        // C)
        final List<Catalog> domainCatalogs = rootCatalog.getChildCatalogs();
        final List<HippoImportableItem> domainsWithDatasets = domainCatalogs.stream()
            .flatMap(domainCatalog -> {
                final Folder domainFolder = toFolder(domainCatalog, rootFolder);

                // D)
                return Stream.concat(
                    Stream.of(domainFolder),
                    domainCatalog.findPublishingPackages().stream().map(domainPublishingPackage ->
                        toDataSet(domainPublishingPackage, domainFolder)
                    )
                );
            }).collect(toList());

        final List<HippoImportableItem> importableItems = new ArrayList<>();
        importableItems.add(rootFolder);
        importableItems.add(publication);
        importableItems.addAll(domainsWithDatasets);

        return importableItems;
    }
}
