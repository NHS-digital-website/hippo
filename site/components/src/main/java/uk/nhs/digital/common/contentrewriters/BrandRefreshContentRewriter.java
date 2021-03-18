package uk.nhs.digital.common.contentrewriters;

import org.apache.commons.lang3.*;
import org.hippoecm.hst.configuration.hosting.*;
import org.hippoecm.hst.content.beans.*;
import org.hippoecm.hst.content.beans.manager.*;
import org.hippoecm.hst.content.beans.standard.*;
import org.hippoecm.hst.content.rewriter.impl.*;
import org.hippoecm.hst.core.request.*;
import org.htmlcleaner.*;
import org.slf4j.*;

import javax.jcr.*;
import javax.jcr.Node;

public class BrandRefreshContentRewriter extends SimpleContentRewriter {

    private static final Logger log =
        LoggerFactory.getLogger(BrandRefreshContentRewriter.class);

    private static boolean htmlCleanerInitialized;
    private static HtmlCleaner cleaner;

    private static synchronized void initCleaner() {
        if (!htmlCleanerInitialized) {
            cleaner = new HtmlCleaner();
            CleanerProperties properties = cleaner.getProperties();
            properties.setOmitComments(true);
            properties.setRecognizeUnicodeChars(false);
            htmlCleanerInitialized = true;
        }
    }

    protected static HtmlCleaner getHtmlCleaner() {
        if (!htmlCleanerInitialized) {
            initCleaner();
        }
        return cleaner;
    }
    
    @Override
    public String rewrite(final String html, final Node node,
                          final HstRequestContext requestContext,
                          final Mount targetMount) {

        if (html == null || HTML_TAG_PATTERN.matcher(html).find() 
            || BODY_TAG_PATTERN.matcher(html).find()) {
            // content is empty
            return null;
        }
        try {
            TagNode rootNode = getHtmlCleaner().clean(html);
            //Add styling to <p> tag
            TagNode[] paragraphs = rootNode.getElementsByName("p", true);

            for (TagNode paragraph : paragraphs) {
                paragraph.addAttribute("class", "nhsd-t-body-s");
            }

            // everything is rewritten. Now write the "body" element
            // as result
            TagNode [] targetNodes =
                            rootNode.getElementsByName("body", true);
            if (targetNodes.length > 0 ) {
                TagNode bodyNode = targetNodes[0];
                return getHtmlCleaner().getInnerHtml(bodyNode);
            } else {
                log.warn("Cannot rewrite content for '{}' because there is no 'body' element" + node.getPath());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
