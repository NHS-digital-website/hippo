package uk.nhs.digital.freemarker;

import org.slf4j.LoggerFactory;

import java.net.URL;

public class BasicRemoteContentService extends RemoteContentService {

    @Override
    public Object getReliableFallBackObject(URL url, String resourceResolver, Class type, Throwable e) {
        LoggerFactory.getLogger(BasicRemoteContentService.class).warn(String.format("Issue with URL: %s", url.toString()), e);
        return null;
    }

}
