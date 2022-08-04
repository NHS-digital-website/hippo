package uk.nhs.digital.apispecs.model;

import static org.hippoecm.repository.util.WorkflowUtils.Variant.PUBLISHED;
import static uk.nhs.digital.apispecs.model.ApiSpecificationDocument.Properties.JSON;

import uk.nhs.digital.apispecs.jcr.JcrDocumentLifecycleSupport;

import java.util.Optional;

public class ApiSpecificationDocument {

    private final JcrDocumentLifecycleSupport jcrDocumentLifecycleSupport;

    private ApiSpecificationDocument(final JcrDocumentLifecycleSupport jcrDocumentLifecycleSupport) {
        this.jcrDocumentLifecycleSupport = jcrDocumentLifecycleSupport;
    }

    public static ApiSpecificationDocument from(final JcrDocumentLifecycleSupport jcrDocumentLifecycleSupport) {
        return new ApiSpecificationDocument(jcrDocumentLifecycleSupport);
    }

    public String path() {
        return jcrDocument().path();
    }

    public Optional<String> json() {
        return jcrDocument().getStringProperty(JSON.jcrName, PUBLISHED);
    }

    public void setJsonForPublishing(final String specificationJson) {
        jcrDocument().setStringPropertyWithCheckout(JSON.jcrName, specificationJson);
    }

    public void save() {
        jcrDocument().save();
    }

    public void saveAndPublish() {
        jcrDocument().saveAndPublish();
    }

    @Override public String toString() {
        return getClass().getSimpleName() + "@" + path();
    }

    private JcrDocumentLifecycleSupport jcrDocument() {
        return jcrDocumentLifecycleSupport;
    }

    enum Properties {
        JSON("website:json");

        private final String jcrName;

        Properties(final String jcrName) {
            this.jcrName = jcrName;
        }
    }
}
