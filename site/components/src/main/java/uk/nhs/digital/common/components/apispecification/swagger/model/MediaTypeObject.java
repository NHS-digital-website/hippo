package uk.nhs.digital.common.components.apispecification.swagger.model;

import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import uk.nhs.digital.common.components.apispecification.swagger.request.bodyextractor.ToPrettyJsonStringDeserializer;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaTypeObject {

    private String name;

    @JsonDeserialize(using = ToPrettyJsonStringDeserializer.class)
    private String example;

    private Map<String, ExampleObject> examples;

    private SchemaObject schema;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getExample() {
        return example;
    }

    public Collection<ExampleObject> getExamples() {
        return Optional.ofNullable(examples)
            .orElse(emptyMap())
            .values();
    }

    public SchemaObject getSchema() {
        return schema;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, MULTI_LINE_STYLE)
            .append("name", name)
            .append("example", example)
            .append("examples", examples)
            .append("schema", schema)
            .toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final MediaTypeObject that = (MediaTypeObject) o;

        return new EqualsBuilder()
            .append(name, that.name)
            .append(example, that.example)
            .append(examples, that.examples)
            .append(schema, that.schema)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(name)
            .append(example)
            .append(examples)
            .append(schema)
            .toHashCode();
    }
}
