package uk.nhs.digital.ps.migrator.task;

import uk.nhs.digital.ps.migrator.model.hippo.*;
import uk.nhs.digital.ps.migrator.model.nesstar.Catalog;
import uk.nhs.digital.ps.migrator.model.nesstar.NesstarResource;
import uk.nhs.digital.ps.migrator.model.nesstar.PublishingPackage;

import java.util.List;
import java.util.stream.Collectors;

public class ImportableItemsFactory {

    public static Series toSeries(final Folder parentFolder, final String title) {

        return new Series(
            parentFolder,
            "content",
            title
        );
    }

    public static Publication toPublication(final Catalog catalog, final Folder parentFolder) {

        return new Publication(
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

        String nominalDate = convertNominalDate(exportedPublishingPackage.getDate());

        List<NesstarResource> resources = exportedPublishingPackage.getResources();
        List<Attachment> attachments = getAttachments(resources);
        List<ResourceLink> resourceLinks = getResourceLinks(resources);

        // Quick sanity check to make sure we have processed all the resources
        if (resources.size() != attachments.size() + resourceLinks.size()) {
            throw new RuntimeException("Had some resources that we didn't know how to map.");
        }

        return new DataSet(
            parentFolder,
            exportedPublishingPackage.getTitle(),
            exportedPublishingPackage.getTitle(),
            exportedPublishingPackage.getSummary(),
            nominalDate,
            attachments,
            resourceLinks
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

    public static List<ResourceLink> getResourceLinks(List<NesstarResource> resources) {
      return resources.stream()
            .filter(NesstarResource::isLink)
            .map(resource -> new ResourceLink(resource.getTitle(), resource.getUri()))
            .collect(Collectors.toList());
    }

    public static List<Attachment> getAttachments(List<NesstarResource> resources) {
        // Convert to Attachment objects and download the file from the existing website
      return resources.stream()
            .filter(NesstarResource::isAttachment)
            .map(resource -> new Attachment(resource.getTitle(), resource.getUri()))
            .peek(attachment -> attachment.downloadAttachment())
            .collect(Collectors.toList());
    }

    /**
     * The nesstar data only has month and year whereas in hippo we want a date.
     * These mappings have been provided to us as the actual dates in each month that the publications were published
     */
    private static String convertNominalDate(String input) {
        switch (input) {
            case "Mar-12":  return "2012-03-22T09:30:00.000Z";
            case "Dec-13":  return "2013-12-19T09:30:00.000Z";
            case "Feb-14":  return "2014-02-20T09:30:00.000Z";
            case "Mar-14":  return "2014-03-20T09:30:00.000Z";
            case "May-14":  return "2014-05-22T09:30:00.000Z";
            case "Jun-14":  return "2014-06-19T09:30:00.000Z";
            case "Jul-14":  return "2014-07-30T09:30:00.000Z";
            case "Aug-14":  return "2014-08-20T09:30:00.000Z";
            case "Sep-14":  return "2014-09-18T09:30:00.000Z";
            case "Oct-14":  return "2014-10-23T09:30:00.000Z";
            case "Nov-14":  return "2014-11-20T09:30:00.000Z";
            case "Dec-14":  return "2014-12-17T09:30:00.000Z";
            case "Jan-15":  return "2015-01-27T09:30:00.000Z";
            case "Feb-15":  return "2015-02-19T09:30:00.000Z";
            case "Mar-15":  return "2015-03-19T09:30:00.000Z";
            case "Apr-15":  return "2015-04-29T09:30:00.000Z";
            case "May-15":  return "2015-05-19T09:30:00.000Z";
            case "Jun-15":  return "2015-06-25T09:30:00.000Z";
            case "Jul-15":  return "2015-07-29T09:30:00.000Z";
            case "Aug-15":  return "2015-08-19T09:30:00.000Z";
            case "Sep-15":  return "2015-09-22T09:30:00.000Z";
            case "Oct-15":  return "2015-10-28T09:30:00.000Z";
            case "Nov-15":  return "2015-11-19T09:30:00.000Z";
            case "Dec-15":  return "2015-12-17T09:30:00.000Z";
            case "Jan-16":  return "2016-01-27T09:30:00.000Z";
            case "Feb-16":  return "2016-02-25T09:30:00.000Z";
            case "Mar-16":  return "2016-03-23T09:30:00.000Z";
            case "Apr-16":  return "2016-04-21T09:30:00.000Z";
            case "May-16":  return "2016-05-19T09:30:00.000Z";
            case "Jun-16":  return "2016-06-23T09:30:00.000Z";
            case "Jul-16":  return "2016-07-20T09:30:00.000Z";
            case "Aug-16":  return "2016-08-18T09:30:00.000Z";
            case "Sep-16":  return "2016-09-22T09:30:00.000Z";
            case "Oct-16":  return "2016-10-20T09:30:00.000Z";
            case "Nov-16":  return "2016-11-17T09:30:00.000Z";
            case "Dec-16":  return "2016-12-15T09:30:00.000Z";
            case "Jan-17":  return "2017-01-26T09:30:00.000Z";
            case "Feb-17":  return "2017-02-23T09:30:00.000Z";
            case "Mar-17":  return "2017-03-23T09:30:00.000Z";
            case "Apr-17":  return "2017-04-20T09:30:00.000Z";
            case "May-17":  return "2017-05-18T09:30:00.000Z";
            case "Jun-17":  return "2017-06-22T09:30:00.000Z";
            case "Jul-17":  return "2017-07-20T09:30:00.000Z";
            case "Aug-17":  return "2017-08-24T09:30:00.000Z";
            case "Sep-17":  return "2017-09-21T09:30:00.000Z";
            case "Oct-17":  return "2017-10-19T09:30:00.000Z";
            case "Nov-17":  return "2017-11-16T09:30:00.000Z";
            case "Dec-17":  return "2017-12-14T09:30:00.000Z";

            case "TBC":     return "0001-01-01T12:00:00.000Z";

            default: throw new RuntimeException("No mapping found for input date: " + input);
        }
    }
}
