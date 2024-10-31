package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.core.parameters.ParametersInfo;
import uk.nhs.digital.common.components.catalogue.filters.Section;
import uk.nhs.digital.common.components.catalogue.filters.Subsection;
import uk.nhs.digital.common.components.info.CatalogueEssentialsFacetsComponentInfo;

import java.util.*;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ParametersInfo(
        type = CatalogueEssentialsFacetsComponentInfo.class
)
public class ApiCatalogueEssentialsFacetsComponent extends CatalogueEssentialsFacetsComponent {
    @Override
    protected void displayFirstLevelParentFilter(AtomicInteger subSectionCounter, Section section, Subsection subsection, HashMap<String, List<Object>> facetBeanMap) {
        super.displayFirstLevelParentFilter(subSectionCounter, section, subsection, facetBeanMap);

        if (isApisFilter(subsection) && facetBeanMap.get("apis_1") == null) {
            subsection.setSelectable();
        }
    }

    private boolean isApisFilter(Subsection subsection) {
        return subsection.getTaxonomyKey().equalsIgnoreCase("apis_1");
    }

    @Override
    protected boolean isExceptionalFilter(Subsection subsection) {
        return subsection.getTaxonomyKey().equalsIgnoreCase("apis_1")
            || subsection.getTaxonomyKey().equalsIgnoreCase("api-standards")
            || subsection.getTaxonomyKey().equalsIgnoreCase("medication-management");
    }

    @Override
    protected boolean isGrayedOutFilter(Subsection subsection) {
        return subsection.getTaxonomyKey().equalsIgnoreCase("api-standards")
            || subsection.getTaxonomyKey().equalsIgnoreCase("medication-management");
    }
}