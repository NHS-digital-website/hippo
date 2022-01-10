package uk.nhs.digital.cache.listener;

import static java.util.Objects.nonNull;

import org.onehippo.repository.events.PersistedHippoEventListener;
import org.onehippo.repository.events.PersistedHippoEventListenerRegistry;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class PersistedHippoEventListenerRegisteringComponent implements InitializingBean, DisposableBean {

    private final PersistedHippoEventListener listener;

    public PersistedHippoEventListenerRegisteringComponent(
        final PersistedHippoEventListener listener
    ) {
        this.listener = listener;
    }

    @Override public void afterPropertiesSet() throws Exception {
        PersistedHippoEventListenerRegistry.get().register(listener);
    }

    @Override public void destroy() throws Exception {
        if (nonNull(listener)) {
            PersistedHippoEventListenerRegistry.get().unregister(listener);
        }
    }
}
