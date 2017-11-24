package uk.nhs.digital.ps.migrator.model.nesstar;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;
import static uk.nhs.digital.ps.migrator.misc.TextHelper.sanitiseNesstarXmlText;
import static uk.nhs.digital.ps.migrator.model.nesstar.CatalogStructure.getCatalogStructureFileNamespaces;

public class Catalog {

    private static final XPathFactory xPathFactory = XPathFactory.instance();
    private final Element catalogElement;

    private final CatalogStructure catalogStructure;

    Catalog(final Element catalogElement, final CatalogStructure catalogStructure) {
        this.catalogElement = catalogElement;
        this.catalogStructure = catalogStructure;
    }

    public String getLabel() {
        return sanitiseNesstarXmlText(
            xPathFactory.compile(
                "s:label",
                Filters.element(),
                null,
                getCatalogStructureFileNamespaces()
            ).evaluateFirst(catalogElement).getTextTrim()
        );
    }

    public List<Catalog> getChildCatalogs() {
        return catalogStructure.findSubCatalogsByBagId(findChildrenBagId());
    }

    private String findChildrenBagId() {
        return xPathFactory.compile(
            "n3:children/@r:resource",
            Filters.attribute(),
            null,
            getCatalogStructureFileNamespaces()
        ).evaluateFirst(catalogElement).getValue();
    }

    public List<PublishingPackage> findPublishingPackages() {
        return catalogStructure.findPublishingPackagesByBagId(findChildrenBagId());
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, SHORT_PREFIX_STYLE);
    }
}
