package uk.nhs.digital.externalstorage.resource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.apache.sling.testing.mock.jcr.MockJcr;
import org.junit.Test;
import uk.nhs.digital.externalstorage.ExternalStorageConstants;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class S3NodeMetadataTest {

    private static final String VALUE_FILE_NAME  = "filename.pdf";
    private static final long VALUE_SIZE = 123456;
    private static final String VALUE_MIME_TYPE = "application/pdf";

    @Test
    public void shouldGiveAccessToFields() {
        S3NodeMetadata actualMetadata = new S3NodeMetadata(getNode());

        assertThat("metadata contains valid .", actualMetadata.getFileName(), equalTo(VALUE_FILE_NAME));
        assertThat("metadata contains valid .", actualMetadata.getMimeType(), equalTo(VALUE_MIME_TYPE));
        assertThat("metadata contains valid .", actualMetadata.getSize(), equalTo(VALUE_SIZE));
    }

    private Node getNode() {
        Repository repository = MockJcr.newRepository();

        try {
            Session session = repository.login();

            Node rootNode = session.getRootNode();
            Node node = rootNode.addNode("resource");

            node.setProperty(ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_FILE_NAME, VALUE_FILE_NAME);
            node.setProperty(ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_SIZE, VALUE_SIZE);
            node.setProperty(ExternalStorageConstants.PROPERTY_EXTERNAL_STORAGE_MIME_TYPE, VALUE_MIME_TYPE);

            return node;
        } catch (RepositoryException repositoryEx) {
            throw new RuntimeException("Failed to set property on JCR node", repositoryEx);
        }
    }
}
