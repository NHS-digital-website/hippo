package uk.nhs.digital.ps.test.acceptance.models;

import static uk.nhs.digital.ps.test.acceptance.util.RandomHelper.newRandomByteArray;
import static uk.nhs.digital.ps.test.acceptance.util.RandomHelper.newRandomString;

public class Attachment {

    private String name;
    private byte[] content;

    private Attachment(final String name, final byte[] content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public byte[] getContent() {
        return content;
    }

    /**
     * @return New instance, fully populated with random values.
     */
    static Attachment createNew() {

        return new Attachment(
            newRandomString() + ".txt",
            newRandomByteArray()
        );
    }
}
