package uk.nhs.digital.common.util.json;

/**
 * <p>
 * General purpose JSON serialiser interface which can be supported by various implementations.
 * </p><p>
 * Being defined as an external dependency, helps with testability of classes that require it.
 * </p><p>
 * Note that it's deliberately not been marked with {@linkplain org.onehippo.cms7.services.SingletonService}
 * annotation as it's likely that the clients will need the serialiser configured in their own,
 * specific way. Therefore, when registering new instances in {@linkplain
 * org.onehippo.cms7.services.HippoServiceRegistry} an id/name needs to be provided to differentiate
 * specifically configured instances.
 * </p>
 */
public interface JsonSerialiser {

    String toJson(Object pojoToSerialise);
}
