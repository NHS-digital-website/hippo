package uk.nhs.digital.common.forms.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "topic")
public class Topic {

    private String code;

    public Topic(final String code) {
        this.code = code;
    }

    public Topic() {
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
