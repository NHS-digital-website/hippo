package uk.nhs.digital.apispecs.swagger.request.bodyextractor;

import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

public class ToPrettyJsonStringDeserializerTest {

    @Test
    public void testDeserialization() throws IOException {
        ToPrettyJsonStringDeserializer deserializer = new ToPrettyJsonStringDeserializer();

        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        ObjectCodec codec = Mockito.mock(ObjectCodec.class);
        JsonNode node = Mockito.mock(JsonNode.class);

        when(parser.getCodec()).thenReturn(codec);
        when(codec.readTree(parser)).thenReturn(node);

        deserializer.deserialize(parser, context);

        verify(node, times(1)).toPrettyString();
    }
}
