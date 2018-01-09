package uk.nhs.digital.ps.migrator.model.nesstar;

import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class CatalogStructure {

    private static final XPathFactory xPathFactory = XPathFactory.instance();

    private final Element rootElement;
    private DataSetRepository dataSetRepository;
    private static final String PUBLISHING_PACKAGE_PREFIX = "./obj/fStudy/";

    static Namespace[] getCatalogStructureFileNamespaces() {
        return new Namespace[]{
            Namespace.getNamespace("s", "http://www.w3.org/TR/1999/PR-rdf-schema-19990303#"),
            Namespace.getNamespace("r", "http://www.w3.org/1999/02/22-rdf-syntax-ns#"),
            Namespace.getNamespace("n3", "http://www.nesstar.org/rdf/Catalog#"),
            Namespace.getNamespace("n", "http://www.nesstar.org/rdf/"),
            Namespace.getNamespace("n5", "http://www.nesstar.org/rdf/Server#"),
            Namespace.getNamespace("n6", "http://www.nesstar.org/rdf/common/"),
            Namespace.getNamespace("n4", "http://www.nesstar.org/rdf/faster/")
        };
    }

    public CatalogStructure(final Element rootElement) {
        this.rootElement = rootElement;
    }

    public void setDataSetRepository(final DataSetRepository dataSetRepository) {
        this.dataSetRepository = dataSetRepository;
    }

    public Catalog findCatalogByLabel(final String catalogLabel) {

        return new Catalog(
            findElement("//n4:Catalog[s:label[text() = '¶en¤" + catalogLabel + "¤']]", rootElement),
            this
        );
    }

    List<Catalog> findSubCatalogsByBagId(final String bagId) {
        return findBagById(bagId).getResourceIds().stream()
            // Catalog/n3:children can reference a Bag which is a collection of datasets or catalogs
            // but we only want catalogs here
            .filter(reference -> reference.startsWith("./obj/fCatalog/Catalog"))
            .map(this::findCatalogByReference)
            .collect(toList());
    }

    List<PublishingPackage> findPublishingPackagesByBagId(final String bagId) {

        return findBagById(bagId).getResourceIds().stream()
            // Catalog/n3:children can reference a Bag which is a collection of datasets or catalogs
            // but we only want datasets here
            .filter(publishingPackagePath -> publishingPackagePath.startsWith(PUBLISHING_PACKAGE_PREFIX))
            .map(publishingPackagePath -> publishingPackagePath.substring(PUBLISHING_PACKAGE_PREFIX.length()))
            .map(publishingPackageid -> dataSetRepository.findById(publishingPackageid))
            .collect(toList());
    }

    private Bag findBagById(final String bagId) {

        return new Bag(
            findElement("//r:Bag[@r:about='" + bagId + "']", rootElement)
        );
    }

    private Catalog findCatalogByReference(final String catalogReference) {


        return new Catalog(
            findElement("//n4:Catalog[@r:about='" + catalogReference + "']", rootElement),
            this
        );
    }

    private static Element findElement(final String expression, final Element context) {
        final Namespace[] catalogStructureFileNamespaces = getCatalogStructureFileNamespaces();

        return xPathFactory.compile(
            expression,
            Filters.element(),
            null,
            catalogStructureFileNamespaces
        ).evaluateFirst(context);
    }

    /**
     * @return Flat list of all descendant sub catalogs.
     */
    public List<Catalog> findAllDescendantCatalogsOf(final Catalog catalog) {
        return catalog.getChildCatalogs().stream()
            .flatMap(childCatalog ->
                Stream.concat(
                    Stream.of(childCatalog),
                    findAllDescendantCatalogsOf(childCatalog).stream()
                )
            )
            .collect(toList());
    }
}
