package uk.nhs.digital.arc.storage;

import uk.nhs.digital.arc.util.FilePathData;
import uk.nhs.digital.externalstorage.s3.S3ObjectMetadata;

import java.io.InputStream;

/**
 * The storage manager helps the querying/downloading/uploading of files with regards
 * to a specific type of stoirage lcoation.
 * The functionality is implemented in concrete classes which implement this interface,
 * the only one of which at the moment is {@link S3StorageManager} although the interface
 * has been generated to allow it to be implementation independent in terms of the incoming
 * file details
 */
public interface ArcStorageManager {
    S3ObjectMetadata uploadFileToS3(String docbase, String sourceFilePath);

    boolean fileExists(String fullyReferencedFile);

    boolean fileExists(FilePathData filePathData);

    InputStream getFileInputStream(FilePathData filePathData);

    ArcFileData getFileMetaData(FilePathData sourceFilePathData);
}
