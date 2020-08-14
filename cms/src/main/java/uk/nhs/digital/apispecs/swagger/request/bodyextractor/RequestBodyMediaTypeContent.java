package uk.nhs.digital.apispecs.swagger.request.bodyextractor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
class RequestBodyMediaTypeContent {

    // no-op: placeholder for Jackson to have something to deserialize to

    @Override
    public String toString() {
        return "RequestBodyMediaTypeContent{}";
    }

    @Override
    public int hashCode() {
        // temporary construct to make testing easier, soon to be replaced by a subsequent story
        return toString().hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        // temporary construct to make testing easier, soon to be replaced by a subsequent story
        return toString().equals(String.valueOf(other));
    }
}
