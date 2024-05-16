package uk.nhs.digital.apispecs.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import uk.nhs.digital.apispecs.model.ProxygenOpenApiSpecification;

import java.util.LinkedList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProxygenOpenApiSpecifications extends LinkedList<ProxygenOpenApiSpecification> {

    public List<ProxygenOpenApiSpecification> getContents() {
        return this;
    }
}
