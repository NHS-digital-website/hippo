package uk.nhs.digital.ps.migrator.model.nesstar;

import org.jdom2.Element;
import org.jdom2.xpath.XPathExpression;
import org.apache.commons.lang3.StringUtils;

public class NesstarResource {

    private final Element rootElement;

    private XPathExpression<Element> titleXpath;
    private XPathExpression<Element> uriXpath;

    public NesstarResource(final Element rootElement, final PublishingPackage publishingPackage) {
        this.rootElement = rootElement;
        initXpath(publishingPackage);
    }

    public boolean isAttachment() {
        return getUri().startsWith("/download/");
    }

    public boolean isLink() {
        return getUri().matches("https?://.*");
    }

    public boolean isNotOnIgnoreList() {
        // Certain links with set phrases are not required to be migrated over
        return !StringUtils.containsIgnoreCase(getTitle(), "earlier data may be available")
            && !StringUtils.containsIgnoreCase(getTitle(), "contact us");
    }

    public String getTitle() {
        return titleXpath.evaluateFirst(rootElement).getTextTrim();
    }

    public String getUri() {
        return uriXpath.evaluateFirst(rootElement).getAttributeValue("URI");
    }

    private void initXpath(PublishingPackage publishingPackage) {
        titleXpath = publishingPackage.compileXpath("citation/titlStmt/titl");
        uriXpath = publishingPackage.compileXpath("citation/holdings");
    }
}
