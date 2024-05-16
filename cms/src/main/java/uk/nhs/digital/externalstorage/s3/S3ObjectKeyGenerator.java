package uk.nhs.digital.externalstorage.s3;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Supplier;
import javax.xml.bind.DatatypeConverter;

public class S3ObjectKeyGenerator {

    private final MessageDigest messageDigest;

    private final Supplier<String> randomSeedProvider;

    public S3ObjectKeyGenerator(final Supplier<String> randomSeedProvider) {
        this.randomSeedProvider = randomSeedProvider;
        messageDigest = getSha256MessageDigest();
    }


    public String generateObjectKey(String fileName) {

        final String fileNameSalt = randomSeedProvider.get();

        messageDigest.update((fileName + fileNameSalt).getBytes());

        final byte[] digest = messageDigest.digest();

        final String hash = DatatypeConverter.printHexBinary(digest).toUpperCase();

        return hash.substring(0,2) + "/" + hash.substring(2,8) + "/" + fileName;
    }

    private MessageDigest getSha256MessageDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Error initialising SHA-256 MessageDigest in " + S3SdkConnector.class.getName(), ex);
        }
    }
}
