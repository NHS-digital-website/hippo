package uk.nhs.digital.common.contentrewriters;

import org.hippoecm.hst.configuration.hosting.Mount;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.util.List;
import javax.jcr.Node;

public class ServiceBrandRefreshContentRewriter extends BrandRefreshContentRewriter {
    @Override
    public String rewrite(String html, Node hippoHtmlNode, HstRequestContext requestContext, Mount targetMount) {
        html = super.rewrite(html, hippoHtmlNode, requestContext, targetMount);
        Document document = Jsoup.parse(html, "", Parser.xmlParser());
        List<Element> lsElements = document.select("a");
        for (Element ele : lsElements) {
            ele.addClass("section-link-event");
        }
        return String.valueOf(document);
    }
}
