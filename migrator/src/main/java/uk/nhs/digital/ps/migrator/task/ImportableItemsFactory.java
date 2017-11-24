package uk.nhs.digital.ps.migrator.task;

import uk.nhs.digital.ps.migrator.model.nesstar.Catalog;
import uk.nhs.digital.ps.migrator.model.nesstar.PublishingPackage;
import uk.nhs.digital.ps.migrator.model.hippo.Folder;
import uk.nhs.digital.ps.migrator.model.hippo.DataSet;
import uk.nhs.digital.ps.migrator.model.hippo.Series;
import uk.nhs.digital.ps.migrator.model.hippo.Publication;

public class ImportableItemsFactory {

    public static Series toSeries(final Catalog catalog, final Folder parentFolder) {

        return new Series(
            parentFolder, "content",
            catalog.getLabel()
        );
    }

    public static Folder toFolder(final Catalog catalog, final Folder parentFolder) {

        return new Folder(
            parentFolder,
            catalog.getLabel()
        );
    }

    public static DataSet toDataSet(final PublishingPackage exportedPublishingPackage,
                             final Folder parentFolder) {
        return new DataSet(
            parentFolder,
            exportedPublishingPackage.getTitle(),
            exportedPublishingPackage.getTitle()
        );
    }

    public static Folder newFolder(final String name, final Folder parentFolder) {
        return new Folder(
            parentFolder,
            name
        );
    }

    public static Publication newPublication(final String name, final String title, final Folder parentFolder) {
        return new Publication(
            parentFolder,
            name,
            title
        );
    }
}
