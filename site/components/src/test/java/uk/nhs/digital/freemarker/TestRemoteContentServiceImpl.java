package uk.nhs.digital.freemarker;

import java.net.URL;

public class TestRemoteContentServiceImpl extends RemoteContentService {

    public static final String fallbackValue = "Fallback Value";

    @Override
    public Object getReliableFallBackObject(URL url, String resourceResolver, Class type, Throwable e) {
        TestObject testObject = new TestObject();
        testObject.setTestValue(fallbackValue);
        return testObject;
    }

}
