package uk.nhs.digital.apispecs.services;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import org.apache.commons.io.IOUtils;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.resource.Binary;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.apispecs.OpenApiSpecificationRepositoryException;
import uk.nhs.digital.apispecs.model.OpenApiSpecification;

import java.nio.charset.StandardCharsets;
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

    @Override
    public String apiSpecificationJsonForSpecId(final String specificationId) throws OpenApiSpecificationRepositoryException {

        log.debug("Retrieving specification from ApigeeService with id {}.", specificationId);

        return throwServiceExceptionOnFailure(() -> {

            Binary binary = null;

            try {
                final String singleSpecUrl = urlForSingleSpecification(specificationId);

                binary = resourceServiceBroker().resolveBinary(resourceNamespace(), singleSpecUrl);

                return IOUtils.toString(binary.getInputStream(), StandardCharsets.UTF_8);
            } finally {
                if (binary != null) {
                    binary.dispose();
                }
            }

        }, "Failed to retrieve specification from ApigeeService with id {0}.", specificationId);
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
