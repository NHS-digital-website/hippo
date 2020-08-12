package uk.nhs.digital.common.forms.factory;

import uk.nhs.digital.common.forms.model.Subscriber;
import uk.nhs.digital.common.forms.model.Topic;
import uk.nhs.digital.common.forms.model.Topics;

import java.util.List;
import java.util.stream.Collectors;

public class SubscriberFactory {

    public static Subscriber create(final String email, final List<String> topicCodes) {
        final Topics topics = new Topics(topicCodes.stream().map(Topic::new).collect(Collectors.toList()));
        return new Subscriber(email, false, topics);
    }
}
