package uk.nhs.digital.apispecs.swagger.model;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import uk.nhs.digital.apispecs.swagger.request.bodyextractor.ToPrettyJsonStringDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SchemaObject {

    @JsonDeserialize(using = ToPrettyJsonStringDeserializer.class)
    private String example;

    public String getExample() {
        return example;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, MULTI_LINE_STYLE)
            .append("example", example)
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

        final SchemaObject schema = (SchemaObject) o;

        return new EqualsBuilder()
            .append(example, schema.example)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(example)
            .toHashCode();
    }
}
