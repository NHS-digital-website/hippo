package uk.nhs.digital.apispecs.services;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponentsBuilder;
import uk.nhs.digital.apispecs.OpenApiSpecificationRepository;
import uk.nhs.digital.apispecs.OpenApiSpecificationRepositoryException;
import uk.nhs.digital.apispecs.model.OpenApiSpecification;

import java.text.MessageFormat;
import java.util.List;
import java.util.function.Supplier;


public class ProxygenService implements OpenApiSpecificationRepository {

    private static final Logger log = LoggerFactory.getLogger(ProxygenService.class);

    private static final String RESOURCE_NAMESPACE_PROXYGEN_MANAGEMENT_API = "proxygenManagementApi";

    private final ResourceServiceBroker resourceServiceBroker;
    private final String allSpecUrl;
    private final String singleSpecUrl;

    public ProxygenService(final ResourceServiceBroker resourceServiceBroker,
                           final String allSpecUrl,
                           final String singleSpecUrl
    ) {
        this.resourceServiceBroker = resourceServiceBroker;
        this.allSpecUrl = allSpecUrl;
        this.singleSpecUrl = singleSpecUrl;
    }

    @Override
    public List<OpenApiSpecification> apiSpecificationStatuses() throws OpenApiSpecificationRepositoryException {

        // TODO duplicated code from the ApigeeService, clean up after basic functionality working
        log.debug("Retrieving list of available specifications.");

        return throwServiceExceptionOnFailure(() -> {

                final Resource resource = resourceAt(allSpecUrl);

                return apiSpecificationsStatusesFrom(resource);
            },
            "Failed to retrieve list of available specifications."
        );
    }

    @Override
    public String apiSpecificationJsonForSpecId(final String specificationId) throws OpenApiSpecificationRepositoryException {

        // rename to spec name rather than ID?
        log.debug("Retrieving specification with id {}.", specificationId);

        return throwServiceExceptionOnFailure(() -> {

                final String singleSpecUrl = urlForSingleSpecification(specificationId);

                final Resource resource = resourceAt(singleSpecUrl);

                return apiSpecificationJsonFrom(resource);

            },
            "Failed to retrieve specification with id {0}.", specificationId
        );
    }

    private String urlForSingleSpecification(final String specificationId) {
        return UriComponentsBuilder.fromHttpUrl(singleSpecUrl).build(specificationId).toString();
    }

    private Resource resourceAt(final String url) {
        return resourceServiceBroker.resolve(RESOURCE_NAMESPACE_PROXYGEN_MANAGEMENT_API, url);
    }

    private List<OpenApiSpecification> apiSpecificationsStatusesFrom(final Resource resource) {
        // TODO mapping may be different (proxygen = spec_id, last_modified)
        final List<OpenApiSpecification> remoteApiSpecifications = unmodifiableList(
            resourceServiceBroker
                .getResourceBeanMapper(RESOURCE_NAMESPACE_PROXYGEN_MANAGEMENT_API)
                .map(resource, OpenApiSpecifications.class)
                .getContents()
                .stream()
                .peek(openApiSpecification -> openApiSpecification.setService(this))
                .collect(toList())
        );

        log.debug("Found {} specifications.", remoteApiSpecifications.size());

        return remoteApiSpecifications;
    }

    private String apiSpecificationJsonFrom(final Resource resource) {
        return resource.getNodeData().toString();
    }

    private <T> T throwServiceExceptionOnFailure(
        final Supplier<T> supplier,
        final String errorMessage,
        final Object... errorMessageArgs
    ) {
        try {
            return supplier.get();
        } catch (final Exception cause) {

            final String formattedErrorMessage = MessageFormat.format(
                errorMessage,
                errorMessageArgs
            );

            throw new OpenApiSpecificationRepositoryException(formattedErrorMessage, cause);
        }
    }
}
