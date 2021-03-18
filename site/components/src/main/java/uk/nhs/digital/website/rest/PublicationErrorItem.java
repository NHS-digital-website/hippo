package uk.nhs.digital.website.rest;

import javax.validation.ConstraintViolation;

public class PublicationErrorItem {

    private String field;
    private String invalidValue;
    private String originalMessage;
    private String annotation;

    public PublicationErrorItem(ConstraintViolation<PublicationModel> violation) {
        if (violation.getPropertyPath() != null) {
            this.field = violation.getPropertyPath().toString();
        }
        this.originalMessage = violation.getMessage();
        if (violation.getInvalidValue() != null) {
            this.invalidValue = violation.getInvalidValue().toString();
        }
        this.annotation = violation.getMessageTemplate();
    }

    public String getMessage() {
        return toString();
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getInvalidValue() {
        return invalidValue;
    }

    public void setInvalidValue(String invalidValue) {
        this.invalidValue = invalidValue;
    }

    public String getOriginalMessage() {
        return originalMessage;
    }

    public void setOriginalMessage(String originalMessage) {
        this.originalMessage = originalMessage;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String toString() {
        return "There is a problem with value '" + invalidValue + "' "
                + "in field '" + field + "'"
                + ": '" + originalMessage + "'"
                + ", annotation:'" + annotation + "'.";
    }
}
