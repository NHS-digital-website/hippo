package uk.nhs.digital.apispecs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hippoecm.repository.util.WorkflowUtils.Variant.DRAFT;
import static org.hippoecm.repository.util.WorkflowUtils.Variant.PUBLISHED;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static uk.nhs.digital.test.util.RandomTestUtils.randomString;

import org.junit.Before;
import org.junit.Test;
import uk.nhs.digital.apispecs.jcr.JcrDocumentLifecycleSupport;
import uk.nhs.digital.apispecs.model.ApiSpecificationDocument;

import java.time.Instant;
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
    public void id_delegatesToDocumentProxy() {

        // given
        final String expectedSpecificationId = randomString();

        given(jcrDocumentLifecycleSupport.getStringProperty("website:specification_id", DRAFT))
            .willReturn(Optional.of(expectedSpecificationId));

        // when
        final String actualSpecificationId = apiSpecificationDocument.specificationId();

        // then
        assertThat("HTML value is as returned from document proxy.",
            actualSpecificationId,
            is(expectedSpecificationId)
        );
    }

    @Test
    public void html_delegatesToDocumentProxy() {

        // given
        final String expectedHtml = randomString();

        given(jcrDocumentLifecycleSupport.getStringProperty("website:html", PUBLISHED))
            .willReturn(Optional.of(expectedHtml));

        // when
        final Optional<String> actualHtml = apiSpecificationDocument.html();

        // then
        assertThat("HTML value is as returned from document proxy.",
            actualHtml,
            is(Optional.of(expectedHtml))
        );
    }

    @Test
    public void setHtml_delegatesToDocumentProxy() {

        // given
        final String updatedHtmlContent = randomString();

        // when
        apiSpecificationDocument.setHtmlForPublishing(updatedHtmlContent);

        // then
        then(jcrDocumentLifecycleSupport).should().setStringPropertyWithCheckout("website:html", updatedHtmlContent);
    }

    @Test
    public void json_delegatesToDocumentProxy() {

        // given
        final String expectedJson = randomString();

        given(jcrDocumentLifecycleSupport.getStringProperty("website:json", PUBLISHED))
            .willReturn(Optional.of(expectedJson));

        // when
        final Optional<String> actualHtml = apiSpecificationDocument.json();

        // then
        assertThat("JSON value is as returned from document proxy.",
            actualHtml,
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

    @Test
    public void lastPublicationInstant_delegatesToDocumentProxy() {

        // given
        final Instant expectedInstant = Instant.now();

        given(jcrDocumentLifecycleSupport.getLastPublicationInstant())
            .willReturn(Optional.of(expectedInstant));

        // when
        final Optional<Instant> actualInstant = apiSpecificationDocument.lastPublicationInstant();

        // then
        assertThat("Last publication time value is as returned from document proxy.",
            actualInstant,
            is(Optional.of(expectedInstant))
        );
    }
}