package uk.nhs.digital.admin;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static uk.nhs.digital.toolbox.exception.ExceptionUtils.wrapCheckedException;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.hippoecm.repository.util.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.toolbox.jcr.SessionProvider;

import java.util.List;
import java.util.Optional;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class AdminCommandProvider {

    private final Logger log = LoggerFactory.getLogger(AdminCommandProvider.class);

    public static final String ADMIN_COMMAND_INTERFACE_DOC_JCR_PATH =
        "/content/documents/administration/admin-command-interface";

    public static final String ADMIN_COMMAND_INTERFACE_DOCTYPE = "common:stringlist";

    private final SessionProvider sessionProvider;

    public AdminCommandProvider(final SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    /**
     * <p>
     * Looks up Admin Command Interface object and builds an AdminCommand from values found
     * in document's {@code common:entries} property, treating the first value as the command's keyword,
     * and the rest as its arguments (if present).
     * <p>
     * Values retrieved come from the 'published' variant node, so it's expected that this node exists
     * with no further checks done.
     */
    public AdminCommand currentCommandFromAdminCommandInterface() {

        Session session = null;

        try {
            session = sessionProvider.session();

            final List<String> rawStringValues = rawStringValuesFromAdminCommandInterfaceDoc(session);

            final Optional<String> commandKeyword = commandKeywordFrom(rawStringValues);

            final List<String> commandArguments = argumentsFrom(rawStringValues);

            return commandKeyword.map(keyword -> AdminCommand.with(keyword, commandArguments)).orElse(AdminCommand.NULL);

        } catch (final Exception e) {
            throw new RuntimeException(
                format(
                    "Failed to create %s from Admin Command Interface document %s.",
                    AdminCommand.class.getSimpleName(),
                    ADMIN_COMMAND_INTERFACE_DOC_JCR_PATH
                ),
                e
            );

        } finally {
            Optional.ofNullable(session).ifPresent(Session::logout);
        }
    }

    private List<String> rawStringValuesFromAdminCommandInterfaceDoc(final Session session) throws RepositoryException {
        final Node adminInterfaceDocHandleNode = adminInterfaceDocHandleNode(session);
        if (adminInterfaceDocHandleNode == null) {
            log.error(
                "Failed to identify current Admin Command; Admin Command Interface document's handle node was not found at {}",
                ADMIN_COMMAND_INTERFACE_DOC_JCR_PATH
            );
            return emptyList();
        }

        final Node publishedVariantNode = publishedVariantFrom(adminInterfaceDocHandleNode);
        if (publishedVariantNode == null) {
            log.error(
                "'Published' variant node not available for {}; has the document actually been published?",
                ADMIN_COMMAND_INTERFACE_DOC_JCR_PATH
            );
            return emptyList();
        }

        final List<String> rawStringValues = allStringValuesFrom(publishedVariantNode);
        return rawStringValues;
    }

    private Optional<String> commandKeywordFrom(final List<String> values) {
        return values.stream()
            .filter(StringUtils::isNotBlank)
            .findFirst();
    }

    private List<String> argumentsFrom(final List<String> rawValues) {
        return rawValues.stream()
            .skip(1) // the first element is the admin command keyword, and we have already extracted it
            .collect(toList());
    }

    private List<String> allStringValuesFrom(final Node publishedVariantNode) throws RepositoryException {

        return Optional.ofNullable(publishedVariantNode
                .getProperty("common:entries")
                .getValues()
            )
            .map(ImmutableList::copyOf)
            .orElse(ImmutableList.of())
            .stream()
            .map(value -> wrapCheckedException(value::getString))
            .filter(StringUtils::isNotBlank)
            .collect(toList());
    }

    private Node adminInterfaceDocHandleNode(final Session session) throws RepositoryException {
        return JcrUtils.getNodeIfExists(
            ADMIN_COMMAND_INTERFACE_DOC_JCR_PATH,
            session
        );
    }

    private Node publishedVariantFrom(final Node handleNode) throws RepositoryException {
        return JcrUtils.getDescendants(handleNode, ADMIN_COMMAND_INTERFACE_DOCTYPE, true).stream()
            .filter(node -> wrapCheckedException(() -> node.hasProperty("hippostd:state")))
            .filter(node -> wrapCheckedException(() -> "published".equals(node.getProperty("hippostd:state").getString())))
            .findAny()
            .orElse(null);
    }
}
