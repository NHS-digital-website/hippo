package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoFolder;
import org.hippoecm.hst.content.beans.standard.HippoResourceBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoResourceBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

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
            return getParentBean();
        }

        return this;
    }

    @HippoEssentialsGenerated(internalName = "hippotaxonomy:keys")
    public String[] getKeys() {
        return getProperty("hippotaxonomy:keys");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:Summary")
    public String getSummary() {
        return getProperty("publicationsystem:Summary");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:KeyFacts")
    public String getKeyFacts() {
        return getProperty("publicationsystem:KeyFacts");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:InformationType")
    public String[] getInformationType() {
        return getProperty("publicationsystem:InformationType");
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
        return getProperty("publicationsystem:CoverageStart");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:CoverageEnd")
    public Calendar getCoverageEnd() {
        return getProperty("publicationsystem:CoverageEnd");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:GeographicCoverage")
    public String getGeographicCoverage() {
        return getProperty("publicationsystem:GeographicCoverage");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:Granularity")
    public String[] getGranularity() {
        return getProperty("publicationsystem:Granularity");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:AdministrativeSources")
    public String getAdministrativeSources() {
        return getProperty("publicationsystem:AdministrativeSources");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:Title")
    public String getTitle() {
        return getProperty("publicationsystem:Title");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:RelatedLinks")
    public List<Relatedlink> getRelatedLinks() {
        return getChildBeansByName("publicationsystem:RelatedLinks",
                Relatedlink.class);
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:attachments")
    public List<HippoResourceBean> getAttachments() {
        return getChildBeansByName("publicationsystem:attachments", HippoResourceBean.class);
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
