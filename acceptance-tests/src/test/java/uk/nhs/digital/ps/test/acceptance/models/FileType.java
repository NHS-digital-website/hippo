package uk.nhs.digital.ps.test.acceptance.models;

/**
 * Represents file types related to attachments used in automated tests.
 */
public enum FileType {

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
    PPTX;

    public String getExtension() {
        return name().toLowerCase();
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
