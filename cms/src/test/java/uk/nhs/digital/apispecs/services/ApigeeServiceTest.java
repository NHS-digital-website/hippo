package uk.nhs.digital.apispecs.services;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.api.resource.ResourceBeanMapper;
import uk.nhs.digital.apispecs.OpenApiSpecificationRepositoryException;
import uk.nhs.digital.apispecs.model.OpenApiSpecification;

import java.util.List;

public class ApigeeServiceTest {

    private static final String RESOURCE_NAMESPACE_APIGEE_MANAGEMENT_API = "apigeeManagementApi";
    private static final String URL_ALL_SPECS = "http://openapispecifications.service/specifications";
    private static final String URL_SINGLE_SPEC_TEMPLATE = "http://openapispecifications.service/specifications/{specificationId}";

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Mock private ResourceServiceBroker broker;
    @Mock private Resource resource;
    @Mock private ResourceBeanMapper resourceBeanMapper;
    @Mock private RuntimeException collaboratorException;

    private ApigeeService apigeeService;

    @Before
    public void setUp() {
        openMocks(this);

        apigeeService = new ApigeeService(
            broker,
            URL_ALL_SPECS,
            URL_SINGLE_SPEC_TEMPLATE
        );
    }

    @Test
    public void apiSpecificationStatuses_retrievesSpecificationsStatusesFromResourceServiceBroker() {

        // given
        final OpenApiSpecification specA = new OpenApiSpecification("specA", "2020-06-10T11:17:00.017Z");
        final OpenApiSpecification specB = new OpenApiSpecification("specB", "2020-06-10T11:23:00.017Z");
        final OpenApiSpecifications expectedStatuses = new OpenApiSpecifications(asList(specA, specB));

        given(broker.resolve(any(), any())).willReturn(resource);
        given(broker.getResourceBeanMapper(any())).willReturn(resourceBeanMapper);
        given(resourceBeanMapper.map(any(), any())).willReturn(expectedStatuses);

        // when
        final List<OpenApiSpecification> actualStatuses = apigeeService.apiSpecificationStatuses();

        // then
        then(broker).should().resolve(RESOURCE_NAMESPACE_APIGEE_MANAGEMENT_API, URL_ALL_SPECS);
        assertThat(
            "Returns statuses as produced by CRISP API.",
            actualStatuses,
            containsInAnyOrder(
                sameInstance(specA),
                sameInstance(specB)
            )
        );
    }

    @Test
    public void apiSpecificationStatuses_throwsException_onFailure() {

        // given
        given(broker.resolve(any(), any())).willThrow(collaboratorException);

        expectedException.expect(OpenApiSpecificationRepositoryException.class);
        expectedException.expectMessage("Failed to retrieve list of available specifications from ApigeeService.");
        expectedException.expectCause(sameInstance(collaboratorException));

        // when
        apigeeService.apiSpecificationStatuses();

        // then
        // expectations set in 'given' are satisfied
    }


    @Test
    public void apiSpecificationJsonForSpecId_retrievesSpecificationsStatusesFromResourceServiceBroker() {

        // given
        final String anySpecificationId = "123456";
        final String singleSpecUrlWithGivenId = URL_SINGLE_SPEC_TEMPLATE.replace("{specificationId}", anySpecificationId);
        final String expectedSpecificationJson = "{\"expected\":{\"specification\":\"json\"}}";

        given(broker.resolve(any(), any())).willReturn(resource);
        given(resource.getNodeData()).willReturn(expectedSpecificationJson);

        // when
        final String actualSpecificationJson = apigeeService.apiSpecificationJsonForSpecId(anySpecificationId);

        // then
        then(broker).should().resolve(RESOURCE_NAMESPACE_APIGEE_MANAGEMENT_API, singleSpecUrlWithGivenId);
        assertThat(
            "Returns specification JSON as produced by CRISP API.",
            actualSpecificationJson,
            is(expectedSpecificationJson)
        );
    }

    @Test
    public void apiSpecificationJsonForSpecId_throwsException_onFailure() {

        // given
        given(broker.resolve(any(), any())).willThrow(collaboratorException);

        final String anySpecificationId = "123456";

        expectedException.expect(OpenApiSpecificationRepositoryException.class);
        expectedException.expectMessage("Failed to retrieve specification from ApigeeService with id " + anySpecificationId + ".");
        expectedException.expectCause(sameInstance(collaboratorException));

        // when
        apigeeService.apiSpecificationJsonForSpecId(anySpecificationId);

        // then
        // expectations set in 'given' are satisfied
    }
}