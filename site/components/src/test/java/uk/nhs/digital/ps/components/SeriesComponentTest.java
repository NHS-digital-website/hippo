package uk.nhs.digital.ps.components;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryManager;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.mock.core.container.MockComponentManager;
import org.hippoecm.hst.site.HstServices;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onehippo.forge.selection.hst.contentbean.ValueList;
import org.onehippo.forge.selection.hst.util.SelectionUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.nhs.digital.ps.beans.LegacyPublication;
import uk.nhs.digital.ps.beans.Pair;
import uk.nhs.digital.ps.beans.Publication;
import uk.nhs.digital.ps.beans.PublicationBase;
import uk.nhs.digital.ps.beans.Series;
import uk.nhs.digital.ps.beans.SeriesReplaces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SelectionUtil.class, RequestContextProvider.class, HstServices.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*", "javax.net.ssl.*"})
public class SeriesComponentTest {

    @Mock private HstRequest request;
    @Mock private HstResponse response;
    @Mock private HstRequestContext requestContext;
    @Mock private HippoBean seriesFolder;
    @Mock private HstQueryManager queryManager;
    @Mock private HstQuery query;
    @Mock private HstQueryResult queryResult;
    @Mock private Series series;

    private SeriesComponent component;
    private Map<String, Object> attributes;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        PowerMockito.mockStatic(SelectionUtil.class);
        PowerMockito.mockStatic(RequestContextProvider.class);
        PowerMockito.mockStatic(HstServices.class);
        PowerMockito.when(RequestContextProvider.get()).thenReturn(requestContext);
        PowerMockito.when(SelectionUtil.getValueListByIdentifier(anyString(), any())).thenReturn((ValueList)null);
        PowerMockito.when(HstServices.getComponentManager()).thenReturn(new MockComponentManager());

        component = new SeriesComponent();
        attributes = new HashMap<>();

        doAnswer(invocation -> {
            attributes.put(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(request).setAttribute(anyString(), any());

        when(request.getRequestContext()).thenReturn(requestContext);
        when(requestContext.getContentBean()).thenReturn(series);
        when(series.isHippoFolderBean()).thenReturn(false);
        when(series.getParentBean()).thenReturn(seriesFolder);
        when(requestContext.getQueryManager()).thenReturn(queryManager);
        when(queryManager.createQuery(seriesFolder, Publication.class, LegacyPublication.class)).thenReturn(query);
        when(query.execute()).thenReturn(queryResult);
    }

    @Test
    public void doBeforeRenderPopulatesAttributesForSeriesWithLiveAndUpcomingPublications() throws Exception {
        when(series.getShowLatest()).thenReturn(true);

        PublicationBase latestPublication = publication("latest", true, date(2022, Calendar.JANUARY, 5));
        PublicationBase livePublication = publication("live", true, date(2021, Calendar.MARCH, 15));
        PublicationBase upcomingEarly = publication("upcomingEarly", false, date(2024, Calendar.JUNE, 2));
        PublicationBase upcomingLate = publication("upcomingLate", false, date(2025, Calendar.APRIL, 7));

        SeriesReplaces seriesReplaces = mock(SeriesReplaces.class);
        Calendar changeDate = new GregorianCalendar(2020, Calendar.MAY, 1);
        when(seriesReplaces.getChangeDate()).thenReturn(changeDate);
        when(series.getSeriesReplaces()).thenReturn(seriesReplaces);

        givenPublications(latestPublication, livePublication, upcomingEarly, upcomingLate);

        component.doBeforeRender(request, response);

        assertSame(series, attributes.get("series"));

        @SuppressWarnings("unchecked")
        List<PublicationBase> upcomingPublications = (List<PublicationBase>) attributes.get("upcomingPublications");
        assertThat(upcomingPublications, contains(upcomingLate, upcomingEarly));

        @SuppressWarnings("unchecked")
        List<Pair> pastEvents = (List<Pair>) attributes.get("pastPublicationsAndSeriesChanges");
        assertEquals(2, pastEvents.size());
        assertSame(livePublication, pastEvents.get(0).getObject());
        assertSame(seriesReplaces, pastEvents.get(1).getObject());
    }

    @Test
    public void doBeforeRenderHandlesSeriesWithoutLivePublications() throws Exception {
        when(series.getShowLatest()).thenReturn(false);
        when(series.getSeriesReplaces()).thenReturn(null);

        PublicationBase upcomingFirst = publication("upcomingFirst", false, date(2024, Calendar.JANUARY, 12));
        PublicationBase upcomingSecond = publication("upcomingSecond", false, date(2024, Calendar.FEBRUARY, 19));

        givenPublications(upcomingFirst, upcomingSecond);

        component.doBeforeRender(request, response);

        @SuppressWarnings("unchecked")
        List<PublicationBase> upcomingPublications = (List<PublicationBase>) attributes.get("upcomingPublications");
        assertThat(upcomingPublications, contains(upcomingSecond, upcomingFirst));

        @SuppressWarnings("unchecked")
        List<Pair> pastEvents = (List<Pair>) attributes.get("pastPublicationsAndSeriesChanges");
        assertTrue(pastEvents.isEmpty());
    }

    private PublicationBase publication(String name, boolean publiclyAccessible, Date publicationDate) {
        PublicationBase publication = mock(PublicationBase.class, name);
        when(publication.isPubliclyAccessible()).thenReturn(publiclyAccessible);
        when(publication.getNominalPublicationDateCalendar()).thenReturn(publicationDate);
        return publication;
    }

    private Date date(int year, int month, int dayOfMonth) {
        return new GregorianCalendar(year, month, dayOfMonth).getTime();
    }

    private void givenPublications(PublicationBase... publications) throws Exception {
        List<PublicationBase> items = new ArrayList<>(Arrays.asList(publications));
        when(queryResult.getHippoBeans()).thenReturn(new ListHippoBeanIterator(items));
    }

    private static class ListHippoBeanIterator implements HippoBeanIterator {

        private final List<? extends HippoBean> beans;
        private int index = 0;

        private ListHippoBeanIterator(List<? extends HippoBean> beans) {
            this.beans = beans;
        }

        @Override
        public HippoBean next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return beans.get(index++);
        }

        @Override
        public boolean hasNext() {
            return index < beans.size();
        }

        @Override
        public HippoBean nextHippoBean() {
            return next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void skip(int skipNum) {
            index = Math.min(beans.size(), index + skipNum);
        }

        @Override
        public long getSize() {
            return beans.size();
        }

        @Override
        public long getPosition() {
            return index;
        }
    }
}
