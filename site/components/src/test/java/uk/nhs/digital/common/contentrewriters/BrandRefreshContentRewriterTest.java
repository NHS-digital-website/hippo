package uk.nhs.digital.common.contentrewriters;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

import org.hippoecm.hst.configuration.hosting.Mount;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.manager.ObjectConverter;
import org.hippoecm.hst.content.beans.standard.HippoFolder;
import org.hippoecm.hst.content.tool.ContentBeansTool;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.Node;

public class BrandRefreshContentRewriterTest {

    @Mock private Node node;
    @Mock private HstRequestContext requestContext;
    @Mock private Mount targetMount;
    @Mock private HippoFolder hippoBean;
    @Mock private ContentBeansTool contentBeansTool;
    @Mock private ObjectConverter objectConverter;
    @Mock private Object childNode;


    @Before
    public void setUp() throws ObjectBeanManagerException {
        initMocks(this);

        given(requestContext.getContentBeansTool()).willReturn(contentBeansTool);
        given(requestContext.getContentBean()).willReturn(hippoBean);
        given(objectConverter.getObject(hippoBean.getNode(), "content")).willReturn(childNode);
        given(contentBeansTool.getObjectConverter()).willReturn(objectConverter);
    }

    @Test
    public void rewriteTestForNormalText() {

        BrandRefreshContentRewriter rewriter = new BrandRefreshContentRewriter();

        String html = "<h3>SCR for patients</h3><p> If you are registered with a GP practice in England your SCR is created automatically, unless you have opted out. 98% of practices are now using the system. You can talk to your practice about <a href='/pagenotfound'>including additional information</a> to do with long term conditions, care preferences or specific communications needs.</p><p><a href='/pagenotfound'> Read more patient information on SCR</a></p>";
        String expectedHtml = "<h3>SCR for patients</h3><p class=\"nhsd-t-body\"> If you are registered with a GP practice in England your SCR is created automatically, unless you have opted out. 98% of practices are now using the system. You can talk to your practice about <a href='/pagenotfound'>including additional information</a> to do with long term conditions, care preferences or specific communications needs.</p><p class=\"nhsd-t-body\"><a href='/pagenotfound'> Read more patient information on SCR</a></p>";

        String result = rewriter.rewrite(html, node, requestContext, targetMount);

        Document document = Jsoup.parse(result);
        Elements paragraphs = document.select("p");

        Document expectedDocument = Jsoup.parse(expectedHtml);
        Elements expectedParagraphs = expectedDocument.select("p");

        for (int i = 0; i < paragraphs.size(); i++) {
            assertEquals(expectedParagraphs.get(i).tagName(), paragraphs.get(i).tagName()); //p
            assertEquals(expectedParagraphs.get(i).attr("class"), paragraphs.get(i).attr(("class"))); //nhsd-t-body
            assertEquals(expectedParagraphs.get(i).text(), paragraphs.get(i).text()); //text
        }
    }

    @Test
    public void rewriteTestForHeading2() {

        BrandRefreshContentRewriter rewriter = new BrandRefreshContentRewriter();

        String html = "<h1>This is heading 1</h1><h2>This is heading 2</h2><h3>This is heading 3</h3><h4>This is heading 4</h4>"
                    + "<h1>Heading 1 again</h1><h2>Heading 2 again</h2><h3>Heading 3 again</h3><h4>Heading 4 again</h4>";
        String expectedHtml = "<h1>This is heading 1</h1><h2 class=\"nhsd-t-heading-xl\">This is heading 2</h2><h3>This is heading 3</h3><h4>This is heading 4</h4>"
                            + "<h1>Heading 1 again</h1><h2 class=\"nhsd-t-heading-xl\">Heading 2 again</h2><h3>Heading 3 again</h3><h4>Heading 4 again</h4>";

        String result = rewriter.rewrite(html, node, requestContext, targetMount);

        Document document = Jsoup.parse(result);
        Elements paragraphs = document.select("h2");

        Document expectedDocument = Jsoup.parse(expectedHtml);
        Elements expectedParagraphs = expectedDocument.select("h2");

        for (int i = 0; i < paragraphs.size(); i++) {
            assertEquals(expectedParagraphs.get(i).tagName(), paragraphs.get(i).tagName()); //h2
            assertEquals(expectedParagraphs.get(i).attr("class"), paragraphs.get(i).attr(("class"))); //nhsd-t-heading-xl
            assertEquals(expectedParagraphs.get(i).text(), paragraphs.get(i).text()); //text
        }
    }

    @Test
    public void rewriteTestForHeading3() {

        BrandRefreshContentRewriter rewriter = new BrandRefreshContentRewriter();

        String html = "<h1>This is heading 1</h1><h2>This is heading 2</h2><h3>This is heading 3</h3><h4>This is heading 4</h4>"
                    + "<h1>Heading 1 again</h1><h2>Heading 2 again</h2><h3>Heading 3 again</h3><h4>Heading 4 again</h4>";
        String expectedHtml = "<h1>This is heading 1</h1><h2>This is heading 2</h2><h3 class=\"nhsd-t-heading-l\">This is heading 3</h3><h4>This is heading 4</h4>"
                            + "<h1>Heading 1 again</h1><h2>Heading 2 again</h2><h3 class=\"nhsd-t-heading-l\">Heading 3 again</h3><h4>Heading 4 again</h4>";

        String result = rewriter.rewrite(html, node, requestContext, targetMount);

        Document document = Jsoup.parse(result);
        Elements paragraphs = document.select("h3");

        Document expectedDocument = Jsoup.parse(expectedHtml);
        Elements expectedParagraphs = expectedDocument.select("h3");

        for (int i = 0; i < paragraphs.size(); i++) {
            assertEquals(expectedParagraphs.get(i).tagName(), paragraphs.get(i).tagName()); //h3
            assertEquals(expectedParagraphs.get(i).attr("class"), paragraphs.get(i).attr(("class"))); //nhsd-t-heading-l
            assertEquals(expectedParagraphs.get(i).text(), paragraphs.get(i).text()); //text
        }
    }

    @Test
    public void rewriteTestForHeading4() {

        BrandRefreshContentRewriter rewriter = new BrandRefreshContentRewriter();

        String html = "<h1>This is heading 1</h1><h2>This is heading 2</h2><h3>This is heading 3</h3><h4>This is heading 4</h4>"
                    + "<h1>Heading 1 again</h1><h2>Heading 2 again</h2><h3>Heading 3 again</h3><h4>Heading 4 again</h4>";
        String expectedHtml = "<h1>This is heading 1</h1><h2>This is heading 2</h2><h3>This is heading 3</h3><h4 class=\"nhsd-t-heading-m\">This is heading 4</h4>"
                            + "<h1>Heading 1 again</h1><h2>Heading 2 again</h2><h3>Heading 3 again</h3><h4 class=\"nhsd-t-heading-m\">Heading 4 again</h4>";

        String result = rewriter.rewrite(html, node, requestContext, targetMount);

        Document document = Jsoup.parse(result);
        Elements paragraphs = document.select("h4");

        Document expectedDocument = Jsoup.parse(expectedHtml);
        Elements expectedParagraphs = expectedDocument.select("h4");

        for (int i = 0; i < paragraphs.size(); i++) {
            assertEquals(expectedParagraphs.get(i).tagName(), paragraphs.get(i).tagName()); //h4
            assertEquals(expectedParagraphs.get(i).attr("class"), paragraphs.get(i).attr(("class"))); //nhsd-t-heading-m
            assertEquals(expectedParagraphs.get(i).text(), paragraphs.get(i).text()); //text
        }
    }

    @Test
    public void rewriteTestForInLineCode() {

        BrandRefreshContentRewriter rewriter = new BrandRefreshContentRewriter();

        String html = "<h1>Some code</h1><code class=\"codeinline\">this is some code></code><p>some code again></p><code class=\"codeinline\">some code again</code>";
        String expectedHtml = "<h1>Some code</h1><span class=\"nhsd-a-text-highlight nhsd-a-text-highlight--code\">this is some code></code>"
                            + "<p>some code again></p><span class=\"nhsd-a-text-highlight nhsd-a-text-highlight--code\">some code again</code>";

        String result = rewriter.rewrite(html, node, requestContext, targetMount);

        Document document = Jsoup.parse(result);
        Elements paragraphs = document.select("code");

        Document expectedDocument = Jsoup.parse(expectedHtml);
        Elements expectedParagraphs = expectedDocument.select("span.nhsd-a-text-highlight nhsd-a-text-highlight--code");

        for (int i = 0; i < paragraphs.size(); i++) {
            assertEquals(expectedParagraphs.get(i).tagName(), paragraphs.get(i).tagName()); //span
            assertEquals(expectedParagraphs.get(i).attr("class"), paragraphs.get(i).attr(("class"))); //nhsd-a-text-highlight nhsd-a-text-highlight--code
            assertEquals(expectedParagraphs.get(i).text(), paragraphs.get(i).text()); //text
        }
    }

    @Test
    public void rewriteTestForNumberedList() {

        BrandRefreshContentRewriter rewriter = new BrandRefreshContentRewriter();

        String html = "<h1>Numbered List</h1><ol><li>First</li><li>Second</li><li>Third</li></ol>"
                    + "<h1>Numbered List 2</h1><ol><li>England</li><li>Scotland</li><li>Wales</li><li>Northern Ireland</li></ol>";
        String expectedHtml = "<h1>Numbered List</h1><ol class=\"nhsd-t-list nhsd-t-list--number\"><li>First</li><li>Second</li><li>Third</li></ol>"
                            + "<h1>Numbered List 2</h1><ol class=\"nhsd-t-list nhsd-t-list--number\"><li>England</li><li>Scotland</li><li>Wales</li><li>Northern Ireland</li></ol>";

        String result = rewriter.rewrite(html, node, requestContext, targetMount);

        Document document = Jsoup.parse(result);
        Elements list = document.select("ol");

        Document expectedDocument = Jsoup.parse(expectedHtml);
        Elements expectedList = expectedDocument.select("ol");

        for (int i = 0; i < list.size(); i++) {
            assertEquals(expectedList.get(i).tagName(), list.get(i).tagName()); //ol
            assertEquals(expectedList.get(i).attr("class"), list.get(i).attr(("class"))); //nhsd-t-list nhsd-t-list--number
            assertEquals(expectedList.get(i).text(), list.get(i).text()); //text
        }
    }

    @Test
    public void rewriteTestForBulletPointList() {

        BrandRefreshContentRewriter rewriter = new BrandRefreshContentRewriter();

        String html = "<h1>Bullet Point List</h1><ul><li>First</li><li>Second</li><li>Third</li></ul>"
                    + "<h1>Bullet Point List 2</h1><ul><li>England</li><li>Scotland</li><li>Wales</li><li>Northern Ireland</li></ul>";
        String expectedHtml = "<h1>Bullet Point List</h1><ul class=\"nhsd-t-list nhsd-t-list--bullet\"><li>First</li><li>Second</li><li>Third</li></ul>"
            + "<h1>Bullet Point List 2</h1><ul class=\"nhsd-t-list nhsd-t-list--bullet\"><li>England</li><li>Scotland</li><li>Wales</li><li>Northern Ireland</li></ul>";

        String result = rewriter.rewrite(html, node, requestContext, targetMount);

        Document document = Jsoup.parse(result);
        Elements list = document.select("ul");

        Document expectedDocument = Jsoup.parse(expectedHtml);
        Elements expectedList = expectedDocument.select("ul");

        for (int i = 0; i < list.size(); i++) {
            assertEquals(expectedList.get(i).tagName(), list.get(i).tagName()); //ul
            assertEquals(expectedList.get(i).attr("class"), list.get(i).attr(("class"))); //nhsd-t-list nhsd-t-list--bullet
            assertEquals(expectedList.get(i).text(), list.get(i).text()); //text
        }
    }

    @Test
    public void rewriteTestForExternalLink() {

        BrandRefreshContentRewriter rewriter = new BrandRefreshContentRewriter();

        String html = "<h3>SCR for patients</h3><p> If you are registered with a GP practice in England your SCR is created automatically, unless you have opted out. 98% of practices are now using the system. You can talk to your practice about <a href='/pagenotfound'>including additional information</a> to do with long term conditions, care preferences or specific communications needs.</p><p><a href='/pagenotfound'> Read more patient information on SCR</a></p>";
        String expectedHtml = "<h3>SCR for patients</h3><p> If you are registered with a GP practice in England your SCR is created automatically, unless you have opted out. 98% of practices are now using the system. You can talk to your practice about <a href='/pagenotfound' class=\"nhsd-a-link\">including additional information</a> to do with long term conditions, care preferences or specific communications needs.</p><p><a href='/pagenotfound' class=\"nhsd-a-link\"> Read more patient information on SCR</a></p>";

        String result = rewriter.rewrite(html, node, requestContext, targetMount);

        Document document = Jsoup.parse(result);
        Elements links = document.select("a");

        Document expectedDocument = Jsoup.parse(expectedHtml);
        Elements expectedLinks = expectedDocument.select("a");

        for (int i = 0; i < links.size(); i++) {
            assertEquals(expectedLinks.get(i).tagName(), links.get(i).tagName()); //a
            assertEquals(expectedLinks.get(i).attr("class"), links.get(i).attr(("class"))); //nhsd-a-link
            assertEquals(expectedLinks.get(i).text(), links.get(i).text()); //text
        }
    }

    @Test
    public void rewriteTestForImage() {

        BrandRefreshContentRewriter rewriter = new BrandRefreshContentRewriter();

        String html = "<h1>Image</h1><img src=\"https://en.wikipedia.org/wiki/NHS_Digital#/media/File:NHS_Digital_logo.svg\" alt=\"This is the alt txt\" align=\"top\">";

        String result = rewriter.rewrite(html, node, requestContext, targetMount);

        assertTrue(result.contains("span"));
        assertTrue(result.contains("picture"));
        assertTrue(result.contains("img"));
        assertTrue(result.contains("alt"));
        assertTrue(result.contains("align"));

        Document document = Jsoup.parse(result);

        Element spanTag = document.select("span").first();
        assertTrue(spanTag.hasClass("nhsd-a-image nhsd-a-image--round-corners nhsd-a-image--no-scale"));

        Element pictureTag = document.select("picture").first();
        assertEquals("picture", spanTag.child(0).tagName());
        assertTrue(pictureTag.hasClass("nhsd-a-image__picture"));

        Element image = document.select("img").first();
        assertEquals("img", pictureTag.child(0).tagName());
        assertEquals("This is the alt txt", image.attr("alt"));
        assertEquals("top", image.attr("align"));
    }

    @Test
    public void rewriteTestForTableWithoutHeader() {

        BrandRefreshContentRewriter rewriter = new BrandRefreshContentRewriter();

        String html = "<h1>Table</h1><table><caption>Statement of comprehensive net expenditure for the year ended 31 March 2020</caption>"
                    + "<tbody><tr><td>Staff costs</td><td>3</td><td>179,841</td><td>177,798</td></tr><tr><td>Termination benefits</td><td>3</td><td>8,359</td><td>11,165</td></tr>"
                    + "<tr><td>Operating expenditure</td><td>5</td><td>223,988</td><td>218,031</td></tr></tbody></table>";

        String result = rewriter.rewrite(html, node, requestContext, targetMount);

        assertTrue(result.contains("div"));
        assertTrue(result.contains("p"));
        assertTrue(result.contains("table"));
        assertFalse(result.contains("cannotsort"));
        assertTrue(result.contains("caption"));
        assertFalse(result.contains("thead"));
        assertTrue(result.contains("tr"));
        assertFalse(result.contains("data-no-sort"));
        assertTrue(result.contains("tbody"));
        assertTrue(result.contains("td"));

        Document document = Jsoup.parse(result);

        Element divTag = document.select("div").first();
        assertTrue(divTag.hasClass("nhsd-m-table nhsd-t-body"));

        Element heading = divTag.child(0).child(0);
        assertEquals("Statement of comprehensive net expenditure for the year ended 31 March 2020", heading.text());

        Element table = document.select("table").first();
        assertEquals("table", divTag.child(0).tagName());
        assertFalse(table.hasAttr("data-responsive"));
    }

    @Test
    public void rewriteTestForTable() {

        BrandRefreshContentRewriter rewriter = new BrandRefreshContentRewriter();

        String html = "<h1>Table</h1><table><caption>Statement of comprehensive net expenditure for the year ended 31 March 2020</caption>"
            + "<thead><tr><th>Expenditure</th><th>Note</th><th >2019-20 (£000)</th><th>2018-19 (£000)</th></tr></thead>"
            + "<tbody><tr><td>Staff costs</td><td>3</td><td>179,841</td><td>177,798</td></tr><tr><td>Termination benefits</td><td>3</td><td>8,359</td><td>11,165</td></tr>"
            + "<tr><td>Operating expenditure</td><td>5</td><td>223,988</td><td>218,031</td></tr></tbody></table>";

        String result = rewriter.rewrite(html, node, requestContext, targetMount);

        assertTrue(result.contains("div"));
        assertTrue(result.contains("p"));
        assertTrue(result.contains("table"));
        assertFalse(result.contains("cannotsort"));
        assertTrue(result.contains("caption"));
        assertTrue(result.contains("thead"));
        assertTrue(result.contains("tr"));
        assertTrue(result.contains("th"));
        assertFalse(result.contains("data-no-sort"));
        assertTrue(result.contains("tbody"));
        assertTrue(result.contains("td"));

        Document document = Jsoup.parse(result);

        Element divTag = document.select("div").first();
        assertTrue(divTag.hasClass("nhsd-m-table nhsd-t-body"));

        Element heading = divTag.child(0).child(0);
        assertEquals("Statement of comprehensive net expenditure for the year ended 31 March 2020", heading.text());

        Element table = document.select("table").first();
        assertEquals("table", divTag.child(0).tagName());
        assertTrue(table.hasAttr("data-responsive"));
    }

    @Test
    public void rewriteTestForTableNoSort() {

        BrandRefreshContentRewriter rewriter = new BrandRefreshContentRewriter();

        String html = "<h1>Table</h1><table id=\"cannotsort\"><caption>Statement of comprehensive net expenditure for the year ended 31 March 2020</caption>"
            + "<thead><tr><th>Expenditure</th><th>Note</th><th >2019-20 (£000)</th><th>2018-19 (£000)</th></tr></thead>"
            + "<tbody><tr><td>Staff costs</td><td>3</td><td>179,841</td><td>177,798</td></tr><tr><td>Termination benefits</td><td>3</td><td>8,359</td><td>11,165</td></tr>"
            + "<tr><td>Operating expenditure</td><td>5</td><td>223,988</td><td>218,031</td></tr></tbody></table>";

        String result = rewriter.rewrite(html, node, requestContext, targetMount);

        assertTrue(result.contains("div"));
        assertTrue(result.contains("p"));
        assertTrue(result.contains("table"));
        assertFalse(result.contains("cannotsort"));
        assertTrue(result.contains("caption"));
        assertTrue(result.contains("thead"));
        assertTrue(result.contains("tr"));
        assertTrue(result.contains("th"));
        assertTrue(result.contains("data-no-sort"));
        assertTrue(result.contains("tbody"));
        assertTrue(result.contains("td"));

        Document document = Jsoup.parse(result);

        Element divTag = document.select("div").first();
        assertTrue(divTag.hasClass("nhsd-m-table nhsd-t-body"));

        Element heading = divTag.child(0).child(0);
        assertEquals("Statement of comprehensive net expenditure for the year ended 31 March 2020", heading.text());

        Element table = document.select("table").first();
        assertEquals("table", divTag.child(0).tagName());
        assertTrue(table.hasAttr("data-responsive"));

        Elements tableHeading = table.select("th");
        for (Element th : tableHeading) {
            assertTrue(th.hasAttr("data-no-sort"));
        }
    }

    @Test
    public void rewriteTestForMathJax() {

        BrandRefreshContentRewriter rewriter = new BrandRefreshContentRewriter();

        String html = "<h1>Special Characters (MathJax)</h1>"
                    + "<p><span class=\"math-tex\">y = mx + c</span></p>"
                    + "<p><span class=\"math-tex\">log2(x) = 16</span></p>";

        String result = rewriter.rewrite(html, node, requestContext, targetMount);

        Document document = Jsoup.parse(result);

        Elements pTag = document.select("p");

        for (int i = 0; i < pTag.size(); i++) {
            assertTrue("style", pTag.get(i).hasAttr("style"));
            assertEquals("text-align:center", pTag.get(i).attr("style"));
            assertEquals("span", pTag.get(i).child(0).tagName());
            assertEquals("math-tex", pTag.get(i).child(0).attr("class"));
        }
    }
}
