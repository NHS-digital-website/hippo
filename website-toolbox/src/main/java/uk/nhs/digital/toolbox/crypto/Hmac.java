package uk.nhs.digital.toolbox.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Hmac {
    private static String algorithm = "HmacSHA256";

    public static byte[] hash(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKeySpec);

        return mac.doFinal(data.getBytes());
    }

    public static String base64Hash(String data, String key) throws InvalidKeyException, NoSuchAlgorithmException {
        byte[] hash = Hmac.hash(data, key);
        return Base64.getEncoder().encodeToString(hash);
    }
}
