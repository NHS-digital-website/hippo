package uk.nhs.digital.freemarker.tableau;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

public class TableauRemoteContentServiceTest {

    @Test
    public void tableauFallBackIsSet() throws MalformedURLException {
        TableauRemoteContentService tableauRemoteContentService = new TableauRemoteContentService();
        ThrottleOptions throttleOptions = (ThrottleOptions) tableauRemoteContentService.getReliableFallBackObject(new URL("http://no-null-pointer.io"),null, null, new Throwable("Just a test, nothing to see here"));
        assertThat(throttleOptions.getSize(), is(TableauRemoteContentService.defaultThrottleOptionValue));
    }

}
