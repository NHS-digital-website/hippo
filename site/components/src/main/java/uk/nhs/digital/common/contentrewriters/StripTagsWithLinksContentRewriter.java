package uk.nhs.digital.common.contentrewriters;

import org.hippoecm.hst.configuration.hosting.Mount;
import org.hippoecm.hst.content.rewriter.impl.SimpleContentRewriter;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import javax.jcr.Node;

public class StripTagsWithLinksContentRewriter extends SimpleContentRewriter {

    public String rewrite(String html, Node hippoHtmlNode, HstRequestContext requestContext, Mount targetMount) {

        html = super.rewrite(html, hippoHtmlNode, requestContext, targetMount);

        Safelist safelist = Safelist.none();
        safelist.addTags("a");
        safelist.addAttributes("a","href");
        safelist.addEnforcedAttribute("a", "class", "nhsd-a-link");

        return Jsoup.clean(html, safelist);
    }
}
