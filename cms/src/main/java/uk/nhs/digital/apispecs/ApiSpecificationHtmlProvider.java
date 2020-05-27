package uk.nhs.digital.apispecs;

import uk.nhs.digital.apispecs.model.ApiSpecificationDocument;

public interface ApiSpecificationHtmlProvider {

    String getHtmlForSpec(final ApiSpecificationDocument apiSpecificationDocument);

}
