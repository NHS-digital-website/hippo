package uk.nhs.digital.common.forms.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import uk.nhs.digital.common.forms.model.Subscriber;
import uk.nhs.digital.common.forms.model.Topics;

import java.util.Arrays;
import java.util.List;

public class SubscriberFactoryTest {

    @Test
    public void should_create_subscriber_with_topics() {
        final String emailAddress = "test@test.com";
        final List<String> topicCodes = Arrays.asList("code1", "code2");

        final Subscriber subscriber = SubscriberFactory.create(emailAddress, topicCodes);
        final Topics topics = subscriber.getTopics();

        assertNotNull(subscriber);
        assertNotNull(topics);
        assertNotNull(topics.getTopics());
        assertEquals(emailAddress, subscriber.getEmail());
        assertEquals(2, topics.getTopics().size());
        assertEquals("code1", topics.getTopics().get(0).getCode());
        assertEquals("code2", topics.getTopics().get(1).getCode());
    }
}
