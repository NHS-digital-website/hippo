package uk.nhs.digital.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Implement S3Connector to allow uploading files to S3 and managing ACL.
 */
public class S3ConnectorImpl implements S3Connector {

    private String bucketName;
    private AmazonS3 s3;

    public S3ConnectorImpl(AmazonS3 s3, String bucketName) {
        this.bucketName = bucketName;
        this.s3 = s3;
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

    public boolean uploadFile(String objectPath) {
        s3.putObject(new PutObjectRequest(bucketName, objectPath, createSampleFile()));

        return true;
    }

    private static File createSampleFile() {
        try {
            File file = File.createTempFile("aws-java-sdk-", ".txt");
            file.deleteOnExit();

            Writer writer = new OutputStreamWriter(new FileOutputStream(file));
            writer.write("abcdefghijklmnopqrstuvwxyz\n");
            writer.write("01234567890112345678901234\n");
            writer.write("!@#$%^&*()-=[]{};':',.<>/?\n");
            writer.write("01234567890112345678901234\n");
            writer.write("abcdefghijklmnopqrstuvwxyz\n");
            writer.close();

            return file;
        } catch (IOException ioExn) {
            System.out.println(ioExn.toString());
        }

        return null;
    }
}
