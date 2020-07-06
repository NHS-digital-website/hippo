package uk.nhs.digital.freemarker.tableau;

import org.slf4j.LoggerFactory;
import uk.nhs.digital.freemarker.RemoteContentService;

import java.net.URL;

public class TableauRemoteContentService extends RemoteContentService {

    public static final float defaultThrottleOptionValue = 0.75f;

    @Override
    public Object getReliableFallBackObject(URL url, String resourceResolver, Class type, Throwable e) {
        LoggerFactory.getLogger(TableauRemoteContentService.class).warn(String.format("Issue with URL: %s", url.toString()), e);
        return new ThrottleOptions(defaultThrottleOptionValue);
    }

}
