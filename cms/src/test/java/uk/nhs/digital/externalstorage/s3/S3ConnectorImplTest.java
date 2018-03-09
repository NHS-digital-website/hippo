package uk.nhs.digital.externalstorage.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.io.InputStream;
import java.util.Random;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

public class S3ConnectorImplTest {


    @Mock private AmazonS3 s3;
    @Mock private S3ObjectKeyGenerator s3ObjectKeyGenerator;

    @Captor private ArgumentCaptor<PutObjectRequest> putObjectRequestArgumentCaptor;

    private String bucketName;
    private String objectKey;
    private String fileName;

    private S3ConnectorImpl s3Connector;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        bucketName = newRandomString();
        fileName = newRandomString();
        objectKey = newRandomString() + "/" + fileName;

        s3Connector = new S3ConnectorImpl(s3, bucketName, s3ObjectKeyGenerator);
    }

    @Test
    public void preservesS3BucketName() throws Exception {

        // given
        // setUp

        // when
        final String actualBucketName = s3Connector.getBucketName();

        // then
        assertThat("S3 bucket name is as provided to the constructor.", actualBucketName, is(bucketName));

    }

    @Test
    public void opensUpS3Resource_onPublishRequest() throws Exception {

        // given
        final AccessControlList accessControlList = mock(AccessControlList.class);
        given(s3.getObjectAcl(bucketName, objectKey)).willReturn(accessControlList);

        // when
        s3Connector.publishResource(objectKey);

        // then
        then(s3).should().getObjectAcl(bucketName, objectKey);
        then(accessControlList).should().grantPermission(GroupGrantee.AllUsers, Permission.Read);
    }

    @Test
    public void locksDownS3Resource_onUnPublishRequest() throws Exception {

        // given
        final AccessControlList accessControlList = mock(AccessControlList.class);
        given(s3.getObjectAcl(bucketName, objectKey)).willReturn(accessControlList);

        // when
        s3Connector.unpublishResource(objectKey);

        // then
        then(s3).should().getObjectAcl(bucketName, objectKey);
        then(accessControlList).should().revokeAllPermissions(GroupGrantee.AllUsers);
    }

    @Test
    public void uploadsFileAsS3Resource() throws Exception {

        // given
        final String contentType = newRandomString();
        final long contentLength = newRandomLong(); // includes negative values which doesn't matter here
        final String s3ObjectUrl = "https://" + bucketName + "/" + objectKey;

        given(s3ObjectKeyGenerator.generateObjectKey(fileName)).willReturn(objectKey);

        final InputStream expectedUploadedFileInputStream = mock(InputStream.class);

        final ObjectMetadata resultMetadata = mock(ObjectMetadata.class);
        given(resultMetadata.getContentType()).willReturn(contentType);
        given(resultMetadata.getContentLength()).willReturn(contentLength);

        given(s3.getObjectMetadata(bucketName, objectKey)).willReturn(resultMetadata);

        // when
        final S3ObjectMetadata actualMetadata =
            s3Connector.uploadFile(expectedUploadedFileInputStream, fileName, contentType);

        // then
        then(s3).should().putObject(putObjectRequestArgumentCaptor.capture());
        then(s3).should().getObjectMetadata(bucketName, objectKey);

        //   assert upload request

        final PutObjectRequest putObjectRequest = putObjectRequestArgumentCaptor.getValue();
        assertThat("Upload is made to the expected S3 bucket.", putObjectRequest.getBucketName(), is(bucketName));
        assertThat("Upload is made with the expected object key.", putObjectRequest.getKey(), is(objectKey));
        assertThat("Upload is made with the provided file content.", putObjectRequest.getInputStream(),
            is(sameInstance(expectedUploadedFileInputStream))
        );

        //   assert upload response metadata

        assertThat("Upload response metadata reports correct file name", actualMetadata.getFileName(), is(fileName));
        assertThat("Upload response metadata reports correct content type", actualMetadata.getMimeType(), is(contentType));
        assertThat("Upload response metadata reports correct S3 reference", actualMetadata.getReference(), is(objectKey));
        assertThat("Upload response metadata reports correct content size", actualMetadata.getSize(), is(contentLength));
        assertThat("Upload response metadata reports correct S3 object URL", actualMetadata.getUrl(), is(s3ObjectUrl));
    }

    @Test
    public void retrievesS3Resource() throws Exception {

        // given
        final long contentLength = newRandomLong(); // includes negative values which doesn't matter here
        final S3ObjectInputStream expectedDownloadedFileInputStream = mock(S3ObjectInputStream.class);

        final ObjectMetadata objectMetadata = mock(ObjectMetadata.class);
        given(objectMetadata.getContentLength()).willReturn(contentLength);

        final S3Object s3ResponseObject = mock(S3Object.class);
        given(s3ResponseObject.getObjectContent()).willReturn(expectedDownloadedFileInputStream);
        given(s3ResponseObject.getObjectMetadata()).willReturn(objectMetadata);


        given(s3.getObject(bucketName, objectKey)).willReturn(s3ResponseObject);

        // when
        final S3File actualS3File = s3Connector.getFile(objectKey);

        // then
        assertThat("Returned content stream is the one returned by S3.", actualS3File.getContent(),
            is(sameInstance(expectedDownloadedFileInputStream))
        );
        assertThat("Returned content length is as returned by S3.", actualS3File.getLength(),
            is(contentLength)
        );
    }

    private String newRandomString() {
        return UUID.randomUUID().toString();
    }

    private long newRandomLong() {
        return new Random().nextLong();
    }
}
