package uk.nhs.digital.apispecs.apigee.auth;

import org.apache.commons.codec.binary.Hex;

import java.time.Clock;
import java.util.concurrent.TimeUnit;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class GoogleAuthOtpGenerator {

    public GoogleAuthOtpGenerator() {}

    public String googleAuthenticatorCode(String secret, Clock clock) throws Exception {
        try {
            if (secret == null || secret == "") {
                throw new Exception("Secret key does not exist.");
            }
            long value = clock.millis() / TimeUnit.SECONDS.toMillis(30);

            Base32 base = new Base32(Base32.Alphabet.BASE32, false, true);
            byte[] key = base.fromString(secret);

            byte[] data = new byte[8];
            for (int i = 8; i-- > 0; value >>>= 8) {
                data[i] = (byte) value;
            }

            byte[] hash = hmacSha1(data, key);

            return truncateHash(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate one time password.", e);
        }
    }

    private String truncateHash(byte[] hash) {
        String hashString = new String(hash);
        int offset = Integer.parseInt(hashString.substring(hashString.length() - 1, hashString.length()), 16);

        String truncatedHash = hashString.substring(offset * 2, offset * 2 + 8);

        int val = Integer.parseUnsignedInt(truncatedHash, 16) & 0x7FFFFFFF;

        String finalHash = String.valueOf(val);
        finalHash = finalHash.substring(finalHash.length() - 6, finalHash.length());

        return finalHash;
    }

    private byte[] hmacSha1(byte[] value, byte[] keyBytes) {
        SecretKeySpec signKey = new SecretKeySpec(keyBytes, "HmacSHA1");
        try {
            Mac mac = Mac.getInstance("HmacSHA1");

            mac.init(signKey);

            byte[] rawHmac = mac.doFinal(value);

            return new Hex().encode(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
