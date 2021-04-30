package uk.nhs.digital.common.contentrewriters;

import org.hippoecm.hst.configuration.hosting.Mount;
import org.hippoecm.hst.content.rewriter.impl.SimpleContentRewriter;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import javax.jcr.Node;

public class StripTagsWithLinksContentRewriter extends SimpleContentRewriter {

    public String rewrite(String html, Node hippoHtmlNode, HstRequestContext requestContext, Mount targetMount) {

        html = super.rewrite(html, hippoHtmlNode, requestContext, targetMount);

        Whitelist whitelist = Whitelist.none();
        whitelist.addTags("a");
        whitelist.addAttributes("a","href");
        whitelist.addEnforcedAttribute("a", "class", "nhsd-a-link");

        return Jsoup.clean(html, whitelist);
    }
}
