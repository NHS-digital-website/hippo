package uk.nhs.digital.common.contentrewriters;

import static java.util.Arrays.asList;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.*;
import org.hippoecm.hst.configuration.hosting.*;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.*;
import org.hippoecm.hst.content.beans.manager.*;
import org.hippoecm.hst.content.beans.standard.*;
import org.hippoecm.hst.content.rewriter.impl.*;
import org.hippoecm.hst.core.request.*;
import org.htmlcleaner.*;
import org.slf4j.*;
import uk.nhs.digital.website.beans.Section;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.jcr.*;
import javax.jcr.Node;

public class GoogleAnalyticsContentRewriter extends SimpleContentRewriter {

    private static final Logger log =
        LoggerFactory.getLogger(GoogleAnalyticsContentRewriter.class);

    private static final String EVENT_ON_CLICK = "onClick";
    private static final String EVENT_ON_KEY_UP = "onKeyUp";
    private static final String GA_ACTION_FILE_DOWNLOAD = "Download attachment";
    private static final String GA_ACTION_LINK_CLICK = "Link click";
    private static final List<String> DOWNLOAD_EXTENSIONS = asList(
        "DOC", "DOCX", "XLS", "XLSX", "PDF", "CSV", "ZIP", "TXT", "RAR", "PPT",
        "PPTX", "JPEG", "JPG", "PNG", "DOCM", "XLSM", "PPTM", "WAV", "MP4");

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

            if (documentPath != null) {

                // Migrated (legacy) content documentPath is different format to new attachments, so resolve if needed
                documentPath = resolveLegacyAttachmentPath(documentPath);

                final HippoBean contentBean = requestContext.getContentBean();
                String gaAction = "";

                // What type of link is it (attachment, external link, internal link)?
                if (isFileAttachment(documentPath)) {
                    // documentPath only contains the attachment name, so add the
                    // document it sits within to give context
                    gaAction = GA_ACTION_FILE_DOWNLOAD;
                    documentPath = contentBean.getDisplayName() + " => " + documentPath;
                } else if (isExternal(documentPath)) {
                    gaAction = GA_ACTION_LINK_CLICK;
                } else {
                    // internal link - no tracking required
                    continue;
                }

                //fetching content bean type
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
                String onClickEvent = link.getAttributeByName(EVENT_ON_CLICK);
                //preparing new onclick event to fire
                String gaEvent =
                    "logGoogleAnalyticsEvent('" + gaAction + "',"
                        + "'" + contentBeanTypeName + "',"
                        + "'" + documentPath + "');";
                //check id the onClick attribute exists
                if (StringUtils.isEmpty(onClickEvent)) {
                    link.addAttribute(EVENT_ON_CLICK, gaEvent);
                } else {
                    //in case the onClick attribute is already used, append the gaEvent
                    onClickEvent += ";" + gaEvent;
                    link.removeAttribute(EVENT_ON_CLICK);
                    link.addAttribute(EVENT_ON_CLICK, onClickEvent);
                }

                String onKeyUpAttribute = link.getAttributeByName(EVENT_ON_KEY_UP);
                String onKeyUpEvent = "return vjsu.onKeyUp(event)";

                if (StringUtils.isEmpty(onKeyUpAttribute)) {
                    link.addAttribute(EVENT_ON_KEY_UP, onKeyUpEvent);
                } else {
                    //in case the onKeyUpEvent attribute is already used, append
                    onKeyUpAttribute += ";" + onKeyUpEvent;
                    link.removeAttribute(EVENT_ON_KEY_UP);
                    link.addAttribute(EVENT_ON_KEY_UP, onKeyUpAttribute);
                }

            }
        }

        // Remove any content added by CKeditor to iFrame tag (temp)
        TagNode[] iframes = rootNode.getElementsByName("iframe", true);
        Arrays.stream(iframes)
            .forEach(TagNode::removeAllChildren);

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

    private boolean isFileAttachment(final String url) {
        String extension = FilenameUtils.getExtension(url);
        return DOWNLOAD_EXTENSIONS.contains(extension.toUpperCase());
    }

    private String resolveLegacyAttachmentPath(String documentPath) {

        // Attachments imported through the migration have Names starting with LinkRef... i.e. LinkRef1366-1
        // or DefaultLinkRef i.e. DefaultLinkRef35337-1, instead of the actual attachment.extension when a new
        // attachment is added now.  Therefore any LinkRef...need to be converted to the actual attachment.extension
        if (documentPath.contains("LinkRef")) {

            HippoBean contentBean = RequestContextProvider.get().getContentBean();

            // Any direct HTML child nodes of the current contentBean?
            documentPath = resolveHippoHtmlInternalLink(documentPath, contentBean);

            // Website sections can have HTML child nodes
            for (HippoCompound section : contentBean
                .getChildBeansByName("website:sections", HippoCompound.class)) {

                if (section instanceof Section) {
                    documentPath = resolveHippoHtmlInternalLink(documentPath, section);
                }
            }
        }
        return documentPath;
    }


    private String resolveHippoHtmlInternalLink(String nodeName, final HippoBean bean) {

        for (HippoHtml htmlItem : bean.getChildBeans(HippoHtml.class)) {

            Optional docbase = htmlItem.getChildBeansByName(nodeName, HippoBean.class)
                .stream()
                .map(internallink -> internallink.getProperty("hippo:docbase"))
                .findFirst();

            if (docbase.isPresent()) {
                try {
                    nodeName = RequestContextProvider.get().getSession()
                        .getNodeByIdentifier((String) docbase.get())
                        .getName();
                } catch (RepositoryException repositoryEx) {
                    log.warn("Repository exception while fetching child nodes", repositoryEx);
                }
            }
        }
        return nodeName;
    }

}
