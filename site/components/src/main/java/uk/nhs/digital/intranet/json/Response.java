package uk.nhs.digital.intranet.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
    private String id;
    private int status;
    private String body;

    public String getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }
}
