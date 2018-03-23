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

public class GoogleAnalyticsContentRewriter extends SimpleContentRewriter {

    private static final Logger log =
        LoggerFactory.getLogger(GoogleAnalyticsContentRewriter.class);

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

    @Override
    public String rewrite(final String html, final Node node,
                          final HstRequestContext requestContext,
                          final Mount targetMount) {

        if (html == null || HTML_TAG_PATTERN.matcher(html).find()) {
            //content is empty
            return null;
        }

        TagNode rootNode = getHtmlCleaner().clean(html);
        TagNode[] links = rootNode.getElementsByName("a", true);
        // rewrite of links
        for (TagNode link : links) {
            String documentPath = link.getAttributeByName("href");
            if (isExternal(documentPath)) {
                //fetching content bean type
                HippoBean contentBean = requestContext.getContentBean();
                String contentBeanTypeName = "";
                if (contentBean instanceof HippoFolder) {
                    //corner case to handle in case of publication documents
                    ObjectConverter objectConverter = requestContext.getContentBeansTool().getObjectConverter();
                    try {
                        Object childNode = objectConverter.getObject(contentBean.getNode(), "content");
                        if (childNode != null) {
                            //fetching the type of the child node
                            contentBeanTypeName = childNode.getClass().getSimpleName();
                        }
                    } catch (ObjectBeanManagerException exception) {
                        log.warn("Content child bean called 'content' doesn't exist {}", exception);
                    }
                } else {
                    //in case of "valid" content bean, fetch its type name
                    contentBeanTypeName = contentBean.getClass().getSimpleName();
                }
                //add onClick behaviour
                String onClickEvent = link.getAttributeByName("onClick");
                //preparing new onclick event to fire
                String gaEvent =
                    "logGoogleAnalyticsEvent('Link click','" + contentBeanTypeName + "','" + documentPath + "');";
                //check id the onClick attribute exists
                if (StringUtils.isEmpty(onClickEvent)) {
                    link.addAttribute("onClick", gaEvent);
                } else {
                    //in case the onClick attribute is already used, append the gaEvent
                    onClickEvent += ";" + gaEvent;
                    link.removeAttribute("onClick");
                    link.addAttribute("onClick", onClickEvent);
                }
            }
        }

        // everything is rewritten. Now write the "body" element
        // as result
        TagNode[] targetNodes =
            rootNode.getElementsByName("body", true);
        if (targetNodes.length > 0) {
            TagNode bodyNode = targetNodes[0];
            return super.rewrite(getHtmlCleaner().getInnerHtml(bodyNode),
                node, requestContext, targetMount);
        } else {
            try {
                log.warn("Cannot rewrite content for '{}' because there "
                    + "is no 'body' element " + node.getPath());
            } catch (RepositoryException exception) {
                log.debug("Problems while accessing node path {}", exception);
            }
        }

        return null;
    }

}
