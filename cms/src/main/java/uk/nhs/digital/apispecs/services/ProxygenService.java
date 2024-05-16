package uk.nhs.digital.apispecs.services;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.apispecs.model.OpenApiSpecification;

import java.util.List;


public class ProxygenService extends RemoteSpecService {

    private static final Logger log = LoggerFactory.getLogger(ProxygenService.class);

    private static final String RESOURCE_NAMESPACE_PROXYGEN_API = "proxygenApi";
    private final ResourceServiceBroker resourceServiceBroker;

    public ProxygenService(final ResourceServiceBroker resourceServiceBroker,
                           final String allSpecUrl,
                           final String singleSpecUrl
    ) {
        super(resourceServiceBroker,
            RESOURCE_NAMESPACE_PROXYGEN_API,
            allSpecUrl,
            singleSpecUrl);

        this.resourceServiceBroker = resourceServiceBroker;
    }

    protected List<OpenApiSpecification> apiSpecificationsStatusesFrom(final Resource resource) {
        final List<OpenApiSpecification> remoteApiSpecifications = unmodifiableList(
            resourceServiceBroker
                .getResourceBeanMapper(RESOURCE_NAMESPACE_PROXYGEN_API)
                .map(resource, ProxygenOpenApiSpecifications.class)
                .getContents()
                .stream()
                .map(proxygenOpenApiSpecification -> new OpenApiSpecification(proxygenOpenApiSpecification.getSpecId(), proxygenOpenApiSpecification.getLastModified()))
                .peek(openApiSpecification -> openApiSpecification.setService(this))
                .collect(toList())
        );

        log.debug("Found {} specifications.", remoteApiSpecifications.size());

        return remoteApiSpecifications;
    }
}
