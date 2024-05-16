package uk.nhs.digital.arc.util;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import junit.framework.TestCase;
import org.hippoecm.repository.api.HippoNodeType;
import org.junit.Before;
import org.mockito.Mock;
import org.onehippo.forge.content.exim.core.DocumentManager;

import java.util.Arrays;
import java.util.List;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;

public class PageUtilsTest extends TestCase {

    @Mock
    Session mockSession;

    @Mock
    Node mockFolderNode;

    @Mock
    Node mockPageNode;

    @Mock
    NodeType mockPrimaryNode;

    @Mock
    NodeIterator mockNodeIterator;

    @Mock
    DocumentManager mockDocumentManager;

    @Before
    public void setUp() {
        openMocks(this);
    }

    public void testGetReportRootFolderFound() {
        PageUtils pageUtils = new PageUtils(mockSession, "/fred/bed/red/node");
        assertEquals("/fred/bed/red", pageUtils.getReportRootFolder());
    }

    public void testGetExistingPagesWhereJustFirstIsPage() throws RepositoryException {
        //* given
        setUpMockResponses(HippoNodeType.NT_HANDLE, HippoNodeType.NT_COMPOUND);

        //* when
        PageUtils pu = new PageUtils(mockSession, "/fred/bed/red/node");

        //* then
        List<String> existingPages = pu.getExistingPages();
        assertEquals(1, existingPages.size());
        assertEquals("/a/b/c", existingPages.get(0));
    }

    public void testGetExistingPagesWhereLastIsPage() throws RepositoryException {
        //* given
        setUpMockResponses(HippoNodeType.NT_COMPOUND, HippoNodeType.NT_HANDLE);

        //* when
        PageUtils pu = new PageUtils(mockSession, "/fred/bed/red/node");

        //* then
        List<String> existingPages = pu.getExistingPages();
        assertEquals(1, existingPages.size());
        assertEquals("/a/b/c", existingPages.get(0));
    }

    public void testGetExistingPagesWhereBothArePage() throws RepositoryException {
        //* given
        setUpMockResponses(HippoNodeType.NT_HANDLE, HippoNodeType.NT_HANDLE);

        //* when
        PageUtils pu = new PageUtils(mockSession, "/fred/bed/red/node");

        //* then
        List<String> existingPages = pu.getExistingPages();
        assertEquals(2, existingPages.size());
        assertEquals("/a/b/c", existingPages.get(0));
        assertEquals("/a/b/c", existingPages.get(1));
    }

    public void testRemoveOrphanedPages() {
        //* when

        //* given
        PageUtils pu = new PageUtils(mockSession, "/a");
        pu.setDocumentManager(mockDocumentManager);

        pu.removeOrphanedPages(Arrays.asList("/a/1", "/a/2"), Arrays.asList("/a/1"));

        //* then
        verify(mockDocumentManager, times(1)).deleteDocument("/a/2");
    }




    private void setUpMockResponses(String type1, String type2) throws RepositoryException {
        when(mockSession.getNode("/fred/bed/red")).thenReturn(mockFolderNode);
        when(mockFolderNode.getNodes()).thenReturn(mockNodeIterator);

        when(mockNodeIterator.hasNext()).thenReturn(true, true, false);

        when(mockNodeIterator.nextNode()).thenReturn(mockPageNode);

        when(mockPageNode.getPrimaryNodeType()).thenReturn(mockPrimaryNode);
        when(mockPageNode.getPath()).thenReturn("/a/b/c");

        when(mockPrimaryNode.getName()).thenReturn(type1, type2);
    }

}