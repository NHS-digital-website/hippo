package uk.nhs.digital.ps.beans;

import static java.util.Arrays.asList;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.ps.beans.structuredText.StructuredText;
import uk.nhs.digital.ps.site.exceptions.DataRestrictionViolationException;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@HippoEssentialsGenerated(internalName = "publicationsystem:dataset")
@Node(jcrType = "publicationsystem:dataset")
public class Dataset extends BaseDocument {

    private RestrictableDate nominalDate;

    private static final Collection<String> propertiesPermittedWhenUpcoming = asList(
        PropertyKeys.TITLE,
        PropertyKeys.SUMMARY,
        PropertyKeys.NOMINAL_DATE
    );

    @HippoEssentialsGenerated(internalName = PropertyKeys.TITLE)
    public String getTitle() {
        return getPropertyIfPermitted(PropertyKeys.TITLE);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.SUMMARY)
    public StructuredText getSummary() {
        assertPropertyPermitted(PropertyKeys.SUMMARY);
        return new StructuredText(getProperty(PropertyKeys.SUMMARY, ""));
    }

    public RestrictableDate getNominalDate() {
        if (nominalDate == null) {
            nominalDate = Optional.ofNullable(getProperty(PropertyKeys.NOMINAL_DATE))
                .map(object -> (Calendar)object)
                .map(this::nominalPublicationDateCalendarToRestrictedDate)
                .orElse(null);
        }
        return nominalDate;
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.NEXT_PUBLICATION_DATE)
    public Calendar getNextPublicationDate() {
        return getPropertyIfPermitted(PropertyKeys.NEXT_PUBLICATION_DATE);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.GEOGRAPHIC_COVERAGE)
    public String[] getGeographicCoverage() {
        return geographicCoverageValuesToRegionValue(getPropertyIfPermitted(PropertyKeys.GEOGRAPHIC_COVERAGE));
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.TAXONOMY)
    public String[] getKeys() {
        return getPropertyIfPermitted(PropertyKeys.TAXONOMY);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.FILES_V3)
    public List<ExtAttachment> getFiles() {
        return getChildBeansIfPermitted(PropertyKeys.FILES_V3, ExtAttachment.class);
    }

    public List<RelatedLink> getResourceLinks() {
        return getChildBeansIfPermitted(PropertyKeys.RESOURCE_LINKS, RelatedLink.class);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.GRANULARITY)
    public String[] getGranularity() {
        return getPropertyIfPermitted(PropertyKeys.GRANULARITY);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.COVERAGE_START)
    public Calendar getCoverageStart() {
        return getPropertyIfPermitted(PropertyKeys.COVERAGE_START);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.COVERAGE_END)
    public Calendar getCoverageEnd() {
        return getPropertyIfPermitted(PropertyKeys.COVERAGE_END);
    }

    public Publication getParentPublication() {
        return HippoBeanHelper.getParentPublication(this);
    }

    public String getSeosummary() {
        return getParentPublication().getSeosummary();
    }

    public List<String> getFullTaxonomyList() {
        assertPropertyPermitted(PropertyKeys.TAXONOMY);

        return HippoBeanHelper.getFullTaxonomyList(this);
    }

    @Override
    protected void assertPropertyPermitted(final String propertyKey) {

        final boolean isPropertyPermitted =
            propertiesPermittedWhenUpcoming.contains(propertyKey) || isPubliclyAccessible();

        if (!isPropertyPermitted) {
            throw new DataRestrictionViolationException(
                "Property is not available when parent publication is flagged as 'not publicly accessible': " + propertyKey
            );
        }
    }

    public boolean isPubliclyAccessible() {
        Publication parentPublication = getParentPublication();
        return parentPublication != null && parentPublication.isPubliclyAccessible();
    }

    interface PropertyKeys {
        String TITLE = "publicationsystem:Title";
        String SUMMARY = "publicationsystem:Summary";
        String TAXONOMY = "hippotaxonomy:keys";
        String NOMINAL_DATE = "publicationsystem:NominalDate";
        String NEXT_PUBLICATION_DATE = "publicationsystem:NextPublicationDate";
        String COVERAGE_START = "publicationsystem:CoverageStart";
        String COVERAGE_END = "publicationsystem:CoverageEnd";
        String GEOGRAPHIC_COVERAGE = "publicationsystem:GeographicCoverage";
        String GRANULARITY = "publicationsystem:Granularity";
        String RESOURCE_LINKS = "publicationsystem:ResourceLinks";
        String FILES_V3 = "publicationsystem:Files-v3";
    }
}
