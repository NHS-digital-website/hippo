package uk.nhs.digital.common.contentrewriters;

import org.apache.commons.lang3.*;
import org.hippoecm.hst.configuration.hosting.*;
import org.hippoecm.hst.content.beans.manager.*;
import org.hippoecm.hst.content.beans.standard.*;
import org.hippoecm.hst.content.rewriter.impl.*;
import org.hippoecm.hst.core.request.*;
import org.htmlcleaner.*;

import javax.jcr.*;

public class GoogleAnalyticsContentRewriter extends SimpleContentRewriter {

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
        try {
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
                        Object childNode = objectConverter.getObject(contentBean.getNode(), "content");
                        if (childNode != null) {
                            //fetching the type of the child node
                            contentBeanTypeName = childNode.getClass().getSimpleName();
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

            // everything is rewritten. Now return the new element
            return getHtmlCleaner().getInnerHtml(rootNode);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
