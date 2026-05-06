package uk.nhs.digital.ps.beans;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.junit.Test;
import uk.nhs.digital.pagination.Pagination;

import java.util.List;

public class PublicationPageTest {

    @Test
    public void returnsNullPaginationWhenCurrentPageIsOnlyPageInIndex() {
        Publication publication = mock(Publication.class);
        PublicationPage page = publicationPage("Current page", publication);
        List<IndexPage> pageIndex = singletonList(new IndexPageImpl("Current page", mock(HippoBean.class)));
        when(publication.getPageIndex()).thenReturn(pageIndex);

        Pagination pagination = page.paginate();

        assertNull(pagination);
    }

    @Test
    public void returnsPreviousAndNextPagesFromPublicationPageIndex() {
        Publication publication = mock(Publication.class);
        PublicationPage page = publicationPage("Current page", publication);
        IndexPage previousPage = new IndexPageImpl("Previous page", mock(HippoBean.class));
        IndexPage currentPage = new IndexPageImpl("Current page", mock(HippoBean.class));
        IndexPage nextPage = new IndexPageImpl("Next page", mock(HippoBean.class));
        when(publication.getPageIndex()).thenReturn(asList(previousPage, currentPage, nextPage));

        Pagination pagination = page.paginate();

        assertSame(previousPage, pagination.getPrevious());
        assertSame(nextPage, pagination.getNext());
    }

    @Test
    public void returnsNullPaginationWhenPageIsNotInPublicationPageIndex() {
        Publication publication = mock(Publication.class);
        PublicationPage page = publicationPage("Current page", publication);
        when(publication.getPageIndex()).thenReturn(singletonList(
            new IndexPageImpl("Another page", mock(HippoBean.class))
        ));

        Pagination pagination = page.paginate();

        assertNull(pagination);
    }

    private PublicationPage publicationPage(String title, Publication publication) {
        PublicationPage page = spy(new PublicationPage());
        doReturn(title).when(page).getTitle();
        doReturn(publication).when(page).getPublication();
        return page;
    }
}
