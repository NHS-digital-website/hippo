package uk.nhs.digital.ps.migrator.model.nesstar;

import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class PublishingPackage {

    private static final XPathFactory xPathFactory = XPathFactory.instance();

    private Element rootElement;

    private XPathExpression<Element> idXpath;
    private XPathExpression<Element> titleXpath;
    private XPathExpression<Element> notesXpath;
    private XPathExpression<Element> abstractXpath;
    private XPathExpression<Element> resourcesXpath;
    private XPathExpression<Element> dateXpath;
    private XPathExpression<Element> keywordsXpath;

    public PublishingPackage(final Element rootElement) {
        this.rootElement = rootElement;
        initXpath();
    }

    public String getUniqueIdentifier() {
        return idXpath.evaluateFirst(rootElement).getTextTrim();
    }

    public String getTitle() {
        return titleXpath.evaluateFirst(rootElement).getTextTrim();
    }

    public String getDate() {
        return dateXpath.evaluateFirst(rootElement).getTextTrim();
    }

    public String getNotes() {
        return notesXpath.evaluateFirst(rootElement).getTextTrim();
    }

    public String getAbstract() {
        // abstract is not a required tag so check for null
        Element element = abstractXpath.evaluateFirst(rootElement);
        return element == null ? null : element.getTextTrim();
    }

    public List<NesstarResource> getResources() {
        // Get the nessar resources from the XML and convert to attachment objects
        return resourcesXpath.evaluate(rootElement).stream()
            .map(element -> new NesstarResource(element, this))
            .collect(Collectors.toList());
    }

    /**
     * The summary is the notes followed by a new paragraph with the abstract
     */
    public String getSummary() {
        StringBuilder summary = new StringBuilder(getNotes());

        String abstractStr = getAbstract();
        if (abstractStr != null) {
            summary.append("\n\n");
            summary.append(abstractStr);
        }

        String uniqueId = getUniqueIdentifier().trim();
        if (uniqueId != null && !uniqueId.isEmpty()) {
            summary.append("\n\n");
            summary.append("Unique identifier: ");
            summary.append(uniqueId);
        }

        return summary.toString();
    }

    @Override
    public String toString() {
        return "DataSet{" +
            "uniqueIdentifier='" + getUniqueIdentifier() + '\'' +
            ", title='" + getTitle() + '\'' +
            '}';
    }

    private void initXpath() {
        idXpath = compileXpath("stdyDscr/citation/titlStmt/IDNo");
        titleXpath = compileXpath("stdyDscr/citation/titlStmt/titl");
        notesXpath = compileXpath("stdyDscr/citation/notes");
        abstractXpath = compileXpath("stdyDscr/stdyInfo/abstract");
        resourcesXpath = compileXpath("stdyDscr/othrStdyMat/relMat");
        keywordsXpath = compileXpath("stdyDscr/stdyInfo/subject/keyword");
        dateXpath = compileXpath("stdyDscr/citation/verStmt/version");
    }

    public XPathExpression<Element> compileXpath(final String xpath) {

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
