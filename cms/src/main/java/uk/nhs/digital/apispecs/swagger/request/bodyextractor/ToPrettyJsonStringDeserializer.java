package uk.nhs.digital.apispecs.swagger.request.bodyextractor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class ToPrettyJsonStringDeserializer extends StdDeserializer<String> {

    public ToPrettyJsonStringDeserializer() {
        this(null);
    }

    public ToPrettyJsonStringDeserializer(final Class<?> vc) {
        super(vc);
    }

    @Override
    public String deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        return p.getCodec().<JsonNode>readTree(p).toPrettyString();
    }
}
