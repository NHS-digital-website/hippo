package uk.nhs.digital.externalstorage.s3;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.junit.Test;
import org.mockito.Mock;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class S3ConnectorImplTest {

    @Mock
    private AmazonS3 s3;
    private String bucketName = "test.bucket.name";

    @Test
    public void shouldGenerateObjectKey() {
        initMocks(this);

        given(s3.putObject(any()))
            .willReturn(new PutObjectResult());
        given(s3.getObjectMetadata(anyString(), anyString()))
            .willReturn(new ObjectMetadata());

        String fileName = "test.pdf";
        S3ConnectorImpl subject = new S3ConnectorImpl(s3, bucketName);
        S3ObjectMetadata metadata = subject.uploadFile(
            new ByteArrayInputStream("sample text".getBytes()),
            fileName,
            "application/pdf"
        );

        String actualKey = metadata.getReference();
        Path path = Paths.get(actualKey);
        assertThat("path contains filename at the end.", path.getName(2).toString(), equalTo(fileName));
        assertThat("path's second part is 6 characters long.", path.getName(1).toString().length(), equalTo(6));
        assertThat("path's first part is 2 characters long.", path.getName(0).toString().length(), equalTo(2));
    }
}
