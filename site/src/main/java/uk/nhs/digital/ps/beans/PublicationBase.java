package uk.nhs.digital.ps.beans;

import static java.util.Arrays.asList;

import com.google.common.collect.Lists;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoFolder;
import org.hippoecm.hst.core.component.HstComponentException;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.components.DocumentTitleComparator;
import uk.nhs.digital.ps.site.exceptions.DataRestrictionViolationException;

import java.util.*;

public abstract class PublicationBase extends BaseDocument {

    private static final Logger log = LoggerFactory.getLogger(PublicationBase.class);

    private static final Collection<String> propertiesPermittedWhenUpcoming = asList(
        PropertyKeys.TITLE,
        PropertyKeys.NOMINAL_DATE,
        PropertyKeys.PUBLICLY_ACCESSIBLE,
        PropertyKeys.INFORMATION_TYPE,
        PropertyKeys.PARENT_SERIES
    );

    private RestrictableDate nominalPublicationDate;

    public static <T extends PublicationBase> T getPublicationInFolder(HippoFolder folder, Class<T> beanMappingClass) {
        return folder.getBean("content", beanMappingClass);
    }

    /**
     * Publication URL can be based on one of the following:
     * - this object - if there are no Datasets beans in the same folder
     * - parent folder - if the document name is "content". This indicate that there are more files in the same folder
     *   like data sets.
     */
    public HippoBean getSelfLinkBean() {
        assertPropertyPermitted(PropertyKeys.PARENT_BEAN);

        if (getName().equals("content")) {
            return getCanonicalBean().getParentBean();
        }

        return this;
    }

    public HippoBean getParentDocument() {
        assertPropertyPermitted(PropertyKeys.PARENT_SERIES);

        HippoBean parentBean = null;

        HippoBean folder = getParentBean();
        while (!HippoBeanHelper.isRootFolder(folder)) {
            List<HippoBean> parentBeans = new ArrayList<>();

            //   The parent object of the publication could be either
            //   Series or Archive and this will find which of those
            //   it is and return the parents bean
            parentBeans.addAll(folder.getChildBeans(Series.class));
            parentBeans.addAll(folder.getChildBeans(Archive.class));
            Iterator<HippoBean> iterator = parentBeans.iterator();
            if (iterator.hasNext()) {
                parentBean = iterator.next();
                break;
            } else {
                folder = folder.getParentBean();
            }
        }

        return parentBean;
    }

    public List<Dataset> getDatasets() throws HstComponentException {
        assertPropertyPermitted(PropertyKeys.DATASETS);

        HstQueryResult hstQueryResult;
        try {
            hstQueryResult = HstQueryBuilder.create(getParentBean())
                .ofTypes(Dataset.class)
                .build()
                .execute();
        } catch (QueryException queryException) {
            log.error("Failed to find datasets for publication " + getCanonicalPath(), queryException);
            throw new HstComponentException(
                "Failed to find datasets for publication " + getCanonicalPath(), queryException);
        }

        ArrayList<Dataset> hippoBeans = Lists.newArrayList((Iterator) hstQueryResult.getHippoBeans());
        hippoBeans.sort(DocumentTitleComparator.COMPARATOR);

        return hippoBeans;
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.TAXONOMY)
    public String[] getKeys() {
        return getPropertyIfPermitted(PropertyKeys.TAXONOMY);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.INFORMATION_TYPE)
    public String[] getInformationType() {
        return getPropertyIfPermitted(PropertyKeys.INFORMATION_TYPE);
    }

    public List<String> getFullTaxonomyList() {
        assertPropertyPermitted(Dataset.PropertyKeys.TAXONOMY);

        return HippoBeanHelper.getFullTaxonomyList(this);
    }

    /**
     * <p>
     * Returns Publication Date. If the date is more than {@linkplain #WEEKS_TO_CUTOFF} ahead
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
    public String[] getGeographicCoverage() {
        return geographicCoverageValuesToRegionValue(getPropertyIfPermitted(PropertyKeys.GEOGRAPHIC_COVERAGE));
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

    @HippoEssentialsGenerated(internalName = PropertyKeys.SEO_SUMMARY)
    public String getSeosummary() {
        return getPropertyIfPermitted(PropertyKeys.SEO_SUMMARY);
    }

    public boolean isPubliclyAccessible() {
        return getPropertyIfPermitted(PropertyKeys.PUBLICLY_ACCESSIBLE);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.RELATED_LINKS)
    public List<RelatedLink> getRelatedLinks() {
        return getChildBeansIfPermitted(PropertyKeys.RELATED_LINKS, RelatedLink.class);
    }

    public List<RelatedLink> getResourceLinks() {
        return getChildBeansIfPermitted(PropertyKeys.RESOURCE_LINKS, RelatedLink.class);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.ATTACHMENTS_V3)
    public List<ExtAttachment> getAttachments() {
        return getChildBeansIfPermitted(PropertyKeys.ATTACHMENTS_V3, ExtAttachment.class);
    }

    @Override
    protected void assertPropertyPermitted(final String propertyKey) {

        final boolean isPropertyPermitted =
            isPropertyAlwaysPermitted(propertyKey)
                || isPubliclyAccessible()
                || propertiesPermittedWhenUpcoming.contains(propertyKey);

        if (!isPropertyPermitted) {
            throw new DataRestrictionViolationException(
                "Property is not available when publication is flagged as 'not publicly accessible': " + propertyKey
            );
        }
    }

    private boolean isPropertyAlwaysPermitted(final String propertyKey) {
        return PropertyKeys.PARENT_BEAN.equals(propertyKey)
            || PropertyKeys.PUBLICLY_ACCESSIBLE.equals(propertyKey);
    }

    interface PropertyKeys {
        String TAXONOMY = "hippotaxonomy:keys";
        String SUMMARY = "publicationsystem:Summary";
        String SEO_SUMMARY = "publicationsystem:seosummary";
        String KEY_FACTS = "publicationsystem:KeyFacts";
        String KEY_FACT_IMAGES = "publicationsystem:KeyFactImages";
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
        String RESOURCE_LINKS = "publicationsystem:ResourceLinks";
        String ATTACHMENTS_V3 = "publicationsystem:Attachments-v3";

        String PARENT_BEAN = "PARENT_BEAN";
        String PARENT_SERIES = "PARENT_SERIES";
        String DATASETS = "DATASETS";
        String PAGES = "PAGES";
    }
}
