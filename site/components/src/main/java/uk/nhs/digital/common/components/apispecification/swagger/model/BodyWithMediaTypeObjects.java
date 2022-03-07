package uk.nhs.digital.common.components.apispecification.swagger.model;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BodyWithMediaTypeObjects {

    @JsonProperty
    private MediaTypeObjects content;

    public List<MediaTypeObject> getMediaTypes() {

        return Optional.ofNullable(content).orElse(new MediaTypeObjects())
            .entrySet().stream()
            .map(mapEntry -> {
                final String contentTypeName = mapEntry.getKey();
                final MediaTypeObject mediaTypeObject = mapEntry.getValue();

                mediaTypeObject.setName(contentTypeName);

                return mediaTypeObject;
            })
            .collect(toList());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, MULTI_LINE_STYLE)
            .append("content", content)
            .toString();
    }

    @Override public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final BodyWithMediaTypeObjects that = (BodyWithMediaTypeObjects) o;

        return new EqualsBuilder()
            .append(content, that.content)
            .isEquals();
    }

    @Override public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(content)
            .toHashCode();
    }
}
