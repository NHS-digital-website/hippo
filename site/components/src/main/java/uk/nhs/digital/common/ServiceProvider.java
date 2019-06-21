package uk.nhs.digital.common;

import org.onehippo.cms7.services.HippoServiceRegistry;

/**
 * Delegates calls to {@linkplain HippoServiceRegistry} making it easier to
 * test classes that interact with the static methods of the registry class.
 */
public class ServiceProvider {

    public  <T> T getService(final Class<T> serviceClass) {
        return HippoServiceRegistry.getService(serviceClass);
    }
}
