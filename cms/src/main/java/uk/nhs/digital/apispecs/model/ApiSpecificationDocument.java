package uk.nhs.digital.apispecs.model;

import static org.hippoecm.repository.util.WorkflowUtils.Variant.DRAFT;
import static org.hippoecm.repository.util.WorkflowUtils.Variant.PUBLISHED;
import static uk.nhs.digital.apispecs.model.ApiSpecificationDocument.Properties.*;

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

    public String path() {
        return jcrDocument().path();
    }

    public String getId() {
        return jcrDocument().getStringProperty(SPECIFICATION_ID.jcrName(), DRAFT)
            .orElseThrow(() -> new RuntimeException("Specification id not available"))
            ;
    }

    public Optional<String> html() {
        return jcrDocument().getStringProperty(HTML.jcrName(), PUBLISHED);
    }

    public void setHtml(final String html) {
        jcrDocument().setStringPropertyWithCheckout(HTML.jcrName(), html);
    }

    public Optional<String> json() {
        return jcrDocument().getStringProperty(JSON.jcrName(), PUBLISHED);
    }

    public void setJson(final String specificationJson) {
        jcrDocument().setStringPropertyWithCheckout(JSON.jcrName(), specificationJson);
    }

    public void setLastChangeCheckInstant(final Instant instant) {
        jcrDocument().setInstantPropertyNoCheckout(LAST_CHANGE_CHECK_TIME.jcrName, PUBLISHED, instant);
    }

    public Optional<Instant> lastChangeCheckInstant() {
        return jcrDocument().getInstantProperty(LAST_CHANGE_CHECK_TIME.jcrName, PUBLISHED);
    }

    public Optional<Instant> lastPublicationInstant() {
        return jcrDocument().getLastPublicationInstant();
    }

    public void save() {
        jcrDocument().save();
    }

    public void saveAndPublish() {
        jcrDocument().saveAndPublish();
    }

    @Override public String toString() {
        return "ApiSpecification{documentLifecycleSupport=" + jcrDocumentLifecycleSupport + '}';
    }

    private JcrDocumentLifecycleSupport jcrDocument() {
        return jcrDocumentLifecycleSupport;
    }

    enum Properties {
        HTML("website:html"),
        JSON("website:json"),
        SPECIFICATION_ID("website:specification_id"),
        LAST_CHANGE_CHECK_TIME("website:lastChangeCheckInstant"),
        ;

        private final String jcrName;

        Properties(final String jcrName) {
            this.jcrName = jcrName;
        }

        public String jcrName() {
            return jcrName;
        }
    }
}
