package uk.nhs.digital.intranet.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.intranet.model.AccessToken;

import java.io.*;
import java.util.Base64;

public class AccessTokenEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenEncoder.class);

    public static String encode(final AccessToken accessToken) {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(accessToken);
            objectOutputStream.close();
            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        } catch (final IOException e) {
            LOGGER.error("Could not encode access token.", e);
            return null;
        }
    }

    public static AccessToken decode(final String encodedString) {
        try {
            final byte[] bytes = Base64.getDecoder().decode(encodedString);
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            final AccessToken accessToken = (AccessToken) objectInputStream.readObject();
            objectInputStream.close();
            return accessToken;
        } catch (final Exception e) {
            LOGGER.error("Could not decode access token.", e);
            return null;
        }
    }
}
