package uk.nhs.digital.ps.beans;

import static java.util.Arrays.asList;
import static org.apache.commons.collections.IteratorUtils.toList;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoFolder;
import org.hippoecm.hst.content.beans.standard.HippoFolderBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.util.DocumentUtils;
import uk.nhs.digital.ps.components.DocumentTitleComparator;
import uk.nhs.digital.ps.directives.DateFormatterDirective;
import uk.nhs.digital.ps.site.exceptions.DataRestrictionViolationException;
import uk.nhs.digital.website.beans.News;
import uk.nhs.digital.website.beans.SupplementaryInformation;
import uk.nhs.digital.website.beans.Update;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public abstract class PublicationBase extends BaseDocument {

    private static final Logger log = LoggerFactory.getLogger(PublicationBase.class);

    private static final Collection<String> propertiesPermittedWhenUpcoming = asList(
        PropertyKeys.TITLE,
        PropertyKeys.NOMINAL_DATE,
        PropertyKeys.INFORMATION_TYPE,
        PropertyKeys.PARENT_SERIES
    );

    public static final int HOUR_OF_PUBLIC_RELEASE = 9;
    public static final int MINUTE_OF_PUBLIC_RELEASE = 30;
    public static final String EARLY_ACCESS_KEY_QUERY_PARAM = "key";

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

    /**
     * Retrieves the parent Series/Collection document for the current document.
     * This method ensures the correct parent Series/Collection is retrieved.
     *
     * @return The parent Series Collection document, or null if not found.
     */
    public HippoBean getParentSeriesCollectionDocument() {
        assertPropertyPermitted(PropertyKeys.PARENT_SERIES);
        List<BaseDocument> seriesDocuments = new ArrayList<>();

        for (HippoBean sibling : getSiblingDocuments()) {
            if (sibling instanceof Series) {
                seriesDocuments.add((Series) sibling);
            } else if (sibling instanceof Archive) {
                seriesDocuments.add((Archive) sibling);
            }
        }

        /*
         * The Series Document is in the same folder of the publication.
         */
        if (seriesDocuments.size() > 0) {
            return seriesDocuments.get(0);
        }

        for (HippoBean sibling : getParentSiblings()) {
            if (sibling instanceof Series) {
                seriesDocuments.add((Series) sibling);
            } else if (sibling instanceof Archive) {
                seriesDocuments.add((Archive) sibling);
            }
        }

        /*
         * The Series Document is in the parent folder of the publication.
         */
        if (seriesDocuments.size() > 0) {
            return seriesDocuments.get(0);
        }

        /**
         * The Publication is not part of a series.
         */
        return null;
    }

    /**
     * Retrieves a list of sibling documents for the current document.
     *
     * @return a list of {@link HippoBean} objects representing the sibling documents of the
     *         current document. If no siblings are found or an exception occurs, an empty list
     *         is returned.
     */
    private List<HippoBean> getSiblingDocuments() {
        List<HippoBean> siblings = new ArrayList<>();
        try {
            String handleUuid = this.getCanonicalHandleUUID();
            Session jcrSession = this.getNode().getSession();
            Node handleNode = jcrSession.getNodeByIdentifier(handleUuid);
            Node parentNode = handleNode.getParent();
            siblings = getFolderSiblings(parentNode);
        } catch (RepositoryException e) {
            log.error("Error retrieving sibling documents due to a repository exception. Handle UUID: {}", this.getCanonicalHandleUUID(), e);
        }
        return siblings;
    }

    /**
     * Retrieves a list of sibling documents for the parent folder of the current document.
     *
     * @return a list of {@link HippoBean} objects representing the sibling documents of the
     *         parent folder of the current document. If no siblings are found or an exception occurs, an empty list
     *         is returned.
     */
    private List<HippoBean> getParentSiblings() {
        List<HippoBean> parentFolderSiblings = new ArrayList<>();
        try {
            String handleUuid = this.getCanonicalHandleUUID();
            Session jcrSession = this.getNode().getSession();
            Node handleNode = jcrSession.getNodeByIdentifier(handleUuid);
            Node parentNode = handleNode.getParent();
            Node grandParentNode = parentNode.getParent();
            parentFolderSiblings = getFolderSiblings(grandParentNode);
            // Remove the parent folder itself from the siblings list.
            String parentNodePath = parentNode.getPath();
            parentFolderSiblings.removeIf(bean -> bean.getPath().equals(parentNodePath));
        } catch (RepositoryException e) {
            log.error("Error retrieving parent folder siblings due to a repository exception. Handle UUID: {}", this.getCanonicalHandleUUID(), e);
        }
        return parentFolderSiblings;
    }


    /**
     * Retrieves a list of sibling documents for a given folder node.
     *
     * @param folderNode the JCR Node representing the folder whose siblings are to be retrieved.
     * @return a list of {@link HippoBean} objects representing the sibling documents of the given folder node.
     *         If no siblings are found or an exception occurs, an empty list is returned.
     */
    private List<HippoBean> getFolderSiblings(Node folderNode) {
        List<HippoBean> siblings = new ArrayList<>();
        try {
            Session jcrSession = folderNode.getSession();
            HippoBean folderBean = (HippoBean) getObjectConverter().getObject(jcrSession, folderNode.getPath());
            if (folderBean instanceof HippoFolderBean) {
                HippoFolderBean folder = (HippoFolderBean) folderBean;
                List<HippoBean> children = folder.getChildBeans(HippoBean.class);
                for (HippoBean child : children) {
                    siblings.add(child);
                }
            }
        } catch (RepositoryException e) {
            log.error("Error retrieving parent folder siblings due to a repository exception. Handle UUID: {}", this.getCanonicalHandleUUID(), e);
        } catch (ObjectBeanManagerException e) {
            log.error("Error retrieving sibling documents due to an object bean manager exception. Handle UUID: {}", this.getCanonicalHandleUUID(), e);
        }
        return siblings;
    }

    /**
     * @deprecated This method is deprecated because it may return the parent of another unrelated result
     *             when in the context of a Faceted ResultSet, leading to an incorrect parent
     *             Series/Collection.
     *
     *             Use {@link #getParentSeriesCollectionDocument()} instead.
     */
    @Deprecated
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


    public List<Dataset> getDatasets() {
        assertPropertyPermitted(PropertyKeys.DATASETS);

        try {
            HstQueryResult hstQueryResult = HstQueryBuilder.create(getParentBean())
                .ofTypes(Dataset.class)
                .build()
                .execute();

            ArrayList<Dataset> hippoBeans = Lists.newArrayList((Iterator) hstQueryResult.getHippoBeans());
            hippoBeans.sort(DocumentTitleComparator.COMPARATOR);
            return hippoBeans;
        } catch (QueryException queryException) {
            log.warn("Failed to find datasets for publication {}", getCanonicalPath(), queryException);
            return Collections.emptyList();
        }
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
            nominalPublicationDate = Optional.ofNullable(getSingleProperty(PropertyKeys.NOMINAL_DATE))
                .map(object -> (Calendar)object)
                .map(this::nominalPublicationDateCalendarToRestrictedDate)
                .orElse(null);
        }
        return nominalPublicationDate;
    }

    public Calendar getNominalDate() {
        return getSingleProperty(PropertyKeys.NOMINAL_DATE);
    }

    public Date getNominalPublicationDateCalendar() {

        Calendar cal = Calendar.getInstance();
        if (nominalPublicationDate == null) {
            nominalPublicationDate = Optional.ofNullable(getSingleProperty(PropertyKeys.NOMINAL_DATE))
                .map(object -> (Calendar)object)
                .map(this::nominalPublicationDateCalendarToRestrictedDate)
                .orElse(null);
        }

        if (nominalPublicationDate != null) {
            cal.set(nominalPublicationDate.getYear(), nominalPublicationDate.getMonth().getValue() - 1, nominalPublicationDate.getDayOfMonth(), 0, 0);
        }
        return cal.getTime();
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.COVERAGE_START)
    public Calendar getCoverageStart() {
        return getPropertyIfPermittedSingle(PropertyKeys.COVERAGE_START);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.COVERAGE_END)
    public Calendar getCoverageEnd() {
        return getPropertyIfPermittedSingle(PropertyKeys.COVERAGE_END);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.TITLE)
    public String getTitle() {
        return getPropertyIfPermittedSingle(PropertyKeys.TITLE);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.SEO_SUMMARY)
    public HippoHtml getSeosummary() {
        return getHippoHtml("publicationsystem:seosummary");
    }

    public boolean isPubliclyAccessible() {
        return !getBeforePublicationDate() || isCorrectAccessKey() || isInternalViewAccess();
    }

    private boolean isInternalViewAccess() {
        String servletPath = RequestContextProvider.get().getServletRequest().getServletPath();
        return servletPath != null && servletPath.equalsIgnoreCase("/_cmsinternal");
    }

    public String getPubliclyAccessible() {
        return Boolean.toString(!getBeforePublicationDate() || isCorrectAccessKey());
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

    protected <T extends HippoBean> List<T> getRelatedDocuments(
        String property,
        Class<T> beanClass
    ) throws HstComponentException, QueryException {

        final HstRequestContext context = RequestContextProvider.get();

        HstQuery query = ContentBeanUtils.createIncomingBeansQuery(
            this.getCanonicalBean(), context.getSiteContentBaseBean(),
            property, beanClass, false);

        return toList(query.execute().getHippoBeans());
    }

    public List<Update> getUpdates() throws QueryException {
        List<Update> allLinkedUpdates = getRelatedDocuments(
            "website:relateddocument/@hippo:docbase", Update.class);
        return DocumentUtils.getFilteredAndSortedUpdates(allLinkedUpdates);
    }

    public List<SupplementaryInformation> getSupplementaryInformation()
        throws QueryException {
        List<SupplementaryInformation> linkedSupplementaryInfo = getRelatedDocuments(
            "website:relateddocuments/@hippo:docbase",
            SupplementaryInformation.class);

        return DocumentUtils
            .getSortedSupplementaryInformation(linkedSupplementaryInfo);
    }

    public List<News> getRelatedNews() throws HstComponentException, QueryException {
        return getRelatedDocuments("website:relateddocuments/@hippo:docbase", News.class).stream().sorted(
            (n1, n2) -> n2.getPublisheddatetime().compareTo(n1.getPublisheddatetime())
        ).collect(Collectors.toList());
    }

    public Boolean getBeforePublicationDate() {
        Calendar publicationDate = getSingleProperty(PropertyKeys.NOMINAL_DATE);
        if (publicationDate == null) {
            return false;
        }
        LocalDateTime publicationDateTime = publicationDate.toInstant()
            .atZone(DateFormatterDirective.TIME_ZONE.toZoneId()).toLocalDateTime()
            .withHour(HOUR_OF_PUBLIC_RELEASE).withMinute(MINUTE_OF_PUBLIC_RELEASE)
            .withSecond(0);

        LocalDateTime currentDateTime = LocalDateTime
            .now(DateFormatterDirective.TIME_ZONE.toZoneId());

        return currentDateTime.isBefore(publicationDateTime);
    }

    @Override
    protected void assertPropertyPermitted(final String propertyKey) {
        final boolean isPropertyPermitted =
            isPropertyAlwaysPermitted(propertyKey)
                || isPubliclyAccessible()
                || propertiesPermittedWhenUpcoming.contains(propertyKey)
                || isCorrectAccessKey();

        if (!isPropertyPermitted) {
            if (this.getPath() != null) {
                throw new DataRestrictionViolationException(
                    "Property in '" + this.getPath() + "' not available when publication is flagged as 'not publicly accessible': " + propertyKey
                );
            } else {
                throw new DataRestrictionViolationException(
                    "Property is not available when publication is flagged as 'not publicly accessible': " + propertyKey
                );
            }
        }
    }

    public boolean isCorrectAccessKey() {
        return StringUtils
            .isNotBlank(getSingleProperty(PublicationBase.PropertyKeys.EARLY_ACCESS_KEY))
            && getSingleProperty(PublicationBase.PropertyKeys.EARLY_ACCESS_KEY).equals(
            RequestContextProvider.get().getServletRequest().getParameter(
                EARLY_ACCESS_KEY_QUERY_PARAM));
    }

    public String[] getGeographicCoverage() {
        return geographicCoverageValuesToRegionValue(getMultipleProperty(PropertyKeys.GEOGRAPHIC_COVERAGE));
    }

    private boolean isPropertyAlwaysPermitted(final String propertyKey) {
        return PropertyKeys.PARENT_BEAN.equals(propertyKey)
            || PropertyKeys.EARLY_ACCESS_KEY.equals(propertyKey);
    }

    interface PropertyKeys {
        String TAXONOMY = "hippotaxonomy:keys";
        String SUMMARY = "publicationsystem:Summary";
        String PUBLICATION_TIER = "publicationsystem:publicationtier";
        String SEO_SUMMARY = "publicationsystem:seosummary";
        String KEY_FACTS = "publicationsystem:KeyFacts";
        String KEY_FACTS_HEAD = "publicationsystem:KeyFactsHead";
        String KEY_FACTS_TAIL = "publicationsystem:KeyFactsTail";
        String KEY_FACT_IMAGES = "publicationsystem:KeyFactImages";
        String KEY_FACT_INFOGRAPHICS = "publicationsystem:keyFactInfographics";
        String INFORMATION_TYPE = "publicationsystem:InformationType";
        String NOMINAL_DATE = "publicationsystem:NominalDate";
        String COVERAGE_START = "publicationsystem:CoverageStart";
        String COVERAGE_END = "publicationsystem:CoverageEnd";
        String GEOGRAPHIC_COVERAGE = "publicationsystem:GeographicCoverage";
        String GRANULARITY = "publicationsystem:Granularity";
        String ADMINISTRATIVE_SOURCES = "publicationsystem:AdministrativeSources";
        String TITLE = "publicationsystem:Title";
        String RELATED_LINKS = "publicationsystem:RelatedLinks";
        String RESOURCE_LINKS = "publicationsystem:ResourceLinks";
        String ATTACHMENTS_V3 = "publicationsystem:Attachments-v3";
        String EARLY_ACCESS_KEY = "publicationsystem:earlyaccesskey";

        String PARENT_BEAN = "PARENT_BEAN";
        String PARENT_SERIES = "PARENT_SERIES";
        String DATASETS = "DATASETS";
        String PAGES = "PAGES";
    }

}
