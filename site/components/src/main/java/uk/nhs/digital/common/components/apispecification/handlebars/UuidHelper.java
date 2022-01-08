package uk.nhs.digital.common.components.apispecification.handlebars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.util.UUID;

public class UuidHelper implements Helper<Object> {
    public static final String NAME = "uuid";

    public static final UuidHelper INSTANCE = new UuidHelper();

    private UuidHelper() {
        // private to encourage use of INSTANCE
    }

    @Override
    public CharSequence apply(Object model, Options options) {
        return UUID.randomUUID().toString();
    }
}
