package uk.nhs.digital.apispecs.apigee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import uk.nhs.digital.apispecs.model.OpenApiSpecificationStatus;

import java.beans.ConstructorProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApigeeSpecificationsStatuses {

    private final List<OpenApiSpecificationStatus> contents;

    @ConstructorProperties({"contents"})
    public ApigeeSpecificationsStatuses(final List<OpenApiSpecificationStatus> contents) {
        this.contents = contents;
    }

    public List<OpenApiSpecificationStatus> getContents() {
        return contents;
    }
}
