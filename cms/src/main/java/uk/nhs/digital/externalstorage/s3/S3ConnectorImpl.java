package uk.nhs.digital.externalstorage.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;

import java.io.InputStream;

/**
 * Implement S3Connector to allow uploading files to S3 and managing ACL.
 */
public class S3ConnectorImpl implements S3Connector {

    private String bucketName;
    private AmazonS3 s3;
    private final S3ObjectKeyGenerator s3ObjectKeyGenerator;

    public S3ConnectorImpl(AmazonS3 s3, String bucketName, S3ObjectKeyGenerator s3ObjectKeyGenerator) {
        this.bucketName = bucketName;
        this.s3 = s3;
        this.s3ObjectKeyGenerator = s3ObjectKeyGenerator;
    }

    public String getBucketName() {
        return bucketName;
    }

    public boolean publishResource(String objectPath) {
        AccessControlList acl = s3.getObjectAcl(bucketName, objectPath);
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
        s3.setObjectAcl(bucketName, objectPath, acl);

        return true;
    }

    public boolean unpublishResource(String objectPath) {
        AccessControlList acl = s3.getObjectAcl(bucketName, objectPath);
        acl.revokeAllPermissions(GroupGrantee.AllUsers);
        s3.setObjectAcl(bucketName, objectPath, acl);

        return true;
    }

    public S3ObjectMetadata uploadFile(InputStream fileStream, String fileName, String contentType) {
        String objectKey = s3ObjectKeyGenerator.generateObjectKey(fileName);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);

        s3.putObject(new PutObjectRequest(bucketName, objectKey, fileStream, metadata));

        // The above put request returns metatdata object but it's empty,
        // hence the need for a separate call to fetch actual metadata.
        ObjectMetadata resultMetadata = s3.getObjectMetadata(bucketName, objectKey);

        return new S3ObjectMetadataImpl(resultMetadata, bucketName, objectKey);
    }

    public S3File getFile(String objectPath) {
        return new S3FileProxy(s3.getObject(bucketName, objectPath));
    }


}
