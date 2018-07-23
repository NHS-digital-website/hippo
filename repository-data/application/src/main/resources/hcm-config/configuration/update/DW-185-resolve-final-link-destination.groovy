package org.hippoecm.frontend.plugins.cms.admin.updater

import org.apache.commons.io.FilenameUtils
import org.hippoecm.repository.util.JcrUtils
import org.htmlcleaner.CleanerProperties
import org.htmlcleaner.HtmlCleaner
import org.htmlcleaner.TagNode
import org.onehippo.repository.update.BaseNodeUpdateVisitor
import uk.nhs.digital.common.HttpClientHelper

import javax.jcr.Node

/**
 * Rewrite redirected links to their final destination
 */
class ResolveFinalLinkDestination extends BaseNodeUpdateVisitor {

    private static final String TYPE_RELATED_LINK = "publicationsystem:relatedlink"
    private static final String PROPERTY_HIPPO_HTML = "hippostd:content"
    private static final String PROPERTY_HIPPO_LINK_URL = "publicationsystem:linkUrl"

    def DOWNLOAD_EXTENSIONS = [
        "DOC", "DOCX", "XLS", "XLSX", "PDF", "CSV", "ZIP", "TXT", "RAR", "PPT",
        "PPTX", "JPEG", "JPG", "PNG", "DOCM", "XLSM", "PPTM", "WAV", "MP4", "JAR", "WAR"
    ];

    Map<String, String> redirectsMap = new HashMap<>()

    HttpClientHelper helper = new HttpClientHelper()
    private static boolean htmlCleanerInitialized;
    private static HtmlCleaner cleaner;

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
            return processNode(node)
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean processNode(Node node) {

        if (TYPE_RELATED_LINK.equals(node.getPrimaryNodeType().getName())) {

            // Related link url
            def originalLink = node.getProperty(PROPERTY_HIPPO_LINK_URL).getString()
            if (!checkLink(originalLink)) {return false}

            try {
                String finalLink = getFinalLink(originalLink)

                if (!originalLink.equals(finalLink)) {
                    if (!redirectsMap.containsKey(originalLink)) {
                        redirectsMap.put(originalLink, finalLink)
                    }
                    return updateRelatedLinks(node, originalLink, finalLink)
                }

            } catch (Exception exception) {
                log.error("ERROR (" + originalLink + ") : " + exception.getMessage())
            }


        } else {

            def html = node.getProperty(PROPERTY_HIPPO_HTML).getString()

            if (html) {

                boolean updateRequired = false
                TagNode rootNode = getHtmlCleaner().clean(html)
                TagNode[] links = rootNode.getElementsByName("a", true)

                // rewrite of links
                for (TagNode link : links) {

                    String originalLink = link.getAttributeByName("href");
                    if (!checkLink(originalLink)) {continue}

                    try {
                        String finalLink = getFinalLink(originalLink)

                        if (!originalLink.equals(finalLink)) {
                            if (!redirectsMap.containsKey(originalLink)) {
                                redirectsMap.put(originalLink, finalLink)
                            }
                            updateRequired = true
                            html = html.replace(originalLink, finalLink)
                            //log.debug("link change: " + originalLink + " ======> " + finalLink)
                        }

                    } catch (Exception exception) {
                        log.error("ERROR (" + originalLink + ") : " + exception.getMessage())
                    }
                }

                if (updateRequired) {return updateHtmlLinks(node, html)}

            }
        }

        return false
    }

    String getFinalLink(String originalLink) {

        if (redirectsMap.containsKey(originalLink)) {
            // Seen before, so use what we have previously
            return redirectsMap.get(originalLink)
        } else {
            // Not seen before, check it
            HttpClientHelper.HttpResponse response = helper.resolveFinalDestination(originalLink)
            if (response.statusCode == 200) {
                return response.url
            } else {
                throw new Exception("LINK UNRESOLVED: " + response.statusCode)
            }

        }
    }

    boolean updateRelatedLinks(Node node, String originalLink, finalLink) {

        try {
            log.warn("SAVING RELATED " + node.getPath() + ":::" + originalLink + " ======> " + finalLink)
            JcrUtils.ensureIsCheckedOut(node)
            node.setProperty(PROPERTY_HIPPO_LINK_URL, finalLink)
            return true

        } catch (Exception exception) {
            log.warn("ERROR " + exception.getMessage())
            return false
        }
    }

    boolean updateHtmlLinks(Node node, String finalHtml) {

        try {
            log.warn("SAVING HTML " + node.getPath())
            JcrUtils.ensureIsCheckedOut(node)
            node.setProperty(PROPERTY_HIPPO_HTML, finalHtml)
            return true

        } catch (Exception exception) {
            log.warn("ERROR " + exception.getMessage())
            return false
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
            !url.toLowerCase().contains("linkref") &&
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
