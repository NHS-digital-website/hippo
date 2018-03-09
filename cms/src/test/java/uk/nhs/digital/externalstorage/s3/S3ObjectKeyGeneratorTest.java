package uk.nhs.digital.externalstorage.s3;

import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class S3ObjectKeyGeneratorTest {

    private String randomObjectKeySeed;

    @Before
    public void setUp() throws Exception {
        randomObjectKeySeed = newRandomString();
    }

    @Test
    public void generatesS3ObjectReference() throws Exception {

        // given
        final String fileName = newRandomString();
        final String expectedObjectKey = generateObjectKey(fileName);

        final S3ObjectKeyGenerator s3ObjectKeyGenerator = new S3ObjectKeyGenerator(() -> randomObjectKeySeed);

        // when
        final String actualObjectKey = s3ObjectKeyGenerator.generateObjectKey(fileName);

        // then
        assertThat("Generates S3 object key.", actualObjectKey, is(expectedObjectKey));

    }

    private String newRandomString() {
        return UUID.randomUUID().toString();
    }

    private String generateObjectKey(String fileName) throws NoSuchAlgorithmException {

        final MessageDigest md = MessageDigest.getInstance("MD5");

        md.update((fileName + randomObjectKeySeed).getBytes());

        String hash = DatatypeConverter.printHexBinary(md.digest()).toUpperCase();

        return "/" + hash.substring(0,2) + "/" + hash.substring(2,8) + "/" + fileName;
    }
}
