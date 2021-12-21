package uk.nhs.digital.externalstorage.s3;

import java.io.InputStream;
import java.util.Map;

public interface S3Connector {

    void publishResource(String objectPath);

    void tagResource(String objectPath, Map<String, String> tags);

    void unpublishResource(String objectPath);

    S3ObjectMetadata copyFileFromOtherBucket(final String sourceObjectPath, final String sourceBucketName, final String fileName);

    S3ObjectMetadata copyFile(String sourceObjectPath, String fileName);

    S3ObjectMetadata uploadFile(InputStream fileStream, String objectPath, String contentType);


    /**
     * Determine whether an Object exists in a nominated bucket
     * @param otherBucket is any bucket you want to check (subject to IAM etc)
     * @param objectPath is the location of the object you want to check
     * @return indication of success or failure
     */
    boolean doesObjectExist(String otherBucket, String objectPath);

    /**
     * Determine whether an object exists in the target bucket used for uploading large files
     * @param objectPath the location of the object that you want to check
     * @return indication of success or failure
     */
    boolean doesObjectExist(String objectPath);

    /**
     * <p>
     * Retrieves a file identified by the parameter from S3.
     * </p>
     * <p>
     * Aim to call this method once and make sure to close the stream returned by
     * {@linkplain S3File#getContent()} as soon as the {@linkplain S3File} instance is no longer needed. See
     * description of {@linkplain S3File#getContent()} and of
     * <a href="https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/s3/AmazonS3Client.html#getObject-java.lang.String-java.lang.String-">
     * AmazonS3#getObject</a> method for more details.
     * </p>
     *
     * @param namedBucket is the name of the bucket to pull the object from
     * @param objectPath Path, uniquely idendifying the file in S3.
     *
     * @return Proxy to S3 file.
     */
    S3File downloadFileFromNamedBucket(String namedBucket, String objectPath);

    /**
     * Shortcut method to download a file from the bucket used for offloading large files in BReach
     * @param objectPath unqiue identifier of the file in the default bucket
     * @return a S3Proxy to the object
     */
    S3File downloadFile(String objectPath);

    /**
     * Determine the default bucket used by this connector
     * @return the name of the bucket
     */
    String getBucketName();
}
