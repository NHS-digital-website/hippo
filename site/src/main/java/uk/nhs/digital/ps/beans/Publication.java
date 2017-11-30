package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.content.beans.standard.HippoFolder;
import org.hippoecm.hst.content.beans.standard.HippoResourceBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.onehippo.taxonomy.api.Category;
import org.onehippo.taxonomy.api.Taxonomy;
import org.onehippo.taxonomy.api.TaxonomyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.beans.structuredText.StructuredText;
import uk.nhs.digital.ps.site.exceptions.DataRestrictionViolationException;

import javax.jcr.RepositoryException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;


@HippoEssentialsGenerated(internalName = "publicationsystem:publication")
@Node(jcrType = "publicationsystem:publication")
public class Publication extends BaseDocument {

    private static final Logger log = LoggerFactory.getLogger(Publication.class);

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
        assertPropertyPermitted(PropertyKeys.PARENT_BEAN);

        if (getName().equals("content")) {
            return getCanonicalBean().getParentBean();
        }

        return this;
    }

    public List getTaxonomyList() {
        assertPropertyPermitted(PropertyKeys.TAXONOMY);

        List<List<String>> taxonomyList = new ArrayList<>();

        // For each taxonomy tag key, get the name and also include hierarchy context (ancestors)
        if (getKeys() != null) {
            // Lookup Taxonomy Tree
            TaxonomyManager taxonomyManager = HstServices.getComponentManager().getComponent(TaxonomyManager.class.getName());
            Taxonomy taxonomyTree = taxonomyManager.getTaxonomies().getTaxonomy(getTaxonomyName());

            for (String key : getKeys()) {
                List<Category> ancestors = (List<Category>) taxonomyTree.getCategoryByKey(key).getAncestors();

                List<String> list = ancestors.stream()
                    .map(category -> category.getInfo("en").getName())
                    .collect(Collectors.toList());
                list.add(taxonomyTree.getCategoryByKey(key).getInfo("en").getName());
                taxonomyList.add(list);
            }
        }

        return taxonomyList;
    }

    private static String getTaxonomyName() throws HstComponentException {
        String taxonomyName;

        try {
            HstRequestContext ctx = RequestContextProvider.get();
            taxonomyName = ctx.getSession().getNode(
                "/hippo:namespaces/publicationsystem/publication/editor:templates/_default_/classifiable")
                .getProperty("essentials-taxonomy-name")
                .getString();
        } catch (RepositoryException e) {
            throw new HstComponentException(
                "Exception occurred during fetching taxonomy file name.", e);
        }

        return taxonomyName;
    }

    public Series getParentSeries() {
        assertPropertyPermitted(PropertyKeys.PARENT_SERIES);

        Series seriesBean = null;

        HippoBean folder = getParentBean();
        while (!HippoBeanHelper.isRootFolder(folder)) {
            Iterator<Series> iterator = folder.getChildBeans(Series.class).iterator();
            if (iterator.hasNext()) {
                seriesBean = iterator.next();
                break;
            } else {
                folder = folder.getParentBean();
            }
        }

        return seriesBean;
    }

    public HippoBeanIterator getDatasets() throws HstComponentException {
        assertPropertyPermitted(PropertyKeys.DATASETS);

        HstQueryResult hstQueryResult;
        try {
            hstQueryResult = HstQueryBuilder.create(getParentBean())
                .ofTypes(Dataset.class)
                .orderByDescending("publicationsystem:NominalDate")
                .build()
                .execute();
        } catch (QueryException e) {
            log.error("Failed to find datasets for publication " + getCanonicalPath(), e);
            throw new HstComponentException(
                "Failed to find datasets for publication " + getCanonicalPath(), e);
        }

        return hstQueryResult.getHippoBeans();
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.TAXONOMY)
    public String[] getKeys() {
        return getPropertyIfPermitted(PropertyKeys.TAXONOMY);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.SUMMARY)
    public StructuredText getSummary() {
        assertPropertyPermitted(PropertyKeys.SUMMARY);

        return new StructuredText(getProperty(PropertyKeys.SUMMARY, ""));
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

    public List<Relatedlink> getResourceLinks() {
        return getChildBeansIfPermitted(PropertyKeys.RESOURCE_LINKS, Relatedlink.class);
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

    private <T> T getPropertyIfPermitted(final String propertyKey) {
        assertPropertyPermitted(propertyKey);

        return getProperty(propertyKey);
    }

    private void assertPropertyPermitted(final String propertyKey) {

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
        String RESOURCE_LINKS = "publicationsystem:ResourceLinks";
        String ATTACHMENTS = "publicationsystem:attachments";

        String PARENT_BEAN = "PARENT_BEAN";
        String PARENT_SERIES = "PARENT_SERIES";
        String DATASETS = "DATASETS";
    }
}
