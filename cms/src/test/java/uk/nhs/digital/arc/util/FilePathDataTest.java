package uk.nhs.digital.arc.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FilePathDataTest {

    @Test
    public void testEqualityWhenPathSupplied() {
        FilePathData fpThis = new FilePathData("XYZ");
        FilePathData fpThat = new FilePathData("XYZ");

        assertEquals(fpThis, fpThat);
    }

    @Test
    public void testEqualityWhenThatIsNull() {
        FilePathData fpThis = new FilePathData("XYZ");
        FilePathData fpThat = new FilePathData(null);

        assertNotEquals(fpThis, fpThat);
    }

    @Test
    public void testEqualityWhenDocBaseAndFilePathSUpplied() {
        FilePathData fpThis = new FilePathData("docbase", "XYZ");
        FilePathData fpThat = new FilePathData("docbase", "XYZ");

        assertEquals(fpThis, fpThat);
    }

    @Test
    public void testEqualityWhenDocBaseAndFilePathSuppliedButNotSame() {
        FilePathData fpThis = new FilePathData("docbase", "XYZ");
        FilePathData fpThat = new FilePathData("docbase", "XYZ1");

        assertNotEquals(fpThis, fpThat);
    }


    @Test
    public void expectAProperFilePathToBeConstructedWhenTheDocbaseIsNullButTheInputFilePathContainsAFullPath() {
        FilePathData fpu = new FilePathData(null, "s3://bucketname/folder/filename.dat");
        assertTrue(fpu.isS3Protocol());
        assertEquals("folder/filename.dat", fpu.getFilePathNoBucket());
        assertEquals("bucketname", fpu.getS3Bucketname());
        assertEquals("folder", fpu.getFolderPathNoBucket());
        assertEquals("s3://bucketname/folder/filename.dat", fpu.getFilePath());
    }

    @Test
    public void expectAProperFilePathToBeConstructedWhenTheDocbaseIsEmptyButTheInputFilePathContainsAFullPath() {
        FilePathData fpu = new FilePathData("", "s3://bucketname/folder/filename.dat");
        assertTrue(fpu.isS3Protocol());
        assertEquals("folder/filename.dat", fpu.getFilePathNoBucket());
        assertEquals("bucketname", fpu.getS3Bucketname());
        assertEquals("folder", fpu.getFolderPathNoBucket());
        assertEquals("s3://bucketname/folder/filename.dat", fpu.getFilePath());
    }

    @Test
    public void weCanFindBucketNameInS3Url() {
        FilePathData fpu = new FilePathData("s3://bucketname", "folder/filename.dat");
        assertTrue("Should be recognised as a S3 bucket", fpu.isS3Protocol());
        assertEquals("bucketname", fpu.getS3Bucketname());
    }

    @Test
    public void weCanFindBucketNameInS3UrlWhenJustUrlIsProvided() {
        FilePathData fpu = new FilePathData("s3://bucketname/filename.dat");
        assertTrue("Should be recognised as a S3 bucket", fpu.isS3Protocol());
        assertEquals("bucketname", fpu.getS3Bucketname());
    }

    @Test
    public void weCanFindBucketNameInBucketNameOnlyS3Url() {
        FilePathData fpu = new FilePathData("s3://", "bucketname");
        assertEquals("", fpu.getS3Bucketname());
    }

    @Test
    public void testWeCanIsolateFileNameForS3() {
        FilePathData fpu = new FilePathData("s3://bucketname/folder", "filename.dat");
        assertEquals("Should find filename.dat as filename", "filename.dat", fpu.getFilename());
    }

    @Test
    public void testWeCanIsolateFileNameForS3WhenOnlyFileName() {
        FilePathData fpu = new FilePathData("s3://filename.dat", "");
        assertEquals("Should find filename.dat as filename", "filename.dat", fpu.getFilename());
    }

    @Test
    public void testWeCanIsolateFileNameForHttps() {
        FilePathData fpu = new FilePathData("https://bucketname", "folder/filename.dat");
        assertEquals("Should find filename.dat as filename", "filename.dat", fpu.getFilename());
    }

    @Test
    public void testWeCanIsolateFileNameForHttpsWhenOnlyFilename() {
        FilePathData fpu = new FilePathData("https://filename.dat", "");
        assertEquals("Should find filename.dat as filename", "filename.dat", fpu.getFilename());
    }

    @Test
    public void testWeCanIsolateFileNameForHttp() {
        FilePathData fpu = new FilePathData("http://bucketname/folder", "filename.dat");
        assertEquals("Should find filename.dat as filename", "filename.dat", fpu.getFilename());
    }

    @Test
    public void testWeCanIsolateFileNameForHttpWhenOnlyFilename() {
        FilePathData fpu = new FilePathData("http://filename.dat", "");
        assertEquals("Should find filename.dat as filename", "filename.dat", fpu.getFilename());
    }

    @Test
    public void testWeCanIsolateFilePathForS3() {
        FilePathData fpu = new FilePathData("s3://bucketname/folder", "filename.dat");
        assertEquals("Should find filename.dat as filename", "folder/filename.dat", fpu.getFilePathNoBucket());
    }

    @Test
    public void testWeCanIsolateFilePathForHttp() {
        FilePathData fpu = new FilePathData("http://folderone/foldertwo", "filename.dat");
        assertEquals("Should find filename.dat as filename", "folderone/foldertwo/filename.dat", fpu.getFilePathNoBucket());
    }

    @Test
    public void testWeCanIsolateFilePathForHttps() {
        FilePathData fpu = new FilePathData("https://folderone/foldertwo", "filename.dat");
        assertEquals("Should find filename.dat as filename", "folderone/foldertwo/filename.dat", fpu.getFilePathNoBucket());
    }

    @Test
    public void testWeCanIsolateFolderPathForS3() {
        FilePathData fpu = new FilePathData("s3://bucketname/folder", "filename.dat");
        assertEquals("Should find filename.dat as filename", "folder", fpu.getFolderPathNoBucket());
    }

    @Test
    public void testWeCanIsolateFolderPathForHttp() {
        FilePathData fpu = new FilePathData("http://folderone/foldertwo", "filename.dat");
        assertEquals("Should find filename.dat as filename", "folderone/foldertwo", fpu.getFolderPathNoBucket());
    }

    @Test
    public void testWeCanIsolateFolderPathForHttps() {
        FilePathData fpu = new FilePathData("https://folderone/foldertwo", "filename.dat");
        assertEquals("Should find filename.dat as filename", "folderone/foldertwo", fpu.getFolderPathNoBucket());
    }
}
