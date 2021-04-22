package uk.nhs.digital.apispecs.handlebars;

import java.util.UUID;

public class UuidHelper {

    public static final UuidHelper INSTANCE = new UuidHelper();

    private UuidHelper() {
        // private to encourage use of INSTANCE
    }

    public CharSequence uuid() {
        return UUID.randomUUID().toString();
    }
}
