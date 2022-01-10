package uk.nhs.digital.admin;

import static uk.nhs.digital.admin.AdminCommandProvider.ADMIN_COMMAND_INTERFACE_DOCTYPE;
import static uk.nhs.digital.admin.AdminCommandProvider.ADMIN_COMMAND_INTERFACE_DOC_JCR_PATH;

import org.onehippo.cms7.event.HippoEvent;
import org.onehippo.cms7.event.HippoEventConstants;
import org.onehippo.repository.events.HippoWorkflowEvent;
import org.onehippo.repository.events.PersistedHippoEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.function.Predicate;

/**
 * <p>
 * Invokes supplied {@linkplain AdminCommandEvenHandler}s, listening on cluster-wide event bus.
 * <p>
 * If any handler fails with an exception (whether on checking if it supports current command
 * or when actually trying to handle it), the listener logs an error and invokes remaining
 * handlers without propagating the exception.
 * <p>
 * Only activates the handlers for 'publish' events emitted as a result of a new version
 * of document {@linkplain AdminCommandProvider#ADMIN_COMMAND_INTERFACE_DOC_JCR_PATH} having
 * been published.
 */
public class AdminCommandEventListener implements PersistedHippoEventListener {

    private final Logger log = LoggerFactory.getLogger(AdminCommandEventListener.class);

    private final Predicate<HippoWorkflowEvent<?>> wrongDoctype = event -> !ADMIN_COMMAND_INTERFACE_DOCTYPE.equals(event.documentType());
    private final Predicate<HippoWorkflowEvent<?>> wrongPath = event -> !ADMIN_COMMAND_INTERFACE_DOC_JCR_PATH.equals(event.subjectPath());
    private final Predicate<HippoWorkflowEvent<?>> wrongAction = event -> !"publish".equals(event.action());
    private final Predicate<HippoWorkflowEvent<?>> failed = event -> !event.success();

    private final AdminCommandProvider adminCommandProvider;
    private final Set<AdminCommandEvenHandler> adminCommandEvenHandlers;

    public AdminCommandEventListener(
        final AdminCommandProvider adminCommandProvider,
        final Set<AdminCommandEvenHandler> adminCommandEvenHandlers
    ) {
        this.adminCommandProvider = adminCommandProvider;
        this.adminCommandEvenHandlers = adminCommandEvenHandlers;
    }

    @Override public String getEventCategory() {
        return HippoEventConstants.CATEGORY_WORKFLOW;
    }

    @Override public String getChannelName() {
        return "admin-commands";
    }

    @Override public boolean onlyNewEvents() {
        return true;
    }

    @Override public void onHippoEvent(final HippoEvent event) {
        @SuppressWarnings("rawtypes") final HippoWorkflowEvent workflowEvent = new HippoWorkflowEvent(event);

        if (notEligible(workflowEvent)) {
            log.debug("Ignoring ineligible event '{}' for document {}", workflowEvent.action(), workflowEvent.subjectPath());
            return;
        }

        final String documentHandleUuid = workflowEvent.subjectId();
        final String documentPath = workflowEvent.subjectPath();
        final String user = workflowEvent.user();

        final AdminCommand adminCommand = adminCommandProvider.currentCommandFromAdminCommandInterface();

        log.debug("Admin Command Interface document has been published: {} ({}).", documentPath, documentHandleUuid);
        log.info("Admin Command '{}' received, issued by user '{}'.", adminCommand, user);

        adminCommandEvenHandlers.stream()
            .filter(handler -> safelyCheckIfSupports(handler, adminCommand))
            .forEach(handler -> safelyHandle(handler, adminCommand));
    }

    @SuppressWarnings("rawtypes")
    private boolean notEligible(final HippoWorkflowEvent workflowEvent) {
        return wrongDoctype.or(wrongPath).or(wrongAction).or(failed).test(workflowEvent);
    }

    private boolean safelyCheckIfSupports(final AdminCommandEvenHandler handler, final AdminCommand command) {
        try {
            return handler.supports(command);
        } catch (final Exception e) {
            log.error(String.format("Failed to determine whether admin command is supported by given handler; command: %s.", command), e);
        }

        return false;
    }

    private void safelyHandle(final AdminCommandEvenHandler handler, final AdminCommand command) {
        try {
            handler.execute(command);
        } catch (final Exception e) {
            log.error(String.format("Failed to handle command: %s.", command), e);
        }
    }
}
