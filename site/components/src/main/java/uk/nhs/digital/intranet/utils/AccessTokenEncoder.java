package uk.nhs.digital.intranet.utils;

import uk.nhs.digital.intranet.model.AccessToken;

import java.io.*;
import java.util.Base64;

public class AccessTokenEncoder {

    public static String encode(final AccessToken accessToken) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(accessToken);
        objectOutputStream.close();
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    public static AccessToken decode(final String encodedString) throws IOException, ClassNotFoundException {
        final byte[] bytes = Base64.getDecoder().decode(encodedString);
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        final AccessToken accessToken = (AccessToken) objectInputStream.readObject();
        objectInputStream.close();
        return accessToken;
    }
}
