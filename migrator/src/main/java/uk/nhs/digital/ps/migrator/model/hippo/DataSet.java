package uk.nhs.digital.ps.migrator.model.hippo;

import java.util.*;
import java.util.regex.Pattern;

public class DataSet extends HippoImportableItem {

    private final String title;
    private final String summary;
    private final String nominalDate;
    private final List<Attachment> attachments;
    private final List<ResourceLink> links;

    public DataSet(final Folder parentFolder,
                   final String name,
                   final String title,
                   final String summary,
                   final String nominalDate,
                   final List<Attachment> attachments,
                   final List<ResourceLink> links) {
        super(parentFolder, name);
        this.title = title;
        this.summary = formatSummary(summary);
        this.nominalDate = nominalDate;
        this.attachments = attachments;
        this.links = links;
    }

    /**
     * Check that we don't have any html markup and format the paragraphs for import
     */
    private static String formatSummary(final String input) {
        boolean containsMarkup = Arrays.stream(new String[]{"<.*>", "&lt;", "&gt;"})
            .anyMatch(s -> Pattern.compile(s).matcher(input).find());
        if (containsMarkup) {
            throw new RuntimeException("Summary contained a bad char. Input: " + input);
        }

        // Need to have 2 new lines for paragraphs to be rendered in the cms.
        // Also we need to double escape new lines and quotes as they need to be escaped in the json for the import
        return input.trim()
            .replaceAll("(\n\r?){2,}", "\\\\n\\\\n")
            .replaceAll("\n\r?", "\\\\n")
            .replaceAll("\"", "\\\\\"");
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public List<ResourceLink> getLinks() {
        return links;
    }

    public String getNominalDate() {
        return nominalDate;
    }
}
