package org.hippoecm.frontend.plugins.cms.admin.updater

import org.apache.commons.lang3.StringUtils
import org.htmlcleaner.CleanerProperties
import org.htmlcleaner.HtmlCleaner
import org.htmlcleaner.TagNode
import org.onehippo.repository.update.BaseNodeUpdateVisitor
import uk.nhs.digital.common.ResolveLinksHelper

import javax.jcr.Node
import javax.jcr.PropertyIterator
/**
 *
 *
 */
class ResolveFinalLinkDestination extends BaseNodeUpdateVisitor {

    private static final String PROPERTY_HIPPO_HTML = "hippostd:content"

    ResolveLinksHelper helper = new ResolveLinksHelper()
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
            return parseContentAndReWrite(node)
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean parseContentAndReWrite(Node node) {

        //log.info("attempting to update node: " + node.getPath())

        // Main logic
        def html = node.getProperty(PROPERTY_HIPPO_HTML).getString()

        if (html) {

            boolean updateRequired = false
            TagNode rootNode = getHtmlCleaner().clean(html)
            TagNode[] links = rootNode.getElementsByName("a", true)

            // rewrite of links
            for (TagNode link : links) {

                String originalLink = link.getAttributeByName("href");

                // TODO need to establish if external link to internal resource

                if (originalLink != null && isCandidateLinkToRewrite(originalLink)) {

                    log.warn("NODE " + node.getPath() + ". LINK==" + originalLink)

                    try {
                        def finalLink = helper.resolveFinalDestination(originalLink)

                        if (!originalLink.equals(finalLink)) {
                            updateRequired = true
                            html = html.replace(originalLink, finalLink)
                            log.warn(originalLink + " ================> " + finalLink)
                        }

                    } catch (Exception exception) {
                        log.error("Link failed in path: [" + node.getPath() + "] - Link: " + originalLink)
                        log.error("ERROR: " + exception.getMessage())
                    }
                }
            }

            if (updateRequired) {
                return false // just testing for now

                //node.setProperty(PROPERTY_HIPPO_HTML, html)
                //return true
            }

        }
        return false
    }

    boolean isCandidateLinkToRewrite(String link) {
        return !link.toLowerCase().startsWith("mailto:") &&
            !link.toLowerCase().startsWith("file:") &&
            !link.toLowerCase().contains("linkref")
    }

    boolean undoUpdate(Node node) {
        log.error("UNDO is not available")
        return false
    }
}
