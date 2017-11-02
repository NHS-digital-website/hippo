package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoFolder;
import org.hippoecm.hst.content.beans.standard.HippoResourceBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import uk.nhs.digital.ps.site.exceptions.DataRestrictionViolationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;


@HippoEssentialsGenerated(internalName = "publicationsystem:publication")
@Node(jcrType = "publicationsystem:publication")
public class Publication extends BaseDocument {

    private static final int WEEKS_TO_CUTOFF = 8;

    private static final Collection<String> propertiesPermittedWhenUpcoming = asList(
        PropertyKeys.TITLE,
        PropertyKeys.NOMINAL_DATE,
        PropertyKeys.PUBLICLY_ACCESSIBLE
    );

    private RestrictableDate nominalPublicationDate;

    public static Publication getPublicationInFolder(HippoFolder folder) {
        return folder.getBean("content", Publication.class);
    }

    /**
     * Publication URL can be based on one of the following:
     * - this object - if there are no Datasets beans in the same folder
     * - parent folder - if the document name is "content". This indicate that there are more files in the same folder
     *   like data sets.
     */
    public HippoBean getSelfLinkBean() {
        if (getName().equals("content")) {
            return getCanonicalBean().getParentBean();
        }

        return this;
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.TAXONOMY)
    public String[] getKeys() {
        return getPropertyIfPermitted(PropertyKeys.TAXONOMY);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.SUMMARY)
    public String getSummary() {
        return getPropertyIfPermitted(PropertyKeys.SUMMARY);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.KEY_FACTS)
    public String getKeyFacts() {
        return getPropertyIfPermitted(PropertyKeys.KEY_FACTS);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.INFORMATION_TYPE)
    public String[] getInformationType() {
        return getPropertyIfPermitted(PropertyKeys.INFORMATION_TYPE);
    }

    /**
     * <p>
     * Returns Nominal Publication Date. If the date is more than {@linkplain #WEEKS_TO_CUTOFF} ahead
     * in the future, the returned {@linkplain RestrictableDate} will not have day component populated and
     * its {@linkplain RestrictableDate#isRestricted()} will return {@code true}. Otherwise, the returned object
     * will be fully populated.
     * </p><p>
     * This is to ensure that the day component does not get prematurely exposed before the cut-off date.
     * Any other place (such as view template or component) would offer less protection against the leak.
     * They are still free to implement their custom logic depending on value returned by {@linkplain
     * RestrictableDate#isRestricted()}, though.
     * </p>
     */
    public RestrictableDate getNominalPublicationDate() {
        if (nominalPublicationDate == null) {
            nominalPublicationDate = Optional.ofNullable(getProperty(PropertyKeys.NOMINAL_DATE))
                .map(object -> (Calendar)object)
                .map(this::nominalPublicationDateCalendarToRestrictedDate)
                .orElse(null);
        }
        return nominalPublicationDate;
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.COVERAGE_START)
    public Calendar getCoverageStart() {
        return getPropertyIfPermitted(PropertyKeys.COVERAGE_START);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.COVERAGE_END)
    public Calendar getCoverageEnd() {
        return getPropertyIfPermitted(PropertyKeys.COVERAGE_END);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.GEOGRAPHIC_COVERAGE)
    public String getGeographicCoverage() {
        return getPropertyIfPermitted(PropertyKeys.GEOGRAPHIC_COVERAGE);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.GRANULARITY)
    public String[] getGranularity() {
        return getPropertyIfPermitted(PropertyKeys.GRANULARITY);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.ADMINISTRATIVE_SOURCES)
    public String getAdministrativeSources() {
        return getPropertyIfPermitted(PropertyKeys.ADMINISTRATIVE_SOURCES);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.TITLE)
    public String getTitle() {
        return getPropertyIfPermitted(PropertyKeys.TITLE);
    }

    public boolean isPubliclyAccessible() {
        return getPropertyIfPermitted(PropertyKeys.PUBLICLY_ACCESSIBLE);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.RELATED_LINKS)
    public List<Relatedlink> getRelatedLinks() {
        return getChildBeansIfPermitted(PropertyKeys.RELATED_LINKS, Relatedlink.class);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.ATTACHMENTS)
    public List<HippoResourceBean> getAttachments() {
        return getChildBeansIfPermitted(PropertyKeys.ATTACHMENTS, HippoResourceBean.class);
    }

    private <T extends HippoBean> List<T> getChildBeansIfPermitted(final String propertyName, final
    Class<T> beanMappingClass) {
        assertPropertyPermitted(propertyName);

        return getChildBeansByName(propertyName, beanMappingClass);
    }

    private <T> T getPropertyIfPermitted(final String propertyName) {
        assertPropertyPermitted(propertyName);

        return getProperty(propertyName);
    }

    private void assertPropertyPermitted(final String propertyKey) {

        final boolean isPropertyPermitted = PropertyKeys.PUBLICLY_ACCESSIBLE.equals(propertyKey)
            || isPubliclyAccessible()
            || propertiesPermittedWhenUpcoming.contains(propertyKey);

        if (!isPropertyPermitted) {
            throw new DataRestrictionViolationException(
                "Property is not available when publication is flagged as 'not publicly accessible': " + propertyKey
            );
        }
    }

    /**
     * Converts given {@linkplain Calendar} to {@linkplain RestrictableDate}.
     */
    private RestrictableDate nominalPublicationDateCalendarToRestrictedDate(final Calendar calendar) {

        final LocalDate nominalPublicationDate = LocalDateTime.ofInstant(
                calendar.toInstant(),
                calendar.getTimeZone().toZoneId()
            ).toLocalDate();

        final LocalDate cutOffPoint = LocalDate.now().plusWeeks(WEEKS_TO_CUTOFF);

        return nominalPublicationDate.isAfter(cutOffPoint)
            ? RestrictableDate.restrictedDateFrom(nominalPublicationDate)
            : RestrictableDate.fullDateFrom(nominalPublicationDate);
    }

    interface PropertyKeys {
        String TAXONOMY = "hippotaxonomy:keys";
        String SUMMARY = "publicationsystem:Summary";
        String KEY_FACTS = "publicationsystem:KeyFacts";
        String INFORMATION_TYPE = "publicationsystem:InformationType";
        String NOMINAL_DATE = "publicationsystem:NominalDate";
        String COVERAGE_START = "publicationsystem:CoverageStart";
        String COVERAGE_END = "publicationsystem:CoverageEnd";
        String GEOGRAPHIC_COVERAGE = "publicationsystem:GeographicCoverage";
        String GRANULARITY = "publicationsystem:Granularity";
        String ADMINISTRATIVE_SOURCES = "publicationsystem:AdministrativeSources";
        String TITLE = "publicationsystem:Title";
        String PUBLICLY_ACCESSIBLE = "publicationsystem:PubliclyAccessible";
        String RELATED_LINKS = "publicationsystem:RelatedLinks";
        String ATTACHMENTS = "publicationsystem:attachments";
    }
}
