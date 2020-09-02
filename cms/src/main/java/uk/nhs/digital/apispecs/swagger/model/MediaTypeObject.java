package uk.nhs.digital.apispecs.swagger.model;

import static java.util.Collections.emptyMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaTypeObject {

    private String name;

    private Map<String, ParamExample> examples;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Collection<ParamExample> getExamples() {
        return Optional.ofNullable(examples)
            .orElse(emptyMap())
            .values();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("name", name)
            .append("examples", examples)
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
            .append(examples, that.examples)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(name)
            .append(examples)
            .toHashCode();
    }
}
