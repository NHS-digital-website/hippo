package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor
import uk.nhs.digital.common.HttpClientHelper

import javax.jcr.Node

/**
 * Report on duff links in the url rewriter
 * with the view of removing them once report verified
 */
class UrlRewriterCleanse extends BaseNodeUpdateVisitor {

    private static final String PROPERTY_TO = "urlrewriter:ruleto"
    private static final String PROPERTY_FROM = "urlrewriter:rulefrom"
    HttpClientHelper httpHelper = new HttpClientHelper()
    Map<String,String> linkLookups = new HashMap<>()


    boolean doUpdate(Node node) {
        try {
            return parseContent(node, false)
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }


    boolean parseContent(Node node, boolean secondPass) {

        def redirectFrom = node.getProperty(PROPERTY_FROM).getString()
        def redirectTo = node.getProperty(PROPERTY_TO).getString()
        def redirectToFormatted = formatLink(redirectTo)

        String finalStatusCode = ""
        String nodeName = node.getName()

        if (!linkLookups.containsKey(redirectToFormatted) || secondPass) {

            // The Updater throttle setting doesnt seem to affect the rate at which requests are sent
            // so delay added here.  Tests have shown this is required otherwise could result in false positives.
            sleep(1000)

            // look up final destination for URL
            HttpClientHelper.HttpResponse response = httpHelper.resolveFinalDestination(redirectToFormatted)
            finalStatusCode = response.statusCode

            // if already visited url, then store result (as saves revisiting again!)
            linkLookups.put(redirectToFormatted, response.statusCode)
        } else {
            finalStatusCode = linkLookups.get(redirectToFormatted).toString()
        }


        if (finalStatusCode == "404" || finalStatusCode == "500") {

            if (!secondPass) {
                // Run the link check again to double check
                parseContent(node, true)
            } else {
                // Report
                log.debug(finalStatusCode + " : " + redirectToFormatted + " : " + nodeName)

                // Remove (uncomment when verified report)
                //JcrUtils.ensureIsCheckedOut(node)
                //node.getParent().remove()
                //log.debug("REMOVED " + " : " + nodeName)
                //return true
            }
        }

        return false
    }


    String formatLink(String url) {

        // Some rules start with / some dont, so standardise
        if (url.startsWith("/")) {
            url = url.substring(1, url.length())
        }

        // Some rules have wildcard appended
        url = url.replace("\$1", "")

        // Some rules start with www
        if (url.startsWith("www")) {
            url = "https://" + url
        }

        // Some rules contain absolute urls
        if (!url.startsWith("http")) {
            url = "https://digital.nhs.uk/" + url
        }

        return url
    }


    boolean undoUpdate(Node node) {
        log.error("UNDO is not available")
        return false
    }
}
