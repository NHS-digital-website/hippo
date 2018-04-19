package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import uk.nhs.digital.common.enums.Region;
import uk.nhs.digital.ps.directives.DateFormatterDirective;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

@Node(jcrType = "publicationsystem:basedocument")
public abstract class BaseDocument extends HippoDocument {

    private static final int WEEKS_TO_CUTOFF = 8;

    public abstract String getTitle();

    /**
     * <p>
     * In order to keep the sitemap logic in one place, this function needs to return Hippo Bean which will be used
     * to generate link (url) to this document. In most cases this will be a "this".
     *
     * For publication series this is simply parent folder object but in other cases this can become something more
     * convoluted.
     *
     * @return Object used to render a link to this page
     */
    public HippoBean getSelfLinkBean() {
        return this;
    }

    protected <T extends HippoBean> List<T> getChildBeansIfPermitted(final String propertyName, final
        Class<T> beanMappingClass) {
        assertPropertyPermitted(propertyName);

        return getChildBeansByName(propertyName, beanMappingClass);
    }

    protected <T> T getPropertyIfPermitted(final String propertyKey) {
        assertPropertyPermitted(propertyKey);

        return getProperty(propertyKey);
    }

    protected void assertPropertyPermitted(String propertyName) {
        // To be overwritten by subclasses for specific implementation
    }

    /**
     * Converts given {@linkplain Calendar} to {@linkplain RestrictableDate}.
     */
    protected RestrictableDate nominalPublicationDateCalendarToRestrictedDate(final Calendar calendar) {

        final LocalDate nominalPublicationDate = calendar.toInstant()
            .atZone(DateFormatterDirective.TIME_ZONE.toZoneId())
            .toLocalDate();

        final LocalDate cutOffPoint = LocalDate.now().plusWeeks(WEEKS_TO_CUTOFF);

        return nominalPublicationDate.isAfter(cutOffPoint)
            ? RestrictableDate.restrictedDateFrom(nominalPublicationDate)
            : RestrictableDate.fullDateFrom(nominalPublicationDate);
    }

    /**
    * Convert selected values to appropriate geographic region
    */
    protected String[] geographicCoverageValuesToRegionValue(final String[] cmsValues) {
        return Region.convertGeographicCoverageValues(cmsValues);
    }
}
