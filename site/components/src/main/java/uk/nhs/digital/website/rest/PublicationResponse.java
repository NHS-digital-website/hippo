package uk.nhs.digital.website.rest;

import java.util.List;

public class PublicationResponse {

    private String message;
    private List<PublicationErrorItem> validationErrors;

    public PublicationResponse() {
    }

    public PublicationResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PublicationErrorItem> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<PublicationErrorItem> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
