package uk.nhs.digital.common.forms.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "topics")
public class Topics {

    @SuppressWarnings("unused")
    @JacksonXmlProperty(isAttribute = true)
    private final String type = "array";

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "topic")
    private List<Topic> topics;

    public List<Topic> getTopics() {
        return topics;
    }

    public Topics(final List<Topic> topics) {
        this.topics = topics;
    }

    public Topics() {
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }
}
