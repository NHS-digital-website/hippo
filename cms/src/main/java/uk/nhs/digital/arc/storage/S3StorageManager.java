package uk.nhs.digital.arc.storage;

import jakarta.ws.rs.core.MediaType;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.arc.util.FilePathData;
import uk.nhs.digital.externalstorage.s3.PooledS3Connector;
import uk.nhs.digital.externalstorage.s3.S3ObjectMetadata;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * The S3 implementation of the {@link ArcStorageManager} interface used for
 * operations on incoming large files.
 *
 * We make use of an exsitsing {@link PooledS3Connector} instance to do the actual
 * work
 */
public class S3StorageManager implements ArcStorageManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(S3StorageManager.class);

    protected PooledS3Connector s3Connector;

    public S3StorageManager() {
    }

    public S3StorageManager(PooledS3Connector s3Connector) {
        this.s3Connector = s3Connector;
    }

    public S3ObjectMetadata uploadFileToS3(String docbase, String sourceFilePath) {
        FilePathData sourceFilePathData = new FilePathData(docbase, sourceFilePath);
        String targetFileName = sourceFilePathData.getFilename();
        S3ObjectMetadata metaData = null;

        if (sourceFilePathData.isS3Protocol()) {
            String sourceBucketName = sourceFilePathData.getS3Bucketname();
            String noBucketSourceFilePath = sourceFilePathData.getFilePathNoBucket();

            if (getS3Connector().doesObjectExist(sourceBucketName, noBucketSourceFilePath)) {
                metaData = getS3Connector().copyFileFromOtherBucket(noBucketSourceFilePath, sourceBucketName, targetFileName);
            }
        }
        return metaData;
    }

    @Override
    public boolean fileExists(String fullyReferencedFile) {
        FilePathData filePathData = new FilePathData(fullyReferencedFile);
        return isFileS3AndExists(filePathData);
    }

    @Override
    public boolean fileExists(FilePathData filePathData) {
        return isFileS3AndExists(filePathData);
    }

    private boolean isFileS3AndExists(FilePathData filePathData) {
        if (filePathData.isS3Protocol() && filePathData.getFilePathNoBucket().length() > 0) {
            return getS3Connector().doesObjectExist(filePathData.getS3Bucketname(), filePathData.getFilePathNoBucket());
        }

        return false;
    }

    @Override
    public ArcFileData getFileMetaData(FilePathData sourceFilePathData) {
        if (sourceFilePathData.isS3Protocol()) {
            return getS3Object(sourceFilePathData);
        }
        return null;
    }

    @Override
    public InputStream getFileInputStream(FilePathData filePathData) {
        ArcFileData fileMetaData = getFileMetaData(filePathData);
        return fileMetaData.getDelegateStream();
    }

    @Override
    public void writeOutcomeFile(String docbase, String location, String text) {
        FilePathData sourceFilePathData = new FilePathData(docbase, location);

        if (sourceFilePathData.isS3Protocol()) {
            String sourceBucketName = sourceFilePathData.getS3Bucketname();
            String noBucketSourceFilePath = sourceFilePathData.getFilePathNoBucket();

            try {
                if (!getS3Connector().doesObjectExist(sourceBucketName, noBucketSourceFilePath)) {
                    Supplier<InputStream> inputStream = () -> new ByteArrayInputStream(text.getBytes());
                    getS3Connector().uploadOutcome(inputStream, sourceFilePathData.getS3Bucketname(), noBucketSourceFilePath, MediaType.TEXT_PLAIN);
                }
            } catch (Exception e) {
                LOGGER.error("Unable to write an outcome file for Automatic report creation. "
                    + "Base bucket/folder was {} and filename requested was '{}'", docbase, location);
            }
        }
    }

    private ArcFileData getS3Object(FilePathData filePathData) {
        final AtomicReference<ArcFileData> arcFileData = new AtomicReference<>();

        getS3Connector().download(filePathData.getS3Bucketname(), filePathData.getFilePathNoBucket(),
            s3File -> {
                arcFileData.set(new ArcFileData(s3File.getContent(), s3File.getContentType()));
            }
        );

        return arcFileData.get();
    }

    private PooledS3Connector getS3Connector() {
        if (s3Connector == null) {
            s3Connector = HippoServiceRegistry.getService(PooledS3Connector.class);
        }

        return s3Connector;
    }
}
