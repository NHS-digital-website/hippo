package uk.nhs.digital.externalstorage.s3;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.model.UploadPartResult;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class S3SdkConnectorTest {

    private static Random random = new Random();
    private static final int BUFFER_SIZE = 5 * 1024 * 1024;

    @Mock private AmazonS3 s3;
    @Mock private S3ObjectKeyGenerator s3ObjectKeyGenerator;

    private String bucketName;
    private String objectKey;
    private String fileName;

    private S3SdkConnector s3Connector;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        bucketName = newRandomString();
        fileName = newRandomString();
        objectKey = newRandomString() + "/" + fileName;

        s3Connector = new S3SdkConnector(s3, bucketName, s3ObjectKeyGenerator);
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
    public void uploadsFileAsS3Resource_usingMultipartRequest() throws Exception {

        // given
        final String contentType = newRandomString();
        final String uploadId = newRandomString();
        final String s3ObjectUrl = "https://" + bucketName + "/" + objectKey;

        final byte[] fullChunk = newRandomByteArray(BUFFER_SIZE);
        final byte[] partialChunk = newRandomByteArray(1024);
        final byte[][] expectedChunks = {fullChunk, partialChunk};
        final long contentLength = fullChunk.length + partialChunk.length;

        given(s3ObjectKeyGenerator.generateObjectKey(fileName)).willReturn(objectKey);

        final InitiateMultipartUploadResult result = mock(InitiateMultipartUploadResult.class);
        given(result.getUploadId()).willReturn(uploadId);
        given(s3.initiateMultipartUpload(any())).willReturn(result);

        final PartETag firstPartETag = mock(PartETag.class);
        final PartETag finalPartETag = mock(PartETag.class);
        final UploadPartResult uploadPartResult = mock(UploadPartResult.class);
        given(uploadPartResult.getPartETag()).willReturn(firstPartETag, finalPartETag);
        given(s3.uploadPart(any(UploadPartRequest.class))).willReturn(uploadPartResult);

        final ObjectMetadata resultMetadata = mock(ObjectMetadata.class);
        given(resultMetadata.getContentType()).willReturn(contentType);
        given(resultMetadata.getContentLength()).willReturn(contentLength);
        given(s3.getObjectMetadata(bucketName, objectKey)).willReturn(resultMetadata);

        final InputStream uploadedFileInputStream = new InputStreamStub(expectedChunks);

        // when
        final S3ObjectMetadata actualMetadata = s3Connector.uploadFile(uploadedFileInputStream, fileName, contentType);

        // then

        //   assert upload request - initiation
        final ArgumentCaptor<InitiateMultipartUploadRequest> initiateRequestArgCaptor
            = ArgumentCaptor.forClass(InitiateMultipartUploadRequest.class);
        then(s3).should().initiateMultipartUpload(initiateRequestArgCaptor.capture());
        final InitiateMultipartUploadRequest initRequest = initiateRequestArgCaptor.getValue();
        assertThat("Upload to expected S3 bucket is initiated.", initRequest.getBucketName(), is(bucketName));
        assertThat("Upload is initiated with expected object key.", initRequest.getKey(), is(objectKey));

        //   assert upload request - both chunks (full and partial) of the upload itself
        final ArgumentCaptor<UploadPartRequest> uploadPartRequestArgCaptor = ArgumentCaptor.forClass(UploadPartRequest.class);
        then(s3).should(times(2)).uploadPart(uploadPartRequestArgCaptor.capture());
        final List<UploadPartRequest> actualUploadRequests = uploadPartRequestArgCaptor.getAllValues();

        for (int i = 0; i < expectedChunks.length; i++) {
            final byte[] expectedChunk = expectedChunks[i];
            final UploadPartRequest uploadPartRequest = actualUploadRequests.get(i);

            assertThat("Chunk " + i + " uploaded to correct bucket", uploadPartRequest.getBucketName(), is(bucketName));
            assertThat("Chunk " + i + " uploaded with correct object key", uploadPartRequest.getKey(), is(objectKey));
            assertThat("Chunk " + i + " uploaded with correct upload id", uploadPartRequest.getUploadId(), is(uploadId));
            assertThat("Chunk " + i + " uploaded with correct part number", uploadPartRequest.getPartNumber(), is(i + 1));
            assertThat("Chunk " + i + " uploaded with correct part size", uploadPartRequest.getPartSize(),
                is(Long.valueOf(expectedChunk.length))
            );

            final byte[] actualChunk = new byte[expectedChunk.length];
            try (final InputStream uploadPartRequestInputStream = uploadPartRequest.getInputStream()) {
                IOUtils.read(uploadPartRequestInputStream, actualChunk, 0, actualChunk.length);
            }

            assertThat("Chunk " + i + " uploaded with correct content", actualChunk, is(expectedChunk));
        }

        //   assert upload request - finalisation
        final ArgumentCaptor<CompleteMultipartUploadRequest> completeRequestArgCaptor =
            ArgumentCaptor.forClass(CompleteMultipartUploadRequest.class);
        then(s3).should().completeMultipartUpload(completeRequestArgCaptor.capture());
        final CompleteMultipartUploadRequest completeMultipartUploadRequest = completeRequestArgCaptor.getValue();
        assertThat("Complete upload request with correct bucket name", completeMultipartUploadRequest.getBucketName(), is(bucketName));
        assertThat("Complete upload request with correct object key", completeMultipartUploadRequest.getKey(), is(objectKey));
        assertThat("Complete upload request with correct upload id", completeMultipartUploadRequest.getUploadId(), is(uploadId));
        assertThat("Complete upload request with correct upload id", completeMultipartUploadRequest.getPartETags(), is(
            asList(firstPartETag, finalPartETag)
        ));

        //   assert upload response metadata
        then(s3).should().getObjectMetadata(bucketName, objectKey);
        assertNewResourceMetadata(
            contentType, s3ObjectUrl, contentLength, objectKey, fileName, actualMetadata
        );
    }

    @Test
    public void abortsS3MultipartUploadRequest_onChunkUploadFailure() throws Exception {

        // given
        final String contentType = newRandomString();
        final String uploadId = newRandomString();

        given(s3ObjectKeyGenerator.generateObjectKey(fileName)).willReturn(objectKey);

        final InitiateMultipartUploadResult result = mock(InitiateMultipartUploadResult.class);
        given(result.getUploadId()).willReturn(uploadId);
        given(s3.initiateMultipartUpload(any())).willReturn(result);

        final InputStream uploadedFileInputStream = mock(InputStream.class);

        final RuntimeException expectedCauseException = new RuntimeException(newRandomString());
        given(s3.uploadPart(any(UploadPartRequest.class))).willThrow(expectedCauseException);

        try {
            // when
            s3Connector.uploadFile(uploadedFileInputStream, fileName, contentType);

            // then
            fail("Exception was expected but none has been thrown.");
        } catch (final RuntimeException actualException) {
            //   assert upload request - abort
            final ArgumentCaptor<AbortMultipartUploadRequest> abortMultipartUploadRequestArgumentCaptor =
                ArgumentCaptor.forClass(AbortMultipartUploadRequest.class);
            then(s3).should().abortMultipartUpload(abortMultipartUploadRequestArgumentCaptor.capture());
            final AbortMultipartUploadRequest actualAbortRequest = abortMultipartUploadRequestArgumentCaptor.getValue();
            assertThat("Request aborted with correct bucket name", actualAbortRequest.getBucketName(), is(bucketName));
            assertThat("Request aborted with correct object key", actualAbortRequest.getKey(), is(objectKey));
            assertThat("Request aborted with correct upload id", actualAbortRequest.getUploadId(), is(uploadId));

            //   assert upload request - exception
            assertThat("Exception is thrown with correct message.", actualException.getMessage(),
                is("Failed to upload file " + objectKey)
            );
            assertThat("Exception is thrown with correct cause.", actualException.getCause(),
                is(sameInstance(expectedCauseException))
            );
        }
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
        final S3File actualS3File = s3Connector.downloadFile(objectKey);

        // then
        assertThat("Returned content stream is the one returned by S3.", actualS3File.getContent(),
            is(sameInstance(expectedDownloadedFileInputStream))
        );
        assertThat("Returned content length is as returned by S3.", actualS3File.getLength(),
            is(contentLength)
        );
    }

    @Test
    public void copiesS3Resource() throws Exception {

        // given
        final String expectedTargetObjectKey = newRandomString() + "/" + fileName;
        final String expectedTargetObjectUrl = "https://" + bucketName + "/" + expectedTargetObjectKey;
        final String expectedContentType = newRandomString();
        final long expectedContentLength = newRandomLong();

        given(s3ObjectKeyGenerator.generateObjectKey(fileName)).willReturn(expectedTargetObjectKey);

        final ObjectMetadata expectedObjectMetadata = mock(ObjectMetadata.class);
        given(expectedObjectMetadata.getContentLength()).willReturn(expectedContentLength);
        given(expectedObjectMetadata.getContentType()).willReturn(expectedContentType);

        given(s3.getObjectMetadata(bucketName, expectedTargetObjectKey)).willReturn(expectedObjectMetadata);

        // when
        final S3ObjectMetadata actualTargetObjectMetadata = s3Connector.copyFile(objectKey, fileName);

        // then
        then(s3).should().copyObject(bucketName, objectKey, bucketName, expectedTargetObjectKey);

        assertNewResourceMetadata(
            expectedContentType,
            expectedTargetObjectUrl,
            expectedContentLength,
            expectedTargetObjectKey,
            fileName,
            actualTargetObjectMetadata
        );
    }

    private void assertNewResourceMetadata(final String expectedContentType,
                                           final String expectedUrl,
                                           final long expectedContentLength,
                                           final String expectedObjectKey,
                                           final String expectedFileName,
                                           final S3ObjectMetadata actualMetadata
    ) {
        assertThat("Upload response metadata reports correct file name",
            actualMetadata.getFileName(), is(expectedFileName));
        assertThat("Upload response metadata reports correct content type",
            actualMetadata.getMimeType(), is(expectedContentType));
        assertThat("Upload response metadata reports correct S3 reference",
            actualMetadata.getReference(), is(expectedObjectKey));
        assertThat("Upload response metadata reports correct content size",
            actualMetadata.getSize(), is(expectedContentLength));
        assertThat("Upload response metadata reports correct S3 object URL",
            actualMetadata.getUrl(), is(expectedUrl));
    }

    private String newRandomString() {
        return UUID.randomUUID().toString();
    }

    private long newRandomLong() {
        return new Random().nextLong();
    }

    private static byte[] newRandomByteArray(final int length) {

        final byte[] array = new byte[length];

        random.nextBytes(array);

        return array;
    }

    /**
     * Accepts individual chunks (via constructor) that are then returned one by one on subsequent calls to
     * {@linkplain #read(byte[], int, int)}.
     */
    private class InputStreamStub extends InputStream {

        private final byte[][] chunks;

        private int chunkToReadIndex = 0;

        public InputStreamStub(final byte[][] chunks) {
            this.chunks = chunks;
        }

        @Override
        public int read() throws IOException {
            throw new UnsupportedOperationException("Not applicable.");
        }

        @Override
        public int read(final byte[] buffer, final int offset, final int length) throws IOException {

            assertThat("Asked for no more than the expected number of chunks + 1.", chunkToReadIndex,
                is(lessThanOrEqualTo(chunks.length))
            );
            assertThat("Asked for a chunk up to expected length.", length, is(BUFFER_SIZE));
            assertThat("Asked for a chunk with expected offset.", offset, is(0));

            if (chunkToReadIndex == chunks.length) {
                return -1;
            }

            final byte[] chunk = chunks[chunkToReadIndex];

            System.arraycopy(chunk, 0, buffer, 0, chunk.length);

            chunkToReadIndex++;

            return chunk.length;
        }
    }
}
