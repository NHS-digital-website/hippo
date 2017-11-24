package uk.nhs.digital.ps.migrator.model.nesstar;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static uk.nhs.digital.ps.migrator.model.nesstar.CatalogStructure.getCatalogStructureFileNamespaces;

class Bag {

    private static final XPathFactory xPathFactory = XPathFactory.instance();

    private final Element bagElement;

    Bag(final Element bagElement) {
        this.bagElement = bagElement;
    }

    List<String> getResourceIds() {
        return Optional.ofNullable(
            xPathFactory.compile(
                "./*/@r:resource",
                Filters.attribute(),
                null,
                getCatalogStructureFileNamespaces()
            ).evaluate(bagElement)
        ).orElse(emptyList())
            .stream()
            .map(Attribute::getValue)
            .collect(toList());
    }
}
