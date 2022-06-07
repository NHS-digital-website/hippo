package uk.nhs.digital.apispecs.services;


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


public abstract class RemoteSpecService implements OpenApiSpecificationRepository {

    private static final Logger log = LoggerFactory.getLogger(RemoteSpecService.class);

    private final String serviceName = getClass().getSimpleName();

    private final ResourceServiceBroker resourceServiceBroker;
    private final String resourceNamespace;
    private final String allSpecUrl;
    private final String singleSpecUrl;

    public RemoteSpecService(final ResourceServiceBroker resourceServiceBroker,
                             final String resourceNamespace,
                             final String allSpecUrl,
                             final String singleSpecUrl
    ) {
        this.resourceServiceBroker = resourceServiceBroker;
        this.resourceNamespace = resourceNamespace;
        this.allSpecUrl = allSpecUrl;
        this.singleSpecUrl = singleSpecUrl;
    }

    @Override
    public List<OpenApiSpecification> apiSpecificationStatuses() throws OpenApiSpecificationRepositoryException {

        log.debug("Retrieving list of available specifications from {}.", serviceName);

        return throwServiceExceptionOnFailure(() -> {

                final Resource resource = resourceAt(allSpecUrl);

                return apiSpecificationsStatusesFrom(resource);
            },
            "Failed to retrieve list of available specifications from {0}.", serviceName
        );
    }

    @Override
    public String apiSpecificationJsonForSpecId(final String specificationId) throws OpenApiSpecificationRepositoryException {

        log.debug("Retrieving specification from {} with id {}.", serviceName, specificationId);

        return throwServiceExceptionOnFailure(() -> {

            final String singleSpecUrl = urlForSingleSpecification(specificationId);

            final Resource resource = resourceAt(singleSpecUrl);

            return apiSpecificationJsonFrom(resource);

        }, "Failed to retrieve specification from {0} with id {1}.", serviceName, specificationId);
    }

    private String urlForSingleSpecification(final String specificationId) {
        return UriComponentsBuilder.fromHttpUrl(singleSpecUrl).build(specificationId).toString();
    }

    private Resource resourceAt(final String url) {
        return resourceServiceBroker.resolve(resourceNamespace, url);
    }

    abstract List<OpenApiSpecification> apiSpecificationsStatusesFrom(final Resource resource);

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
