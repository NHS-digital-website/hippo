package uk.nhs.digital.externalstorage.s3;

import static org.junit.Assert.*;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class S3ObjectKeyGeneratorTest {

    @Test
    public void generatesS3ObjectFolderStructure() throws Exception {

        // given
        final String fileName = newRandomString();

        final S3ObjectKeyGenerator s3ObjectKeyGenerator = new S3ObjectKeyGenerator(this::newRandomString);

        // when
        final String actualObjectKey = s3ObjectKeyGenerator.generateObjectKey(fileName);

        // then
        assertTrue("Generates S3 object key folder structure.", actualObjectKey.matches("^[0-9A-F]{2}/[0-9A-F]{6}/.*"));

    }

    @Test
    public void shouldContainFileName() throws Exception {

        // given
        final String fileName = newRandomString();

        final S3ObjectKeyGenerator s3ObjectKeyGenerator = new S3ObjectKeyGenerator(this::newRandomString);

        // when
        final String actualObjectKey = s3ObjectKeyGenerator.generateObjectKey(fileName);

        // then
        Path path = Paths.get(actualObjectKey);
        assertEquals("Path contains filename at the end.", path.getName(2).toString(), fileName);

    }

    @Test
    public void generatedS3ObjectFolderStructuresAreIdentical_forTheFileNameAndSeed() throws Exception {

        // given
        final String fileName = newRandomString();
        final String randomSeed = newRandomString();

        final S3ObjectKeyGenerator s3ObjectKeyGenerator = new S3ObjectKeyGenerator(() -> randomSeed);

        // when
        final String actualObjectKeyA = s3ObjectKeyGenerator.generateObjectKey(fileName);
        final String actualObjectKeyB = s3ObjectKeyGenerator.generateObjectKey(fileName);

        // then
        assertEquals("Generated S3 object keys are identical.", actualObjectKeyA, actualObjectKeyB);
    }

    @Test
    public void generatedS3ObjectFolderStructuresAreDifferentFor_forTheFileNameAndDifferentSeeds() throws Exception {

        // given
        final String fileName = newRandomString();

        final S3ObjectKeyGenerator s3ObjectKeyGeneratorA = new S3ObjectKeyGenerator(this::newRandomString);
        final S3ObjectKeyGenerator s3ObjectKeyGeneratorB = new S3ObjectKeyGenerator(this::newRandomString);

        // when
        final String actualObjectKeyA = s3ObjectKeyGeneratorA.generateObjectKey(fileName);
        final String actualObjectKeyB = s3ObjectKeyGeneratorB.generateObjectKey(fileName);

        // then
        assertNotEquals("Generated S3 object keys are different.", actualObjectKeyA, actualObjectKeyB);
    }

    private String newRandomString() {
        return UUID.randomUUID().toString();
    }
}
