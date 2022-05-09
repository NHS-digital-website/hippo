package uk.nhs.digital.freemarker.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class StringToBase64Test {

    private StringToBase64 stringToBase64 = new StringToBase64();

    @Test
    public void encodedCorrectly() {
        assertThat(stringToBase64.encoded("Test Value"), is("VGVzdCBWYWx1ZQ=="));
    }

}