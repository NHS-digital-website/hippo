package uk.nhs.digital.ps.test.acceptance.models;

import org.apache.commons.lang3.ArrayUtils;

/**
 * <p>
 * Encapsulates file types related to attachments used in automated tests, allowing to handle file
 * types in a more type-safe manner as well as encapsulate certain behaviours unique to specific
 * file types (such as support for 'magic numbers' described below).
 * </p><p>
 * Hippo seems to be checking some of the file types more strictly than others.
 * Files used in tests are auto-generated as a random string of bytes and Hippo
 * would happily accept most of them seemingly only checking the file extension,
 * but would reject a file pretending to be PDF unless it contained PDF-specific
 * <a>magic number</a>.
 * </p>
 */
public enum FileType {

    DOC,
    DOCX,
    XLS,
    XLSX,
    PDF(content -> {
        // PDF magic number translates to: %PDF-1.5.%
        final byte[] pdfMagicNumberPrefix = {0x25, 0x50, 0x44, 0x46, 0x2d, 0x31, 0x2e, 0x35, 0x0a, 0x25};
        return ArrayUtils.addAll(pdfMagicNumberPrefix, content);
    }),
    CSV,
    ZIP,
    TXT,
    RAR,
    PPT,
    PPTX;

    /**
     * Enriches given file content with magic numbers applicable to current file type.
     * The default implementation is a no-op and simply returns the original, unmodified
     * content as only some file types require enriching.
     */
    private ContentMagicNumbersEnricher contentMagicNumbersEnricher = content -> content;

    FileType() {
        // no-op
    }

    FileType(final ContentMagicNumbersEnricher contentMagicNumbersEnricher) {
        this.contentMagicNumbersEnricher = contentMagicNumbersEnricher;
    }

    public byte[] enrichWithMagicNumbers(final byte[] content) {
        return contentMagicNumbersEnricher.enrich(content);
    }

    public String getExtension() {
        return name().toLowerCase();
    }

    @FunctionalInterface
    interface ContentMagicNumbersEnricher {
        byte[] enrich(byte[] content);
    }

    /**
     * @return List of file types configured as allowed in Hippo CMS.
     */
    public static FileType[] getAllowedFileTypes() {
        return new FileType[] {
            DOC,
            DOCX,
            XLS,
            XLSX,
            PDF,
            CSV,
            ZIP,
            TXT,
            RAR,
            PPT,
            PPTX
        };
    }
}
