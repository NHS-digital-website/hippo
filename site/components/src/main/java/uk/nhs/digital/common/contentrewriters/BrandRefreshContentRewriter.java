package uk.nhs.digital.common.contentrewriters;

import org.hippoecm.hst.configuration.hosting.*;
import org.hippoecm.hst.core.request.*;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import javax.jcr.Node;

public class BrandRefreshContentRewriter extends GoogleAnalyticsContentRewriter {

    public String rewrite(String html, Node hippoHtmlNode, HstRequestContext requestContext, Mount targetMount) {

        html = super.rewrite(html, hippoHtmlNode, requestContext, targetMount);

        Whitelist whitelist = Whitelist.none();
        whitelist.addTags("p");
        whitelist.addEnforcedAttribute("p", "class", "nhsd-t-body");

        whitelist.addTags("a");
        whitelist.addAttributes("a","href");
        whitelist.addEnforcedAttribute("a", "class", "nhsd-a-link");

        whitelist.addTags("img");
        whitelist.addAttributes("img","src", "alt", "align", "height", "width");

        return Jsoup.clean(html, whitelist);
    }
}
