package uk.nhs.digital.test.mockito;

import org.junit.After;
import org.junit.Before;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;

/**
 * Makes it easier to use {@linkplain MockitoSession} (recommended by Mockito team)
 * in favour of, say, recently deprecated {@linkplain org.mockito.MockitoAnnotations#initMocks}.
 */
public class MockitoSessionTestBase {

    private MockitoSession mockitoSession;

    @Before
    public void initMocks() {
        mockitoSession = Mockito.mockitoSession()
            .initMocks(this)
            .strictness(Strictness.WARN)
            .startMocking();
    }

    @After
    public void releaseMocks() throws Exception {
        mockitoSession.finishMocking();
    }
}
