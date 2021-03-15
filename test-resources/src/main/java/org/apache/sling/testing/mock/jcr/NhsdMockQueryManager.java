package org.apache.sling.testing.mock.jcr;

/**
 * {@linkplain MockQueryManager} is package-private and so the current class is needed
 * to enable creating its instances and registering of custom result query handlers
 * for the purpose of mocking JCR repository.
 */
public class NhsdMockQueryManager extends MockQueryManager {

    public void registerResultHandler(final MockQueryResultHandler mockQueryResultHandler) {
        super.addResultHandler(mockQueryResultHandler);
    }
}
