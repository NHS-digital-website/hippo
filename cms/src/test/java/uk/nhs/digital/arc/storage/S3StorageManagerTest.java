package uk.nhs.digital.arc.storage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.nhs.digital.arc.util.FilePathData;
import uk.nhs.digital.externalstorage.s3.PooledS3Connector;
import uk.nhs.digital.externalstorage.s3.S3ObjectKeyGenerator;
import uk.nhs.digital.externalstorage.s3.S3ObjectMetadata;

public class S3StorageManagerTest {

    private static final String SOURCE_BUCKET = "source_bucket";
    private static final String TARGET_BUCKET = "target_bucket";
    private static final String S3_SOURCE_BUCKET = "s3://source_bucket";
    private static final String HTTP_SOURCE_BUCKET = "http://source_bucket";

    private static final String OBJECT_KEY = "object_key";
    private static final String OBJECT_FOLDER_OBJECT_KEY = "object_folder/object_key";
    private static final String OBJECT_FOLDER_OBJECT_KEY_2 = "object_folder/object_key2";
    private static final String XYZ_OBJECT_KEY = "x/y/z/object_key";

    @Mock
    S3ObjectKeyGenerator mockObjectKeyGenerator;

    @Mock
    PooledS3Connector mockPooledS3Connector;

    @Mock
    S3ObjectMetadata mockMetaData;

    @Before
    public void setUp() {
        openMocks(this);
    }

    @Test
    public void validateFileExistsConditions() {
        S3StorageManager sm = new S3StorageManager(mockPooledS3Connector);

        when(mockPooledS3Connector.doesObjectExist(SOURCE_BUCKET, OBJECT_FOLDER_OBJECT_KEY)).thenReturn(false);
        assertFalse(sm.fileExists(new FilePathData(S3_SOURCE_BUCKET, OBJECT_FOLDER_OBJECT_KEY)));

        when(mockPooledS3Connector.doesObjectExist(SOURCE_BUCKET, OBJECT_FOLDER_OBJECT_KEY_2)).thenReturn(true);
        assertTrue(sm.fileExists(new FilePathData(S3_SOURCE_BUCKET, OBJECT_FOLDER_OBJECT_KEY_2)));
    }

    @Test
    public void shouldNotBeAbleToUploadNonS3File() {
        S3StorageManager sm = new S3StorageManager(mockPooledS3Connector);

        S3ObjectMetadata meta = sm.uploadFileToS3(HTTP_SOURCE_BUCKET, OBJECT_FOLDER_OBJECT_KEY);
        assertNull(meta);
    }

    @Test
    public void shouldNotBeAbleToUploadS3FileThatDoesNonExistInSourceBucket() {
        S3StorageManager sm = new S3StorageManager(mockPooledS3Connector);
        when(mockPooledS3Connector.doesObjectExist(SOURCE_BUCKET, OBJECT_FOLDER_OBJECT_KEY_2)).thenReturn(false);

        S3ObjectMetadata meta = sm.uploadFileToS3(S3_SOURCE_BUCKET, OBJECT_FOLDER_OBJECT_KEY_2);
        assertNull(meta);

        verify(mockPooledS3Connector, times(1)).doesObjectExist(SOURCE_BUCKET, OBJECT_FOLDER_OBJECT_KEY_2);
        verifyNoMoreInteractions(mockPooledS3Connector);
    }

    @Test
    public void shouldBeAbleToUploadS3FileThatDoesExistInSourceBucket() {
        S3StorageManager sm = new S3StorageManager(mockPooledS3Connector);

        // given
        when(mockPooledS3Connector.doesObjectExist(SOURCE_BUCKET, OBJECT_FOLDER_OBJECT_KEY)).thenReturn(true);
        when(mockObjectKeyGenerator.generateObjectKey(OBJECT_KEY)).thenReturn(XYZ_OBJECT_KEY);
        when(mockPooledS3Connector.copyFileFromOtherBucket(OBJECT_FOLDER_OBJECT_KEY, SOURCE_BUCKET, OBJECT_KEY)).thenReturn(mockMetaData);

        // when
        S3ObjectMetadata meta = sm.uploadFileToS3(S3_SOURCE_BUCKET, OBJECT_FOLDER_OBJECT_KEY);

        // then
        assertNotNull(meta);
    }
}