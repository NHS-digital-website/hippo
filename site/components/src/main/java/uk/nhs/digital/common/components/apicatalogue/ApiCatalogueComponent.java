package uk.nhs.digital.common.components.apicatalogue;

import static java.util.stream.Collectors.toList;
import static uk.nhs.digital.common.components.apicatalogue.Filters.filters;
import static uk.nhs.digital.common.components.apicatalogue.Section.section;
import static uk.nhs.digital.common.components.apicatalogue.Subsection.subsection;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import uk.nhs.digital.common.components.BaseGaContentComponent;
import uk.nhs.digital.website.beans.ComponentList;
import uk.nhs.digital.website.beans.Internallink;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApiCatalogueComponent extends BaseGaContentComponent {

    private static final Predicate<Internallink> INTERNAL_LINKS = link -> link.getLinkType().equals("internal");

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        final Set<String> selectedTags = selectedTagsFrom(request);

        final List<?> apiCatalogueLinksAll = apiCatalogueLinksFrom(request);

        final List<?> apiCatalogueLinksFiltered =
            apiCatalogueLinksFilteredBySelectedTags(apiCatalogueLinksAll, selectedTags);

        final Filters filtersModel = filtersModel(apiCatalogueLinksAll, selectedTags);

        request.setAttribute("apiCatalogueLinks", apiCatalogueLinksFiltered);
        request.setAttribute("filtersModel", filtersModel);
    }

    private static Set<String> selectedTagsFrom(final HstRequest request) {
        // We only support (and expect) a single value passed as filters.
        // Support for multiple values is planned to be added later.

        return Optional.ofNullable(request.getParameter("filters"))
            .map(filter -> new HashSet<>(Collections.singleton(filter)))
            .orElse(new HashSet<>());
    }

    private List<?> apiCatalogueLinksFrom(final HstRequest request) {
        return ((ComponentList) request.getRequestContext().getContentBean()).getBlocks();
    }

    private List<?> apiCatalogueLinksFilteredBySelectedTags(final List<?> links, final Set<String> selectedTags) {

        if (selectedTags.isEmpty()) {
            return links;
        }

        return internalLinksFrom(links, selectedTags).collect(toList());
    }

    private Filters filtersModel(final List<?> apiCatalogueLinks, final Set<String> selectedTags) {

        return newFiltersModel()
            .initialisedWith(
                filteredTaxonomyTags(selectedTags, apiCatalogueLinks),
                selectedTags
            );
    }

    private Set<String> filteredTaxonomyTags(final Set<String> selectedTags, final List<?> links) {
        return internalLinksFrom(links, selectedTags)
            .flatMap(internallink -> getTagsFromLink(internallink).stream())
            .collect(Collectors.toSet());
    }

    private Stream<Internallink> internalLinksFrom(final List<?> links, final Set<String> selectedTags) {

        return links.stream()
            .filter(Internallink.class::isInstance)
            .map(Internallink.class::cast)
            .filter(INTERNAL_LINKS)
            .filter(link -> linkHasSelectedTags(link, selectedTags));
    }

    private boolean linkHasSelectedTags(final Internallink link, final Set<String> selectedTags) {
        Set<String> taxonomyKeys = getTagsFromLink(link);
        return selectedTags.isEmpty() || taxonomyKeys.stream().anyMatch(selectedTags::contains);
    }

    private static Set<String> getTagsFromLink(final Internallink link) {
        return new HashSet<>(Arrays.asList((String[]) link.getLink().getProperties().getOrDefault("hippotaxonomy:keys", new String[0])));
    }

    private static Filters newFiltersModel() {

        final FiltersWalker depthFirstFiltersWalker = new DepthFirstFiltersWalker();

        return filters(
            depthFirstFiltersWalker,
            section("Care Setting",
                subsection("Community health", "community-health-care"),
                subsection("Dentistry", "dental-health"),
                subsection("Hospital", "hospital",
                    subsection("A&E / Emergency Department", "accident-and-emergency--a-e-"),
                    subsection("Inpatient", "inpatients"),
                    subsection("Outpatient", "outpatients")
                ),
                subsection("Maternity", "maternity"),
                subsection("Mental Health", "mental-health"),
                subsection("Patient / Citizen", "patient-citizen"),
                subsection("Pharmacy", "pharmacy"),
                subsection("GP / Primary Care", "general-practice"),
                subsection("Transport / Infrastructure", "transport-infrastructure"),
                subsection("Social Care", "social-care"),
                subsection("Urgent And Emergency Care", "urgent-and-emergency-care",
                    subsection("Ambulance", "ambulance-services"),
                    subsection("NHS 111", "111"),
                    subsection("Urgent Treatment Centres", "urgent-treatment-centres"),
                    subsection("A&E / Emergency Department", "accident-and-emergency--a-e-"),
                    subsection("OOH GP", "out-of-hours")
                )
            ),
            section("Business Use",
                subsection("Appointment / Scheduling", "appointment-scheduling",
                    subsection("Referrals", "referrals")
                ),
                subsection("Access to Records", "access-to-records_1"),
                subsection("Clinical Decision Support", "clinical-decision-support"),
                subsection("Continuity of Care (ToC)", "continuity-of-care"),
                subsection("Demographics", "demographics"),
                subsection("Key Care Information", "key-care-information"),
                subsection("Medication Management", "medication-management",
                    subsection("Prescribing", "prescribing"),
                    subsection("Dispensing", "dispensing"),
                    subsection("Vaccination", "vaccination")
                ),
                subsection("Messaging", "messaging-standards"),
                subsection("Patient Communication", "patient-communication"),
                subsection("Reference Data", "reference-data"),
                subsection("Information Governance", "information-governance"),
                subsection("Security", "security"),
                subsection("Tests and Diagnostics", "tests-and-diagnostics")
            ),
            section("Technology",
                subsection("FHIR", "fhir"),
                subsection("REST", "rest"),
                subsection("SOAP", "soap"),
                subsection("HL7 V3", "hl7-v3"),
                subsection("MESH", "mesh"),
                subsection("Adaptor", "adaptors")
            ),
            section("API Service or Standard",
                subsection("API Service", "api-service",
                    subsection("Central", "central"),
                    subsection("Intermediary", "intermediary")
                ),
                subsection("API Standard", "api-standard")
            )
        );
    }
}
