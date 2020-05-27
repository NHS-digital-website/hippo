package uk.nhs.digital.apispecs.model;

import static org.hippoecm.repository.util.WorkflowUtils.Variant.DRAFT;
import static uk.nhs.digital.apispecs.model.ApiSpecificationDocument.ApiSpecPropertyNames.HTML;
import static uk.nhs.digital.apispecs.model.ApiSpecificationDocument.ApiSpecPropertyNames.SPECIFICATION_ID;

import uk.nhs.digital.apispecs.jcr.JcrDocumentLifecycleSupport;

import java.time.Instant;
import java.util.Optional;

public class ApiSpecificationDocument {

    private JcrDocumentLifecycleSupport jcrDocumentLifecycleSupport;

    private ApiSpecificationDocument(final JcrDocumentLifecycleSupport jcrDocumentLifecycleSupport) {
        this.jcrDocumentLifecycleSupport = jcrDocumentLifecycleSupport;
    }

    public static ApiSpecificationDocument from(final JcrDocumentLifecycleSupport jcrDocumentLifecycleSupport) {
        return new ApiSpecificationDocument(jcrDocumentLifecycleSupport);
    }

    public String getId() {
        return jcrDocument().getStringProperty(SPECIFICATION_ID.value(), DRAFT)
            .orElseThrow(() -> new RuntimeException("Specification id not available"))
            ;
    }

    public void setHtml(final String html) {
        jcrDocument().setProperty(HTML.value(), html);
    }

    public void saveAndPublish() {
        jcrDocument().saveAndPublish();
    }

    @Override public String toString() {
        return "ApiSpecification{documentLifecycleSupport=" + jcrDocumentLifecycleSupport + '}';
    }

    public Optional<Instant> getLastPublicationInstant() {
        return jcrDocument().getLastPublicationInstant();
    }

    private JcrDocumentLifecycleSupport jcrDocument() {
        return jcrDocumentLifecycleSupport;
    }

    enum ApiSpecPropertyNames {
        HTML("website:html"),
        SPECIFICATION_ID("website:specification_id");

        private final String value;

        ApiSpecPropertyNames(final String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }
}
