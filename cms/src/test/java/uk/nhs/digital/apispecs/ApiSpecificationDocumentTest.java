package uk.nhs.digital.apispecs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hippoecm.repository.util.WorkflowUtils.Variant.PUBLISHED;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static uk.nhs.digital.test.util.RandomTestUtils.randomString;

import org.junit.Before;
import org.junit.Test;
import uk.nhs.digital.apispecs.jcr.JcrDocumentLifecycleSupport;
import uk.nhs.digital.apispecs.model.ApiSpecificationDocument;

import java.util.Optional;

public class ApiSpecificationDocumentTest {

    private JcrDocumentLifecycleSupport jcrDocumentLifecycleSupport;

    private ApiSpecificationDocument apiSpecificationDocument;

    @Before
    public void setUp() {
        jcrDocumentLifecycleSupport = mock(JcrDocumentLifecycleSupport.class);

        apiSpecificationDocument = ApiSpecificationDocument.from(jcrDocumentLifecycleSupport);
    }

    @Test
    public void json_delegatesToDocumentProxy() {

        // given
        final String expectedJson = randomString();

        given(jcrDocumentLifecycleSupport.getStringProperty("website:json", PUBLISHED))
            .willReturn(Optional.of(expectedJson));

        // when
        final Optional<String> actualJson = apiSpecificationDocument.json();

        // then
        assertThat("JSON value is as returned from document proxy.",
            actualJson,
            is(Optional.of(expectedJson))
        );
    }

    @Test
    public void setJson_delegatesToDocumentProxy() {

        // given
        final String updatedSpecJsonContent = randomString();

        // when
        apiSpecificationDocument.setJsonForPublishing(updatedSpecJsonContent);

        // then
        then(jcrDocumentLifecycleSupport).should().setStringPropertyWithCheckout("website:json", updatedSpecJsonContent);
    }

    @Test
    public void publish_delegatesToDocumentProxy() {

        // when
        apiSpecificationDocument.saveAndPublish();

        // then
        then(jcrDocumentLifecycleSupport).should().saveAndPublish();
    }
}