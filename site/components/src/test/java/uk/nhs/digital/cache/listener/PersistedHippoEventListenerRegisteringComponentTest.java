package uk.nhs.digital.cache.listener;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.BDDMockito.then;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.onehippo.cms7.services.ServiceHolder;
import org.onehippo.repository.events.PersistedHippoEventListener;
import org.onehippo.repository.events.PersistedHippoEventListenerRegistry;
import uk.nhs.digital.test.mockito.MockitoSessionTestBase;

import java.util.stream.Stream;

public class PersistedHippoEventListenerRegisteringComponentTest extends MockitoSessionTestBase {

    @Mock private PersistedHippoEventListener listener;

    private PersistedHippoEventListenerRegisteringComponent component;

    @Before
    public void setUp() throws Exception {
        component = new PersistedHippoEventListenerRegisteringComponent(listener);
    }

    @After
    public void tearDown() {
        registeredListenersStream()
            .forEach(persistedHippoEventListener -> PersistedHippoEventListenerRegistry.get().unregister(persistedHippoEventListener));
    }

    @Test
    public void afterPropertiesSet_registersNewListener() throws Exception {
        // given
        // setUp()

        // when
        component.afterPropertiesSet();

        // then

        assertThat(
            "There is exactly one registered listener.",
            registeredListenersStream().count(),
            is(1L)
        );

        //noinspection OptionalGetWithoutIsPresent
        final Object registeredListener = registeredListenersStream().findAny().get();

        assertThat(
            "Registered listener is the provided one.",
            registeredListener,
            sameInstance(listener)
        );

        then(listener).shouldHaveNoInteractions();
    }

    @Test
    public void destroy_unregistersNewListener() throws Exception {

        // given
        component.afterPropertiesSet();

        // when
        component.destroy();

        // then
        assertThat(
            "Listener has been unregistered.",
            registeredListenersStream().count(),
            is(0L)
        );

    }

    private Stream<PersistedHippoEventListener> registeredListenersStream() {
        return PersistedHippoEventListenerRegistry.get().getEntries()
            .map(ServiceHolder::getServiceObject);
    }
}