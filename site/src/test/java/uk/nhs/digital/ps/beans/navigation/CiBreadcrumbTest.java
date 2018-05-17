package uk.nhs.digital.ps.beans.navigation;

import static java.util.Arrays.asList;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.or;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.linking.HstLinkCreator;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.onehippo.forge.breadcrumb.om.BreadcrumbItem;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.nhs.digital.ps.beans.*;
import uk.nhs.digital.ps.test.util.ReflectionHelper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(HstQueryBuilder.class)
public class CiBreadcrumbTest {

    @Mock private HstRequest hstRequest;
    @Mock private HstRequestContext hstRequestContext;
    @Mock private HippoBean siteBaseBean;
    @Mock private HstQueryBuilder queryBuilder;
    @Mock private HstQuery query;
    @Mock private HstQueryResult queryResult;
    @Mock private HippoBeanIterator hippoBeanIterator;
    @Mock private CiLanding ciLandingBean;
    @Mock private HstLinkCreator hstLinkCreator;
    @Mock private HstLink hstLink;

    private static final String ARBITRARY_PATH = "/some/arbitrary/path/to/document";
    private static final String TITLE_CI_LANDING = "Clinical Commissioning Group Outcomes Indicator Set (CCG OIS)";
    private static final String TITLE_ARCHIVE = "Archived CCG Outcomes Indicator Set Publications";
    private static final String TITLE_SERIES = "Clinical Commissioning Group Outcomes Indicator SERIES";
    private static final String TITLE_PUBLICATION = "CCG Outcomes Indicator Set - March 2018";
    private static final String TITLE_DATASET = "1.2 Under 75 mortality from cardiovascular disease";

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        // Mock this static method so we can simulate the building and execution of a query
        PowerMockito.mockStatic(HstQueryBuilder.class);
        PowerMockito.when(HstQueryBuilder.create(siteBaseBean)).thenReturn(queryBuilder);

        given(queryBuilder.ofTypes(any(Class.class))).willReturn(queryBuilder);
        given(queryBuilder.where(or(any()))).willReturn(queryBuilder);
        given(queryBuilder.build()).willReturn(query);
        given(query.execute()).willReturn(queryResult);
        given(queryResult.getHippoBeans()).willReturn(hippoBeanIterator);

        ReflectionHelper.callMethod(RequestContextProvider.class, "set", HstRequestContext.class, hstRequestContext);

        given(hstRequestContext.getHstLinkCreator()).willReturn(hstLinkCreator);
        given(hstLinkCreator.create(ciLandingBean, hstRequestContext)).willReturn(hstLink);
        given(hstLink.getPath()).willReturn(ARBITRARY_PATH);
    }

    @Test
    public void displaysBreadcrumb_CiLanding() throws Exception {

        // given
        mockCiLanding();
        mockCurrentDocumentBean(CiLanding.class, TITLE_CI_LANDING);
        List<String> expected = Collections.singletonList(TITLE_CI_LANDING);

        // when
        CiBreadcrumb ciBreadcrumb = getCiBreadcrumb();

        // then
        assertBreadcrumbsMatch(expected, ciBreadcrumb);
    }

    @Test
    public void displaysBreadcrumb_Series() throws Exception {

        // given
        mockCurrentDocumentBean(Series.class, TITLE_SERIES);
        List<String> expected = Collections.singletonList(TITLE_SERIES);

        // when
        CiBreadcrumb ciBreadcrumb = getCiBreadcrumb();

        // then
        assertBreadcrumbsMatch(expected, ciBreadcrumb);
    }

    @Test
    public void displaysBreadcrumb_Archive() throws Exception {

        // given
        mockCurrentDocumentBean(Archive.class, TITLE_ARCHIVE);
        List<String> expected = Collections.singletonList(TITLE_ARCHIVE);

        // when
        CiBreadcrumb ciBreadcrumb = getCiBreadcrumb();

        // then
        assertBreadcrumbsMatch(expected, ciBreadcrumb);
    }

    @Test
    public void displaysBreadcrumb_Publication() throws Exception {

        // given
        mockCurrentDocumentBean(Publication.class, TITLE_PUBLICATION);
        List<String> expected = Collections.singletonList(TITLE_PUBLICATION);

        // when
        CiBreadcrumb ciBreadcrumb = getCiBreadcrumb();

        // then
        assertBreadcrumbsMatch(expected, ciBreadcrumb);
    }

    @Test
    public void displaysBreadcrumb_Series_Publication() throws Exception {

        // given
        Publication currentDocumentBean = mockCurrentDocumentBean(Publication.class, TITLE_PUBLICATION);
        mockPublicationHasParentSeries(currentDocumentBean);
        List<String> expected = asList(TITLE_SERIES,
            TITLE_PUBLICATION);

        // when
        CiBreadcrumb ciBreadcrumb = getCiBreadcrumb();

        // then
        assertBreadcrumbsMatch(expected, ciBreadcrumb);
    }

    @Test
    public void displaysBreadcrumb_Series_Publication_Dataset() throws Exception {

        // given
        Dataset currentDocumentBean = mockCurrentDocumentBean(Dataset.class, TITLE_DATASET);
        Publication parentPublication = mockDatasetHasParentPublication(currentDocumentBean);
        mockPublicationHasParentSeries(parentPublication);

        List<String> expected = asList(TITLE_SERIES,
            TITLE_PUBLICATION,
            TITLE_DATASET);

        // when
        CiBreadcrumb ciBreadcrumb = getCiBreadcrumb();

        // then
        assertBreadcrumbsMatch(expected, ciBreadcrumb);
    }

    @Test
    public void displaysBreadcrumb_CiLanding_Series_Publication_Dataset() throws Exception {

        // given
        mockCiLanding();
        Dataset currentDocumentBean = mockCurrentDocumentBean(Dataset.class, TITLE_DATASET);
        Publication parentPublication = mockDatasetHasParentPublication(currentDocumentBean);
        mockPublicationHasParentSeries(parentPublication);

        List<String> expected = asList(TITLE_CI_LANDING,
            TITLE_SERIES,
            TITLE_PUBLICATION,
            TITLE_DATASET);

        // when
        CiBreadcrumb ciBreadcrumb = getCiBreadcrumb();

        // then
        assertBreadcrumbsMatch(expected, ciBreadcrumb);
    }

    private <T extends BaseDocument> T mockCurrentDocumentBean(Class<T> docClass, String docTitle) {
        T currentDocumentBean = mock(docClass);
        given(hstRequest.getRequestContext()).willReturn(hstRequestContext);
        given(hstRequest.getRequestContext().getContentBean()).willReturn(currentDocumentBean);
        given(RequestContextProvider.get().getSiteContentBaseBean()).willReturn(siteBaseBean);

        given(currentDocumentBean.getTitle()).willReturn(docTitle);
        given(currentDocumentBean.getPath()).willReturn(ARBITRARY_PATH);

        return currentDocumentBean;
    }

    private void mockCiLanding() {
        given(queryResult.getHippoBeans().hasNext()).willReturn(true);
        given((CiLanding) queryResult.getHippoBeans().nextHippoBean()).willReturn(ciLandingBean);
        given(ciLandingBean.getTitle()).willReturn(TITLE_CI_LANDING);
    }

    private void mockPublicationHasParentSeries(Publication publication) {
        Series series = mock(Series.class);
        given(publication.getParentDocument()).willReturn(series);
        given(series.getTitle()).willReturn(TITLE_SERIES);
    }

    private Publication mockDatasetHasParentPublication(Dataset dataset) {
        Publication publication = mock(Publication.class);
        given(dataset.getParentPublication()).willReturn(publication);
        given(publication.getTitle()).willReturn(TITLE_PUBLICATION);
        return publication;
    }

    private CiBreadcrumb getCiBreadcrumb() {
        CiBreadcrumbProvider ciBreadcrumbProvider = new CiBreadcrumbProvider(hstRequest);
        return ciBreadcrumbProvider.getBreadcrumb();
    }

    private void assertBreadcrumbsMatch(List<String> expected, CiBreadcrumb ciBreadcrumb) {

        List<String> actual = ciBreadcrumb.getItems().stream()
            .map(BreadcrumbItem::getTitle)
            .collect(Collectors.toList());

        assertEquals("Breadcrumbs match", expected, actual);
    }
}
