package uk.nhs.digital.externalstorage.s3;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.amazonaws.services.s3.model.ObjectMetadata;
import org.junit.Test;

public class S3ObjectMetadataImplTest {

    @Test
    public void shouldReturnMetadata() {
        String bucketName = "test.bucket";
        String objectKey = "A3/C814DE/test.pdf";
        String contentType = "test/type";
        long size = 123456;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(size);

        S3ObjectMetadata actual = new S3ObjectMetadataImpl(metadata, bucketName, objectKey);

        assertThat("file name is correct", actual.getFileName(), equalTo("test.pdf"));
        assertThat("reference is correct", actual.getReference(), equalTo(objectKey));
        assertThat("content type is correct", actual.getMimeType(), equalTo(contentType));
        assertThat("url is correct", actual.getUrl(), equalTo("https://" + bucketName + "/" + objectKey));
        assertThat("size is correct", actual.getSize(), equalTo(size));
    }

}
