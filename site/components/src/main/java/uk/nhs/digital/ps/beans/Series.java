package uk.nhs.digital.ps.beans;

import static org.apache.commons.collections.IteratorUtils.toList;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.constraint;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.common.util.DocumentUtils;
import uk.nhs.digital.ps.beans.structuredText.StructuredText;
import uk.nhs.digital.website.beans.Person;
import uk.nhs.digital.website.beans.Team;
import uk.nhs.digital.website.beans.Update;

import java.util.ArrayList;
import java.util.List;

@HippoEssentialsGenerated(internalName = "publicationsystem:series")
@Node(jcrType = "publicationsystem:series")
public class Series extends BaseDocument {

    public HippoBean getSelfLinkBean() {
        return getCanonicalBean().getParentBean();
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:Title")
    public String getTitle() {
        return getSingleProperty("publicationsystem:Title");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:Summary")
    public StructuredText getSummary() {
        return new StructuredText(getSingleProperty("publicationsystem:Summary"));
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:ShowLatest")
    public boolean getShowLatest() {
        return getSingleProperty("publicationsystem:ShowLatest", false);
    }

    @HippoEssentialsGenerated(internalName = "common:SearchableTags")
    public String[] getSearchableTags() {
        return getMultipleProperty("common:SearchableTags");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:attachments")
    public List<ExtAttachment> getAttachments() {
        return getChildBeansIfPermitted("publicationsystem:attachments", ExtAttachment.class);
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:resourceLinks")
    public List<RelatedLink> getResourceLinks() {
        return getChildBeansIfPermitted("publicationsystem:resourceLinks", RelatedLink.class);
    }

    public String[] getInformationType() {
        return getMultipleProperty(PublicationBase.PropertyKeys.INFORMATION_TYPE);
    }

    public String[] getKeys() {
        return getMultipleProperty(PublicationBase.PropertyKeys.TAXONOMY);
    }

    public List<String> getFullTaxonomyList() {
        return HippoBeanHelper.getFullTaxonomyList(this);
    }

    public List<String> getTaxonomyList() {
        return HippoBeanHelper.getTaxonomyList(this);
    }

    public String[] getGeographicCoverage() {
        return geographicCoverageValuesToRegionValue(getMultipleProperty(PublicationBase.PropertyKeys.GEOGRAPHIC_COVERAGE));
    }

    public String[] getRawGeographicCoverage() {
        return getMultipleProperty(PublicationBase.PropertyKeys.GEOGRAPHIC_COVERAGE);
    }

    public String[] getGranularity() {
        return getMultipleProperty(PublicationBase.PropertyKeys.GRANULARITY);
    }

    public String getAdministrativeSources() {
        return getSingleProperty(PublicationBase.PropertyKeys.ADMINISTRATIVE_SOURCES);
    }

    public Publication getLatestPublication() throws HstComponentException, QueryException {
        if (!getShowLatest()) {
            return null;
        }

        HippoBean folder = getCanonicalBean().getParentBean();

        HippoBeanIterator hippoBeans = HstQueryBuilder.create(folder)
            .ofTypes(Publication.class)
            .orderByDescending("publicationsystem:NominalDate")
            .build()
            .execute()
            .getHippoBeans();

        boolean found = false;
        Publication publication = null;
        while (!found && hippoBeans.hasNext()) {
            HippoBean hippoBean = hippoBeans.nextHippoBean();
            publication = (Publication) hippoBean;
            found = publication.isPubliclyAccessible();
        }

        if (found) {
            return publication;
        }

        return null;
    }

    HippoBean oldDateFolder = null;
    ArrayList<RestrictableDate> publicationDates = null;

    public ArrayList<RestrictableDate> getPublicationDates() {
        HippoBean folder = getCanonicalBean().getParentBean();

        if (!folder.equals(oldDateFolder)) {
            HippoBeanIterator hippoBeans = null;
            try {
                hippoBeans = HstQueryBuilder.create(folder)
                    .ofTypes(Publication.class, LegacyPublication.class)
                    .orderByAscending("publicationsystem:NominalDate")
                    .build()
                    .execute()
                    .getHippoBeans();

            } catch (QueryException e) {
                e.printStackTrace();
            }

            PublicationBase publication = null;
            publicationDates = new ArrayList<RestrictableDate>();
            while (hippoBeans.hasNext()) {
                HippoBean hippoBean = hippoBeans.nextHippoBean();
                publication = (PublicationBase) hippoBean;
                publicationDates.add(publication.getNominalPublicationDate());
            }

            oldDateFolder = folder;
        }
        return publicationDates;
    }

    HippoBean oldUpcommingFolder = null;
    Boolean hasUpcommingDates = false;

    public Boolean getHasUpcomming() {
        HippoBean folder = getCanonicalBean().getParentBean();

        if (!folder.equals(oldUpcommingFolder)) {
            HippoBeanIterator hippoBeans = null;
            try {
                hippoBeans = HstQueryBuilder.create(folder)
                    .ofTypes(Publication.class, LegacyPublication.class)
                    .where(constraint("publicationsystem:PubliclyAccessible").equalTo(false))
                    .orderByAscending("publicationsystem:NominalDate")
                    .build()
                    .execute()
                    .getHippoBeans();
            } catch (QueryException e) {
                e.printStackTrace();
            }

            oldUpcommingFolder = folder;
            hasUpcommingDates = hippoBeans.hasNext();
        }

        return hasUpcommingDates;
    }

    public Person getStatistician() {
        return getLinkedBean("publicationsystem:statistician", Person.class);
    }

    public Team getTeam() {
        return getLinkedBean("publicationsystem:team", Team.class);
    }

    public SeriesReplaces getSeriesReplaces() {
        return getBean("publicationsystem:replaces", SeriesReplaces.class);
    }

    public String getShortTitle() {
        return getSingleProperty("publicationsystem:shortTitle");
    }

    public String getSubTitle() {
        return getSingleProperty("publicationsystem:subTitle");
    }

    public String getDateNaming() {
        return getSingleProperty("publicationsystem:dateNaming");
    }

    public HippoHtml getAbout() {
        return getHippoHtml("publicationsystem:about");
    }

    public HippoHtml getMethodology() {
        return getHippoHtml("publicationsystem:methodology");
    }

    public String getFrequency() {
        return getSingleProperty("publicationsystem:frequency");
    }

    public Long getRefNumber() {
        return getSingleProperty("publicationsystem:refNumber");
    }

    public String getIssn() {
        return getSingleProperty("publicationsystem:issn");
    }

    public String getPublicationTier()  {
        return getSingleProperty("publicationsystem:publicationTier");
    }

    public List<ReleaseSubject> getReleaseSubjects() {
        return getChildBeansByName("publicationsystem:releaseSubject");
    }

    public List<Update> getUpdates() throws QueryException {
        List<Update> allLinkedUpdates = getRelatedDocuments(
            "website:relateddocument/@hippo:docbase", Update.class);
        return DocumentUtils.getFilteredAndSortedUpdates(allLinkedUpdates);
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

}
