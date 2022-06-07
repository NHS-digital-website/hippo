package uk.nhs.digital.apispecs.services;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.apispecs.model.OpenApiSpecification;

import java.util.List;

public class ApigeeService extends RemoteSpecService {

    private static final Logger log = LoggerFactory.getLogger(ApigeeService.class);

    private static final String RESOURCE_NAMESPACE_APIGEE_MANAGEMENT_API = "apigeeManagementApi";

    private final ResourceServiceBroker resourceServiceBroker;

    public ApigeeService(final ResourceServiceBroker resourceServiceBroker,
                         final String allSpecUrl,
                         final String singleSpecUrl
    ) {
        super(resourceServiceBroker,
            RESOURCE_NAMESPACE_APIGEE_MANAGEMENT_API,
            allSpecUrl,
            singleSpecUrl);

        this.resourceServiceBroker = resourceServiceBroker;
    }

    protected List<OpenApiSpecification> apiSpecificationsStatusesFrom(final Resource resource) {
        final List<OpenApiSpecification> remoteApiSpecifications = unmodifiableList(
            resourceServiceBroker
                .getResourceBeanMapper(RESOURCE_NAMESPACE_APIGEE_MANAGEMENT_API)
                .map(resource, OpenApiSpecifications.class)
                .getContents()
                .stream()
                .peek(openApiSpecification -> openApiSpecification.setService(this))
                .collect(toList())
        );

        log.debug("Found {} specifications.", remoteApiSpecifications.size());

        return remoteApiSpecifications;
    }
}
