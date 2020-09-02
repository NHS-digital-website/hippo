package uk.nhs.digital.apispecs.swagger.model;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import org.mockito.Mockito;
import uk.nhs.digital.apispecs.swagger.request.bodyextractor.ToPrettyJsonStringDeserializer;

import java.io.IOException;

public class ToPrettyJsonStringDeserializerTest {

    @Test
    public void deserialisesValueAsPrettyPrintedJson() throws IOException {

        // given
        final ToPrettyJsonStringDeserializer deserializer = new ToPrettyJsonStringDeserializer();

        final DeserializationContext context = Mockito.mock(DeserializationContext.class);
        final JsonParser parser = Mockito.mock(JsonParser.class);
        final ObjectCodec codec = Mockito.mock(ObjectCodec.class);
        final JsonNode node = Mockito.mock(JsonNode.class);

        given(parser.getCodec()).willReturn(codec);
        given(codec.readTree(parser)).willReturn(node);

        // when
        deserializer.deserialize(parser, context);

        // then
        then(node).should().toPrettyString();
    }
}
