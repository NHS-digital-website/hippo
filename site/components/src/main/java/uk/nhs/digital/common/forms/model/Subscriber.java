package uk.nhs.digital.common.forms.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "subscriber")
public class Subscriber {

    private String email;
    @JacksonXmlProperty(localName = "send-notifications")
    private boolean sendNotifications;
    private Topics topics;

    public Subscriber(String email, boolean sendNotifications, Topics topics) {
        this.email = email;
        this.sendNotifications = sendNotifications;
        this.topics = topics;
    }

    public Subscriber() {
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSendNotifications(boolean sendNotifications) {
        this.sendNotifications = sendNotifications;
    }

    public String getEmail() {
        return email;
    }

    public boolean isSendNotifications() {
        return sendNotifications;
    }

    public Topics getTopics() {
        return topics;
    }

    public void setTopics(Topics topics) {
        this.topics = topics;
    }
}
