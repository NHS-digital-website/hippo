package org.hippoecm.frontend.plugins.cms.admin.updater

import org.apache.commons.io.FilenameUtils
import org.htmlcleaner.CleanerProperties
import org.htmlcleaner.HtmlCleaner
import org.htmlcleaner.TagNode
import org.onehippo.repository.update.BaseNodeUpdateVisitor
import uk.nhs.digital.common.HttpClientHelper

import javax.jcr.Node

/**
 * Report on HTML links and Related Links
 */
class LinkCheckerQaScript extends BaseNodeUpdateVisitor {

    private static final String TYPE_RELATED_LINK = "publicationsystem:relatedlink"
    private static final String PROPERTY_HIPPO_HTML = "hippostd:content"
    private static final String PROPERTY_HIPPO_LINK_URL = "publicationsystem:linkUrl"

    def DOWNLOAD_EXTENSIONS = [
        "DOC", "DOCX", "XLS", "XLSX", "PDF", "CSV", "ZIP", "TXT", "RAR", "PPT",
        "PPTX", "JPEG", "JPG", "PNG", "DOCM", "XLSM", "PPTM", "WAV", "MP4", "JAR", "WAR"
    ];

    HttpClientHelper helper = new HttpClientHelper()
    private static boolean htmlCleanerInitialized;
    private static HtmlCleaner cleaner;
    private int count;

    private static synchronized void initCleaner() {
        if (!htmlCleanerInitialized) {
            cleaner = new HtmlCleaner();
            CleanerProperties properties = cleaner.getProperties();
            properties.setRecognizeUnicodeChars(false);
            properties.setOmitComments(true);
            htmlCleanerInitialized = true;
        }
    }

    protected static HtmlCleaner getHtmlCleaner() {
        if (!htmlCleanerInitialized) {
            initCleaner();
        }
        return cleaner;
    }


    boolean doUpdate(Node node) {
        try {
            return parseContent(node)
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean parseContent(Node node) {

        if (TYPE_RELATED_LINK.equals(node.getPrimaryNodeType().getName())) {

            // Related link url
            def link = node.getProperty(PROPERTY_HIPPO_LINK_URL).getString()

            resolveLinkUrl(node.getPath(), link)

        } else {

            // Hippo HTML field
            def html = node.getProperty(PROPERTY_HIPPO_HTML).getString()

            if (html) {
                TagNode rootNode = getHtmlCleaner().clean(html)
                TagNode[] anchors = rootNode.getElementsByName("a", true)

                for (TagNode anchor : anchors) {
                    String link = anchor.getAttributeByName("href")
                    resolveLinkUrl(node.getPath(), link)
                }
            }
        }

        return false
    }

    void resolveLinkUrl(String nodePath, String originalUrl) {

        if (checkLink(originalUrl)) {

            String finalLink = ""
            String finalStatusCode = ""

            try {
                def initialStatusCode = helper.getInitialHttpStatusCode(originalUrl)

                // if redirect (temp/perm), get final destination
                if (initialStatusCode == 301 || initialStatusCode == 302) {
                    HttpClientHelper.HttpResponse response = helper.resolveFinalDestination(originalUrl)
                    finalLink = response.url
                    finalStatusCode = response.statusCode == 0 ? "" : response.statusCode.toString()
                }
                count++;

                log.info("," + count + "," + nodePath + "," + originalUrl + "," + initialStatusCode + "," + finalLink + "," + finalStatusCode )
            } catch (Exception exception) {
                log.error("ERROR: " + exception.getMessage())
            }
        }

    }

    boolean checkLink(String url) {
        return  url != null &&
            isCandidateLinkToCheck(url) &&
            !isFileAttachment(url)
    }

    boolean isCandidateLinkToCheck(String url) {
        return !url.toLowerCase().startsWith("mailto:") &&
            !url.toLowerCase().startsWith("file:") &&
            !url.toLowerCase().contains("linkref")  &&
            url.contains(".")
    }

    boolean isFileAttachment(final String url) {
        String extension = FilenameUtils.getExtension(url);
        return DOWNLOAD_EXTENSIONS.contains(extension.toUpperCase());
    }

    boolean undoUpdate(Node node) {
        log.error("UNDO is not available")
        return false
    }
}
