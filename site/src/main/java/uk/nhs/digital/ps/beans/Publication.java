package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoResourceBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.ps.site.exceptions.DataRestrictionViolationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;

@HippoEssentialsGenerated(internalName = "publicationsystem:publication")
@Node(jcrType = "publicationsystem:publication")
public class Publication extends BaseDocument {

    private static final int WEEKS_TO_CUTOFF = 8;

    private RestrictableDate nominalPublicationDate;

    @HippoEssentialsGenerated(internalName = "hippotaxonomy:keys")
    public String[] getKeys() {
        return getPermittedProperty("hippotaxonomy:keys");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:Summary")
    public String getSummary() {
        return getPermittedProperty("publicationsystem:Summary");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:KeyFacts")
    public String getKeyFacts() {
        return getPermittedProperty("publicationsystem:KeyFacts");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:InformationType")
    public String[] getInformationType() {
        return getPermittedProperty("publicationsystem:InformationType");
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
     * </p><p>
     * The returned object expresses date in time zone '{@code UTC}'.
     * </p>
     */
    public RestrictableDate getNominalPublicationDate() {
        if (nominalPublicationDate == null) {
            final Calendar nominalPublicationDateCalendar = getProperty("publicationsystem:NominalDate");

            if (nominalPublicationDateCalendar != null) {
                nominalPublicationDate = toRestrictedDate(nominalPublicationDateCalendar);
            }
        }
        return nominalPublicationDate;
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:CoverageStart")
    public Calendar getCoverageStart() {
        return getPermittedProperty("publicationsystem:CoverageStart");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:CoverageEnd")
    public Calendar getCoverageEnd() {
        return getPermittedProperty("publicationsystem:CoverageEnd");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:GeographicCoverage")
    public String getGeographicCoverage() {
        return getPermittedProperty("publicationsystem:GeographicCoverage");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:Granularity")
    public String[] getGranularity() {
        return getPermittedProperty("publicationsystem:Granularity");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:AdministrativeSources")
    public String getAdministrativeSources() {
        return getPermittedProperty("publicationsystem:AdministrativeSources");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:Title")
    public String getTitle() {
        return getProperty("publicationsystem:Title");
    }

    public boolean isPubliclyAccessible() {
        return getProperty("publicationsystem:PubliclyAccessible");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:RelatedLinks")
    public List<Relatedlink> getRelatedLinks() {
        return getPermittedChildBeansByName("publicationsystem:RelatedLinks", Relatedlink.class);
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:attachments")
    public List<HippoResourceBean> getAttachments() {
        return getPermittedChildBeansByName("publicationsystem:attachments", HippoResourceBean.class);
    }

    private <T extends HippoBean> List<T> getPermittedChildBeansByName(final String propertyName, final
    Class<T> beanMappingClass) {
        assertPropertyAccessible(propertyName);

        return getChildBeansByName(propertyName, beanMappingClass);
    }

    private <T> T getPermittedProperty(final String propertyName) {
        assertPropertyAccessible(propertyName);

        return getProperty(propertyName);
    }

    private void assertPropertyAccessible(final String propertyName) {
        if (!isPubliclyAccessible()) {
            throw new DataRestrictionViolationException(
                "Property is not available when publication is flagged as 'not publicly accessible': " + propertyName
            );
        }
    }

    /**
     * Converts given {@linkplain Calendar} to {@linkplain RestrictableDate}, where the returned object expresses date
     * in time zone '{@code UTC}'.
     */
    private RestrictableDate toRestrictedDate(final Calendar nominalPublicationDateCalendar) {

        final LocalDate nominalPublicationDate = LocalDate.from(
            LocalDateTime.ofInstant(
                nominalPublicationDateCalendar.toInstant(),
                ZoneId.of("UTC")
            )
        );

        final LocalDate cutOffPoint = LocalDate.now().plusWeeks(WEEKS_TO_CUTOFF);

        return nominalPublicationDate.isAfter(cutOffPoint)
            ? RestrictableDate.restrictedDateFrom(nominalPublicationDate)
            : RestrictableDate.fullDateFrom(nominalPublicationDate);
    }
}
