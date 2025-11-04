package uk.nhs.digital.common;

import static org.hippoecm.repository.api.HippoNodeType.HIPPO_TEXT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.nhs.digital.common.galleryprocessors.CustomGalleryProcessor;

import java.io.InputStream;
import java.util.*;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeDefinition;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*"})
@PrepareForTest({CustomGalleryProcessor.class})
public class CustomGalleryProcessorTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Node node;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Node nodeItem;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Node assetNode;

    private CustomGalleryProcessor processor;

    @Before
    public void setUp() throws RepositoryException {
        processor = new CustomGalleryProcessor();

        when(node.getPrimaryNodeType().getPrimaryItemName()).thenReturn("website:customizedAssetSet");
        when(node.getSession().getItem(any())).thenReturn(nodeItem);
        when(nodeItem.isNode()).thenReturn(true);
        when(node.getPrimaryNodeType().getChildNodeDefinitions()).thenReturn(new NodeDefinition[]{});
        when(node.hasNode("hippogallery:asset")).thenReturn(true);
        when(node.getNode("hippogallery:asset")).thenReturn(assetNode);
    }

    @Test
    public void setHippoTextToEmptyOnPdfs() throws Exception {
        //when uploading a pdf
        processor.makeImage(node, InputStream.nullInputStream(), "application/pdf", "myfile.pdf");

        //verify the hippo:text property is set to empty
        verify(assetNode).setProperty(HIPPO_TEXT, "");
    }

    @Test
    public void setHippoTextToEmptyOnOtherAssetTypes() throws Exception {
        //when uploading an asset that's not a pdf
        processor.makeImage(node, InputStream.nullInputStream(),
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "myfile.docx");

        //verify the hippo:text property is set to empty
        verify(assetNode).setProperty(HIPPO_TEXT, "");
    }
}
