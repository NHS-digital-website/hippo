package uk.nhs.digital.common.contentrewriters;

import org.apache.jackrabbit.util.ISO9075;
import org.hippoecm.hst.configuration.hosting.*;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.core.request.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.website.beans.CustomizedAssetSet;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;


public class BrandRefreshContentRewriter extends GoogleAnalyticsContentRewriter {

    private static Logger LOGGER = LoggerFactory.getLogger(BrandRefreshContentRewriter.class);

    public String rewrite(String html, Node hippoHtmlNode, HstRequestContext requestContext, Mount targetMount) {

        html = super.rewrite(html, hippoHtmlNode, requestContext, targetMount);

        Document document = Jsoup.parse(html, "", Parser.xmlParser());

        // normal
        docSelect(document, "p", "nhsd-t-body");

        // heading 2
        docSelect(document, "h2", "nhsd-t-heading-xl");

        // heading 3
        docSelect(document, "h3", "nhsd-t-heading-l");

        // heading 4
        docSelect(document, "h4", "nhsd-t-heading-m");

        // code
        if (document.select("code").first() != null) {
            Elements code = document.select("code");
            code.tagName("span").attr("class", "nhsd-a-text-highlight nhsd-a-text-highlight--code");
        }

        // numbered list
        docSelect(document, "ol", "nhsd-t-list nhsd-t-list--number");

        // bullet point list
        docSelect(document, "ul", "nhsd-t-list nhsd-t-list--bullet");

        // external or internal link
        if (document.select("a").first() != null) {
            document.select("a").attr("class", "nhsd-a-link");
            List<Element> lsElements = document.select("a").stream().filter(a -> a.attr("href").contains("/binaries/content/assets")
                    && !(a.attr("href").contains("https://") || a.attr("href").contains("http://")))
                .collect(Collectors.toList());

            for (Element ele : lsElements) {
                LOGGER.debug("Element " + ele);
                try {
                    String assetPath = ele.attr("href");
                    String assetQueryPath = assetPath.substring(assetPath.indexOf("/content"));
                    LOGGER.debug("Query Path is  " + assetQueryPath);
                    QueryManager jcrQueryManager = requestContext.getSession().getWorkspace().getQueryManager();
                    Query jcrQuery = jcrQueryManager.createQuery("/jcr:root" + ISO9075.encodePath(assetQueryPath), "xpath");
                    QueryResult queryResult = jcrQuery.execute();
                    NodeIterator nodes = queryResult.getNodes();
                    CustomizedAssetSet hp = (CustomizedAssetSet) requestContext
                        .getQueryManager()
                        .createQuery(nodes.nextNode(),
                            CustomizedAssetSet.class).execute().getHippoBeans().next();
                    if (hp.getArchiveMaterial() != null && hp.getArchiveMaterial()) {
                        ele.attr("rel", "archived");
                        ele.text(ele.text() + " [Archive Content]");
                    }
                } catch (NoSuchElementException | RepositoryException | QueryException e) {
                    LOGGER.warn(" Error updating Asset link ", e.getMessage());
                }

            }
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

    private void docSelect(Document document, String p, String attributeValue) {
        if (document.select(p).first() != null) {
            document.select(p).attr("class", attributeValue);
        }
    }
}
