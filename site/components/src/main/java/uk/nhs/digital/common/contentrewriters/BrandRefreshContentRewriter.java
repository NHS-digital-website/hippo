package uk.nhs.digital.common.contentrewriters;

import org.hippoecm.hst.configuration.hosting.*;
import org.hippoecm.hst.core.request.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import javax.jcr.Node;

public class BrandRefreshContentRewriter extends GoogleAnalyticsContentRewriter {

    public String rewrite(String html, Node hippoHtmlNode, HstRequestContext requestContext, Mount targetMount) {

        html = super.rewrite(html, hippoHtmlNode, requestContext, targetMount);

        Document document = Jsoup.parse(html, "", Parser.xmlParser());

        // normal
        if (document.select("p").first() != null) {
            document.select("p").attr("class", "nhsd-t-body");
        }

        // heading 2
        if (document.select("h2").first() != null) {
            document.select("h2").attr("class", "nhsd-t-heading-xl");
        }

        // heading 3
        if (document.select("h3").first() != null) {
            document.select("h3").attr("class", "nhsd-t-heading-l");
        }

        // heading 4
        if (document.select("h4").first() != null) {
            document.select("h4").attr("class", "nhsd-t-heading-m");
        }

        // code
        if (document.select("code").first() != null) {
            Elements code = document.select("code");
            code.tagName("span").attr("class", "nhsd-a-text-highlight nhsd-a-text-highlight--code");
        }

        // numbered list
        if (document.select("ol").first() != null) {
            document.select("ol").attr("class", "nhsd-t-list nhsd-t-list--number");
        }

        // bullet point list
        if (document.select("ul").first() != null) {
            document.select("ul").attr("class", "nhsd-t-list nhsd-t-list--bullet");
        }

        // external link
        if (document.select("a").first() != null) {
            document.select("a").attr("class", "nhsd-a-link");
        }

        // image
        if (document.select("img").first() != null) {
            // Add empty alt attribute if no alt text
            document.select("img").forEach(e -> {
                if (!e.hasAttr("alt")) {
                    e.attr("alt", "");
                }
            });

            document.select("img")
                    .wrap("<span class=\"nhsd-a-image nhsd-a-image--round-corners nhsd-a-image--no-scale\"><picture class=\"nhsd-a-image__picture\"></picture></span>");
        }

        // table
        if (document.select("table").first() != null) {
            document.select("table")
                    .wrap("<div class=\"nhsd-m-table nhsd-t-body\"></div>")
                    .attr("data-responsive", "");

            for (Element table : document.select("table")) {
                if (table.select("th").first() != null) {
                    if (table.id().equals("cannotsort")) {
                        table.select("th").attr("data-no-sort", "");
                        table.removeAttr("id");
                    }
                } else {
                    table.removeAttr("data-responsive");
                }

            }
        }

        // mathjax
        if (document.select("span.math-tex").first() != null) {
            for (Element math : document.select("span.math-tex")) {
                math.parent().attr("style", "text-align:center");
            }
        }

        return String.valueOf(document);
    }
}
