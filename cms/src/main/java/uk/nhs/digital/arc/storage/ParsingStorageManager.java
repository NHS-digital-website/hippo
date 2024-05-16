package uk.nhs.digital.arc.storage;

import com.amazonaws.services.s3.model.ObjectMetadata;
import uk.nhs.digital.arc.util.FilePathData;
import uk.nhs.digital.externalstorage.s3.PooledS3Connector;
import uk.nhs.digital.externalstorage.s3.S3ObjectMetadata;
import uk.nhs.digital.externalstorage.s3.S3ObjectMetadataImpl;

/**
 * The S3 implementation of the {@link ArcStorageManager} interface used for
 * operations on incoming large files.
 *
 * We make use of an exsitsing {@link PooledS3Connector} instance to do the actual
 * work
 */
public class ParsingStorageManager extends S3StorageManager {

    public ParsingStorageManager() {
    }

    /**
     * This is fake upload in that we generate Metadata based on what we have been given but we don't actually add content.
     * It's designed to be used for preview only
     * @param docbase is the standard docbase from the manifest file
     * @param sourceFilePath is the file that we ordinarailly would want to have uploaded but actually we are just interrogating
     * @return is the metdata that will be referenced elsewhere
     */
    public S3ObjectMetadata uploadFileToS3(String docbase, String sourceFilePath) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("*/*");

        FilePathData bucketAndPath = new FilePathData(sourceFilePath);
        String bucketName = bucketAndPath.getS3Bucketname();
        String objectKey = bucketAndPath.getFolderPathNoBucket();

        S3ObjectMetadata metaData = new S3ObjectMetadataImpl(objectMetadata, bucketName, objectKey);

        return metaData;
    }
}
