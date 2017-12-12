package uk.nhs.digital.ps.migrator.model.nesstar;

import org.jdom2.Element;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

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
