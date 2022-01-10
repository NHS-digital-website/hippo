package uk.nhs.digital.admin;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AdminCommand {

    public static final AdminCommand NULL = AdminCommand.with("NULL", Collections.emptyList());

    private final String keyword;
    private final List<String> arguments;

    private AdminCommand(final String keyword, final List<String> arguments) {
        Objects.requireNonNull(keyword, "Command's keyword is required.");

        this.keyword = keyword;
        this.arguments = Optional.ofNullable(arguments).map(ImmutableList::copyOf).orElse(ImmutableList.of());
    }

    public static AdminCommand with(final String keyword, final List<String> arguments) {
        return new AdminCommand(keyword, arguments);
    }

    public String keyword() {
        return keyword;
    }

    public List<String> arguments() {
        return ImmutableList.copyOf(arguments);
    }

    public boolean isFor(final String keyword) {
        return this.keyword.equals(keyword);
    }

    @Override public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final AdminCommand that = (AdminCommand) o;

        return new EqualsBuilder().append(keyword, that.keyword).append(arguments, that.arguments).isEquals();
    }

    @Override public int hashCode() {
        return new HashCodeBuilder(17, 37).append(keyword).append(arguments).toHashCode();
    }

    @Override public String toString() {
        return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
            .append("keyword", keyword)
            .append("arguments", arguments)
            .toString();
    }
}
