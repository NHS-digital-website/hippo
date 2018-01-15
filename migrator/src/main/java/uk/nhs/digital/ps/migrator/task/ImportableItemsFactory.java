package uk.nhs.digital.ps.migrator.task;

import org.slf4j.Logger;
import uk.nhs.digital.ps.migrator.config.ExecutionParameters;
import uk.nhs.digital.ps.migrator.model.hippo.*;
import uk.nhs.digital.ps.migrator.model.nesstar.Catalog;
import uk.nhs.digital.ps.migrator.model.nesstar.NesstarResource;
import uk.nhs.digital.ps.migrator.model.nesstar.PublishingPackage;
import uk.nhs.digital.ps.migrator.report.MigrationReport;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.text.MessageFormat.format;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.slf4j.LoggerFactory.getLogger;
import static uk.nhs.digital.ps.migrator.report.IncidentType.*;

public class ImportableItemsFactory {

    private final static Logger log = getLogger(ImportableItemsFactory.class);

    private final ExecutionParameters executionParameters;
    private final MigrationReport migrationReport;
    private final TaxonomyMigrator taxonomyMigrator;

    public ImportableItemsFactory(final ExecutionParameters executionParameters,
                                  final MigrationReport migrationReport,
                                  final TaxonomyMigrator taxonomyMigrator) {
        this.executionParameters = executionParameters;
        this.migrationReport = migrationReport;
        this.taxonomyMigrator = taxonomyMigrator;
    }

    public Series newSeries(final Folder parentFolder, final String title) {

        return new Series(
            parentFolder,
            "content",
            title
        );
    }

    public Publication toPublication(final Folder parentFolder, final Catalog catalog) {

        return new Publication(
            parentFolder, "content",
            catalog.getLabel()
        );
    }

    public Folder toFolder(final Folder parentFolder, final Catalog catalog) {

        return new Folder(
            parentFolder,
            catalog.getLabel()
        );
    }

    public DataSet toDataSet(final Folder parentFolder, final PublishingPackage exportedPublishingPackage) {

        final String pCode = exportedPublishingPackage.getUniqueIdentifier();

        try {
            String nominalDate = convertImportedDate(exportedPublishingPackage.currentVersionUploadedDate(), pCode);
            String nextPublicationDate = convertImportedDate(exportedPublishingPackage.nextVersionDueDate(), pCode);

            List<NesstarResource> resources = exportedPublishingPackage.getResources();

            List<Attachment> attachments = getAttachments(exportedPublishingPackage);
            attachments = filterAttachments(pCode, attachments, isWithinCompendiumStructure(parentFolder));

            List<ResourceLink> resourceLinks = getResourceLinks(exportedPublishingPackage.getUniqueIdentifier(), resources);
            resourceLinks = filterResourceLinks(pCode, resourceLinks, isWithinCompendiumStructure(parentFolder));

            List<String> taxonomyKeys = taxonomyMigrator.getTaxonomyKeys(exportedPublishingPackage);

            // Quick sanity check to make sure we have processed all the resources
            if (resources.stream().anyMatch(r -> !r.isLink() && !r.isAttachment())) {
                throw new RuntimeException("Had some resources that we didn't know how to map.");
            }

            final String summary = formatDatasetSummary(
                exportedPublishingPackage.getSummary(),
                pCode
            );

            return new DataSet(
                parentFolder,
                pCode,
                exportedPublishingPackage.getTitle(),
                exportedPublishingPackage.getTitle(),
                summary,
                nominalDate,
                nextPublicationDate,
                attachments,
                resourceLinks,
                String.join("\", \"", taxonomyKeys)
            );
        } catch (Exception e) {
            migrationReport.report(pCode, DATASET_CONVERSION_ERROR, e.getMessage());
            migrationReport.logError(e, "Failed to convert dataset " + pCode);

            return null;
        }
    }

    public Folder newFolder(final Folder parentFolder, final String name) {
        return new Folder(
            parentFolder,
            name
        );
    }

    public Publication newPublication(final Folder parentFolder, final String name, final String title) {
        return new Publication(
            parentFolder,
            name,
            title
        );
    }

    public List<ResourceLink> getResourceLinks(final String pCode, List<NesstarResource> resources) {
      return resources.stream()
            .filter(NesstarResource::isLink)
            .map(resource -> new ResourceLink(resource.getTitle(), resource.getUri()))
            .collect(toList());
    }

    public List<Attachment> getAttachments(PublishingPackage publishingPackage) {
        List<NesstarResource> resources = publishingPackage.getResources();
        // Convert to Attachment objects and download the file from the existing website
      return resources.stream()
            .filter(NesstarResource::isAttachment)
            .map(resource -> new Attachment(
                executionParameters.getNesstarAttachmentDownloadDir(),
                resource.getTitle(),
                resource.getUri(),
                migrationReport))
            .filter(attachment -> attachment.download(publishingPackage))
            .collect(toList());
    }

    /**
     * @return Curated list of attachments that excludes those that should be ignored
     * (excluded: PDF files within Compendium space).
     */
    private List<Attachment> filterAttachments(final String pCode,
                                               final List<Attachment> attachments,
                                               final boolean isWithinCompendiumStructure
    ) {
        final Predicate<Attachment> isCompendiumPdf = attachment -> isWithinCompendiumStructure
            && attachment.getMimeType().equalsIgnoreCase("application/pdf");

        return attachments.stream()
            .peek(attachment -> {
                if (isCompendiumPdf.test(attachment)) {
                    log.info("{}: ignoring attachment {}", pCode, attachment);
                }
            })
            .filter(attachment -> isCompendiumPdf.negate().test(attachment))
            .collect(toList());
    }

    /**
     * @return Curated list of links that excludes those that should be ignored
     * (excluded: links with given marker text within Compendium space).
     */
    private List<ResourceLink> filterResourceLinks(final String pCode,
                                                   final List<ResourceLink> resourceLinks,
                                                   final boolean isWithinCompendiumStructure
    ) {
        final Predicate<ResourceLink> containsMarkerText = resourceLink ->
            containsIgnoreCase(resourceLink.getName(), "earlier data may be available")
                || containsIgnoreCase(resourceLink.getName(), "contact us");


        return resourceLinks.stream()
            // only report on links to be filtered out that come from outside Compendium as these would be unexpected to see
            .peek(resourceLink -> {
                if (!isWithinCompendiumStructure && containsMarkerText.test(resourceLink)) {
                    migrationReport.report(pCode, RESOURCE_LINK_FILTERED_OUT, resourceLink.getName() + " | " + resourceLink.getUri());
                }
            })
            .peek(resourceLink -> {
                // remove all matching links - Compendium or not
                if (containsMarkerText.test(resourceLink)) {
                    log.info("{}: ignoring link: {}", pCode, resourceLink);
                }
            })
            .filter(resourceLink -> containsMarkerText.negate().test(resourceLink))
            .collect(toList());
    }

    private boolean isWithinCompendiumStructure(final Folder folder) {
        return folder.getJcrPath().contains("compendium-of-population-health-indicators");
    }

    /**
     * The nesstar data only has month and year whereas in hippo we want a date.
     * These mappings have been provided to us as the actual dates in each month that the publications were published
     */
    private String convertImportedDate(final String rawInputDate, final String pCode) {
        String input = rawInputDate;

        switch (rawInputDate) {
            case "Nov/Dec 2018":                             input = "Dec-18"; break;
            case "April 2016 (contextual information only)": input = "Apr-16"; break;
            default: // no-op
        }
        if (!input.equals(rawInputDate)) {
            migrationReport.report(pCode, DATE_WITH_EXTRA_TEXT, rawInputDate + " | " + input);
        }

        switch (input) {
            case "Jun-07":    return "2007-06-21T09:30:00.000Z";

            case "Jan-09":    return "2009-01-29T09:30:00.000Z";
            case "Nov-09":    return "2009-11-19T09:30:00.000Z";
            case "Dec-09":    return "2009-12-17T09:30:00.000Z";

            case "Aug-10":    return "2010-08-26T09:30:00.000Z";

            case "Jan-11":    return "2011-01-27T09:30:00.000Z";
            case "Jun-11":    return "2011-06-23T09:30:00.000Z";
            case "Oct-11":    return "2011-10-27T09:30:00.000Z";

            case "Jan-12":    return "2012-01-26T09:30:00.000Z";
            case "Feb-12":    return "2012-02-23T09:30:00.000Z";
            case "Mar-12":    return "2012-03-22T09:30:00.000Z";
            case "Sep-12":    return "2012-09-27T09:30:00.000Z";
            case "Dec-12":    return "2012-12-20T09:30:00.000Z";

            case "Jun-13":    return "2013-06-27T09:30:00.000Z";
            case "Apr-13":    return "2013-04-25T09:30:00.000Z";
            case "May-13":    return "2013-05-23T09:30:00.000Z";
            case "Sep-13":    return "2013-09-26T09:30:00.000Z";
            case "Dec-13":    return "2013-12-19T09:30:00.000Z";

            case "Jan-14":    return "2014-01-30T09:30:00.000Z";
            case "Feb-14":    return "2014-02-20T09:30:00.000Z";
            case "Mar-14":    return "2014-03-20T09:30:00.000Z";
            case "May-14":    return "2014-05-22T09:30:00.000Z";
            case "Jun-14":    return "2014-06-19T09:30:00.000Z";
            case "Jul-14":    return "2014-07-30T09:30:00.000Z";
            case "Aug-14":    return "2014-08-20T09:30:00.000Z";
            case "Sep-14":    return "2014-09-18T09:30:00.000Z";
            case "Oct-14":    return "2014-10-23T09:30:00.000Z";
            case "Nov-14":    return "2014-11-20T09:30:00.000Z";
            case "Dec-14":    return "2014-12-17T09:30:00.000Z";

            case "Jan-15":    return "2015-01-27T09:30:00.000Z";
            case "Feb-15":
            case "Feb-2015":  return "2015-02-19T09:30:00.000Z";
            case "Mar-15":    return "2015-03-19T09:30:00.000Z";
            case "Apr-15":    return "2015-04-29T09:30:00.000Z";
            case "May-15":    return "2015-05-19T09:30:00.000Z";
            case "Jun-15":    return "2015-06-25T09:30:00.000Z";
            case "Jul-15":    return "2015-07-29T09:30:00.000Z";
            case "Aug-15":    return "2015-08-19T09:30:00.000Z";
            case "Sep-15":    return "2015-09-22T09:30:00.000Z";
            case "Oct-15":    return "2015-10-28T09:30:00.000Z";
            case "Nov-15":    return "2015-11-19T09:30:00.000Z";
            case "Dec-15":
            case "Dec-2015":  return "2015-12-17T09:30:00.000Z";

            case "Jan-16":
            case "Jan-2016":  return "2016-01-27T09:30:00.000Z";
            case "Feb-16":    return "2016-02-25T09:30:00.000Z";
            case "Mar-16":    return "2016-03-23T09:30:00.000Z";
            case "Apr-16":    return "2016-04-21T09:30:00.000Z";
            case "May-16":    return "2016-05-19T09:30:00.000Z";
            case "Jun-16":    return "2016-06-23T09:30:00.000Z";
            case "Jul-16":    return "2016-07-20T09:30:00.000Z";
            case "Aug-16":    return "2016-08-18T09:30:00.000Z";
            case "Sep-16":    return "2016-09-22T09:30:00.000Z";
            case "Oct-16":    return "2016-10-20T09:30:00.000Z";
            case "Nov-16":    return "2016-11-17T09:30:00.000Z";
            case "Dec-16":    return "2016-12-15T09:30:00.000Z";

            case "Jan-17":    return "2017-01-26T09:30:00.000Z";
            case "Feb-17":    return "2017-02-23T09:30:00.000Z";
            case "Mar-17":    return "2017-03-23T09:30:00.000Z";
            case "Apr-17":    return "2017-04-20T09:30:00.000Z";
            case "May-17":    return "2017-05-18T09:30:00.000Z";
            case "Jun-17":    return "2017-06-22T09:30:00.000Z";
            case "Jul-17":    return "2017-07-20T09:30:00.000Z";
            case "Aug-17":    return "2017-08-24T09:30:00.000Z";
            case "Sep-17":    return "2017-09-21T09:30:00.000Z";
            case "Oct-17":    return "2017-10-19T09:30:00.000Z";
            case "Nov-17":    return "2017-11-16T09:30:00.000Z";
            case "Dec-17":    return "2017-12-14T09:30:00.000Z";


            case "Jan-18":    return "2018-01-25T09:30:00.000Z";
            case "Feb-18":    return "2018-02-22T09:30:00.000Z";
            case "Mar-18":    return "2018-03-22T09:30:00.000Z";
            case "Apr-18":    return "2018-04-19T09:30:00.000Z";
            case "May-18":    return "2018-05-17T09:30:00.000Z";
            case "Jun-18":    return "2018-06-21T09:30:00.000Z";
            case "Jul-18":    return "2018-07-19T09:30:00.000Z";
            case "Aug-18":    return "2018-08-23T09:30:00.000Z";
            case "Sep-18":    return "2018-09-20T09:30:00.000Z";
            case "Oct-18":    return "2018-10-18T09:30:00.000Z";
            case "Nov-18":    return "2018-11-15T09:30:00.000Z";
            case "Dec-18":    return "2018-12-13T09:30:00.000Z";

            default:
                migrationReport.report(pCode, NO_DATE_MAPPING, input);

                return "0001-01-01T12:00:00.000Z";
        }
    }

    /**
     * <p>
     *     Formats summary, validates for constructs we cannot handle (HTML) and reports for manual interventions where
     *     necessary.
     * </p><p>
     *     Hyperlinks are reported on so that they can be added manually.
     * </p><p>
     *     Presence of HTML tags is reported for manual fixing.
     *     So far no HTML tags were spotted in Datasets' summaries so we make no attempt to strip them; instead we flag
     *     offending Dataset so that in the unlikely event of a new showing up shortly before migration it can be fixed
     *     lmanually. It's unlikely that at this point any would be added, much less more than one.
     * </p>
     */
    public String formatDatasetSummary(final String rawSummary, final String pCode) {

        // Report on hyperlinks present in the summary - these will need to be manually added as Resource Links.
        final List<String> hyperlinksInSummary = Stream.of(
            "(?<URL>http(?:s)?://[^\\s)]+)",
            "[^/](?<URL>www\\.[^\\s)]+)")
            .filter(pattern -> Pattern.compile(pattern, CASE_INSENSITIVE).matcher(rawSummary).find())
            .flatMap(pattern -> {
                final Matcher matcher = Pattern.compile(pattern, CASE_INSENSITIVE).matcher(rawSummary);

                final List<String> hyperlinks = new ArrayList<>();

                while (matcher.find()) {
                    hyperlinks.add(matcher.group("URL"));
                }

                return hyperlinks.stream();
            })
            .collect(toList());

        if (!hyperlinksInSummary.isEmpty()) {
            migrationReport.report(pCode, HYPERLINKS_IN_SUMMARY, hyperlinksInSummary);
        }

        if (isBlank(rawSummary)) {
            migrationReport.report(pCode, BLANK_SUMMARY);
        }

        // Detect HTML elements in Datasets' summaries. So far we haven't seen any but should one show up in subsequent
        // exports from Nesstar, we want to know about it ASAP so that we can deal with each case individually.
        boolean containsMarkup = Stream.of(
            "\\<.+\\>",   // any complete HTML tag (there are legitimate occurrances of angle brackets)
            "&lt;.+&gt;"  // same as above, expresseed with HTML entities
        ).anyMatch(pattern -> Pattern.compile(pattern, CASE_INSENSITIVE).matcher(rawSummary).find());

        if (containsMarkup) {
            migrationReport.report(pCode, HTML_IN_SUMMARY, rawSummary);
        }

        String summary = rawSummary;

        if (!isBlank(pCode)) {
            summary = format("{0}\n\nLegacy unique identifier: {1}", summary, pCode);
        }

        // Need to have 2 new lines for paragraphs to be rendered in the cms.
        // Also we need to double escape new lines and quotes as they need to be escaped in the json for the import
        return summary.trim()
            .replaceAll("(\n\r?){2,}", "\\\\n\\\\n")
            .replaceAll("\n\r?", "\\\\n")
            .replaceAll("\"", "\\\\\"");
    }
}
