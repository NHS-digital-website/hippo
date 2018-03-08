package uk.nhs.digital.externalstorage.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import javax.xml.bind.DatatypeConverter;

/**
 * Implement S3Connector to allow uploading files to S3 and managing ACL.
 */
public class S3ConnectorImpl implements S3Connector {

    private String bucketName;
    private AmazonS3 s3;
    private final MessageDigest md;

    public S3ConnectorImpl(AmazonS3 s3, String bucketName) {
        this.bucketName = bucketName;
        this.s3 = s3;
        md = getMd5MessageDigest();
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
        String objectKey = generateObjectKey(fileName);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);

        s3.putObject(new PutObjectRequest(bucketName, objectKey, fileStream, metadata));
        ObjectMetadata resultMetadata = s3.getObjectMetadata(bucketName, objectKey);

        return new S3ObjectMetadataImpl(resultMetadata, bucketName, objectKey);
    }

    public long getFileSize(String objectPath) {
        ObjectMetadata metadata = s3.getObjectMetadata(bucketName, objectPath);
        return metadata.getInstanceLength();
    }

    private String generateObjectKey(String fileName) {
        String fileNameSalt = UUID.randomUUID().toString();
        md.update((fileName + fileNameSalt).getBytes());
        byte[] digest = md.digest();

        String hash = DatatypeConverter
            .printHexBinary(digest).toUpperCase();

        Path path = Paths.get(hash.substring(0,2), hash.substring(2,8), fileName);

        return path.toString();
    }

    private MessageDigest getMd5MessageDigest() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Error initialising MD5 MessageDigest in " + S3ConnectorImpl.class.getName(), ex);
        }
    }
}
