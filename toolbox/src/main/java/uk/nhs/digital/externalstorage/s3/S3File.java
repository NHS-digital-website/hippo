package uk.nhs.digital.externalstorage.s3;

import java.io.InputStream;

/**
 * <p>
 * Defines interface of a custom proxy wrapping
 * <a href="https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/s3/model/S3Object.html">
 * com.amazonaws.services.s3.model.S3Object</a>.
 * </p>
 * <p>
 * Allows avoiding exposing the {@code S3Object} to the clients of this module, thus allowing them to avoid having
 * to declare Amazon S3 SDK as their dependency.
 * </p>
 * <p>
 * As soon as it's created, this object holds an open connection to S3. It is thus important to
 * close the stream returned by {@linkplain #getContent()} as soon as the instance is no longer needed, to ensure that
 * connections are left lingering, risking exhausting client's resources.
 * <p>
 * See description of
 * <a href="https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/s3/AmazonS3Client.html#getObject-java.lang.String-java.lang.String-">
 * AmazonS3#getObject</a> for more details.
 * </p>
 */
public interface S3File {

    long getLength();

    InputStream getContent();
}
