package uk.nhs.digital.ps.migrator.model.nesstar;

import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class PublishingPackage {

    private static final XPathFactory xPathFactory = XPathFactory.instance();

    private Element rootElement;

    private XPathExpression<Element> idXpath;
    private XPathExpression<Element> titleXpath;


    public PublishingPackage(final Element rootElement) {
        this.rootElement = rootElement;
        initXpath();
    }

    String getUniqueIdentifier() {
        return idXpath.evaluateFirst(rootElement).getTextTrim();
    }

    public String getTitle() {
        return titleXpath.evaluateFirst(rootElement).getTextTrim();
    }

    @Override
    public String toString() {
        return "DataSet{" +
            "uniqueIdentifier='" + getUniqueIdentifier() + '\'' +
            ", title='" + getTitle() + '\'' +
            ", rootElement=" + rootElement +
            '}';
    }

    private void initXpath() {
        idXpath = compileXpath("stdyDscr/citation/titlStmt/IDNo");
        titleXpath = compileXpath("stdyDscr/citation/titlStmt/titl");
    }

    private XPathExpression<Element> compileXpath(final String xpath) {

        // PublishingPackages XML files are somewhat inconsistent: in some, the root element 'codeBook' has default
        // namespace specified explicitly and some not. In the former case, the namespace has to be specified
        // with prefix when compiling XPath even though the prefix itself is not used throughout the document,
        // otherwise XPath fails to find elements. Solution found in https://stackoverflow.com/a/24574242.


        final Namespace namespace = rootElement.getNamespaceURI().isEmpty()
            ? Namespace.NO_NAMESPACE
            : Namespace.getNamespace("ddi", rootElement.getNamespaceURI());

        final String namespacePrefix = rootElement.getNamespaceURI().isEmpty() ? "" : "ddi:";

        final String sanitisedXpath = ("./" + xpath).replaceAll("/", "/" + namespacePrefix);

        return xPathFactory.compile(sanitisedXpath, Filters.element(), null, namespace);
    }
}
