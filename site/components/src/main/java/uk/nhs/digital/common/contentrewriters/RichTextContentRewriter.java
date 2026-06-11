package uk.nhs.digital.common.contentrewriters;

import org.hippoecm.hst.configuration.hosting.Mount;
import org.hippoecm.hst.content.rewriter.impl.SimpleContentRewriter;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.HtmlSerializer;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class RichTextContentRewriter extends SimpleContentRewriter {

    private static final Logger log = LoggerFactory.getLogger(RichTextContentRewriter.class);

    private static final String EVENT_ON_KEY_UP = "onKeyUp";
    private static final String ON_KEY_UP_EVENT = "return vjsu.onKeyUp(event)";

    private static boolean htmlCleanerInitialized;
    private static HtmlCleaner cleaner;

    private static HstRequestContext prevPageRequestContext = null;
    private ArrayList<String> appliedAbbrs = new ArrayList<>();

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
            prevPageRequestContext = requestContext;
            return null;
        }

        TagNode rootNode = getHtmlCleaner().clean(html);

        TagNode[] links = rootNode.getElementsByName("a", true);
        for (TagNode link : links) {
            addOnKeyUpHandler(link);
        }

        // Remove any content added by CKeditor to iFrame tag (temp)
        TagNode[] iframes = rootNode.getElementsByName("iframe", true);
        Arrays.stream(iframes)
            .forEach(TagNode::removeAllChildren);

        for (TagNode iframe : iframes) {
            // When we forbid cookies in Cookiebot, then iframe has attribute
            // 'data-src'. This attribute changes to 'src' when we permit cookies.
            String documentPathCookieOptIn = iframe.getAttributeByName("src");
            String documentPathCookieOptOut = iframe.getAttributeByName("data-src");

            if (documentPathCookieOptIn != null && documentPathCookieOptIn.contains("youtube")
                || documentPathCookieOptOut != null && documentPathCookieOptOut.contains("youtube")) {

                TagNode parent = iframe.getParent();
                if (parent != null) {
                    TagNode beforeYoutube = new TagNode("div");
                    beforeYoutube.addAttribute("class", "cookieconsent-optout-marketing");
                    parent.insertChildBefore(iframe, beforeYoutube);
                }
            }

        }

        // Add <dfn> for first instance of <abbr> of each abbreviation
        // It should work adding <dfn> only for each abbreviation per full
        // page, while this rewrite() function is invoked for each page section.
        //
        // However, we noticed that requestContext is the same for each
        // sections per page request. Therefore we reset appliedAbbrs every
        // time when new requestContext object arrives (ex. page refresh or new page)
        TagNode[] abbrs = rootNode.getElementsByName("abbr", true);
        if (requestContext != prevPageRequestContext) {
            appliedAbbrs = new ArrayList<>();
        }
        for (TagNode abbr : abbrs) {
            String abbrUniqueName = (abbr.getText().toString() + "-" + abbr.getAttributeByName("title").trim().replace(" ", "-")).toLowerCase();
            if (!appliedAbbrs.contains(abbrUniqueName)) {

                HtmlSerializer serializer = new SimpleHtmlSerializer(getHtmlCleaner().getProperties());
                String abbrString = serializer.getAsString(abbr);
                // Skip xml header from abbrString.
                abbrString = abbrString.substring(abbrString.indexOf("<abbr")).trim();

                String dfnString = "<dfn>" + abbrString + "</dfn>";

                TagNode parent = abbr.getParent();
                String innerHtml = getHtmlCleaner().getInnerHtml(parent);
                innerHtml = innerHtml.replaceFirst(abbrString, dfnString);
                getHtmlCleaner().setInnerHtml(parent, innerHtml);

                appliedAbbrs.add(abbrUniqueName);
            }
        }

        TagNode[] targetNodes =
            rootNode.getElementsByName("body", true);
        if (targetNodes.length > 0) {
            TagNode bodyNode = targetNodes[0];
            prevPageRequestContext = requestContext;
            return super.rewrite(getHtmlCleaner().getInnerHtml(bodyNode),
                node, requestContext, targetMount);
        } else {
            try {
                log.warn("Cannot rewrite content for '{}' because there is no 'body' element ", node.getPath());
            } catch (RepositoryException exception) {
                log.debug("Problems while accessing node path {}", exception);
            }
        }

        prevPageRequestContext = requestContext;
        return null;
    }

    private void addOnKeyUpHandler(final TagNode link) {
        if (link.getAttributeByName("href") == null) {
            return;
        }

        String onKeyUpAttribute = link.getAttributeByName(EVENT_ON_KEY_UP);
        if (onKeyUpAttribute == null || onKeyUpAttribute.isEmpty()) {
            link.addAttribute(EVENT_ON_KEY_UP, ON_KEY_UP_EVENT);
        } else if (!onKeyUpAttribute.contains(ON_KEY_UP_EVENT)) {
            link.removeAttribute(EVENT_ON_KEY_UP);
            link.addAttribute(EVENT_ON_KEY_UP, onKeyUpAttribute + ";" + ON_KEY_UP_EVENT);
        }
    }
}
