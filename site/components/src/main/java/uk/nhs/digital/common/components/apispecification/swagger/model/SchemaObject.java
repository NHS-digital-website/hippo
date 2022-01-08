package uk.nhs.digital.common.components.apispecification.swagger.model;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import uk.nhs.digital.common.components.apispecification.swagger.request.bodyextractor.ToPrettyJsonStringDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SchemaObject {

    @JsonDeserialize(using = ToPrettyJsonStringDeserializer.class)
    private String example;

    @JsonAlias("default")
    private String defaultValue;

    public String getExample() {
        return example;
    }

    public String getDefault() {
        return defaultValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, MULTI_LINE_STYLE)
            .append("example", example)
            .append("default", defaultValue)
            .toString();
    }

    @Override public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final SchemaObject that = (SchemaObject) o;

        return new EqualsBuilder().append(example, that.example).append(defaultValue, that.defaultValue).isEquals();
    }

    @Override public int hashCode() {
        return new HashCodeBuilder(17, 37).append(example).append(defaultValue).toHashCode();
    }
}
