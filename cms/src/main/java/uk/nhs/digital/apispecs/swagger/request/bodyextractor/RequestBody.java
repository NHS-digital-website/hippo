package uk.nhs.digital.apispecs.swagger.request.bodyextractor;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestBody {

    @JsonProperty
    private RequestBodyMediaTypes content;

    public Collection<RequestBodyMediaTypeContent> getMediaTypes() {

        return Optional.ofNullable(content).orElse(new RequestBodyMediaTypes())
            .entrySet().stream()
            .map(mapEntry -> {
                final String contentTypeName = mapEntry.getKey();
                final RequestBodyMediaTypeContent requestBodyMediaTypeContent = mapEntry.getValue();

                requestBodyMediaTypeContent.setName(contentTypeName);

                return requestBodyMediaTypeContent;
            })
            .collect(toList());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("content", content)
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

        final RequestBody that = (RequestBody) o;

        return new EqualsBuilder()
            .append(content, that.content)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(content)
            .toHashCode();
    }
}
