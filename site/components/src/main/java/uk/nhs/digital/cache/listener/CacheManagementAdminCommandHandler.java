package uk.nhs.digital.cache.listener;

import uk.nhs.digital.admin.AdminCommand;
import uk.nhs.digital.admin.AdminCommandEventHandler;
import uk.nhs.digital.cache.HeavyContentCache;

/**
 * Purges cache upon receiving admin command {@code PURGE-HEAVY-CONTENT-CACHE}.
 */
public class CacheManagementAdminCommandHandler implements AdminCommandEventHandler {

    public static final String COMMAND_KEYWORD = "PURGE-HEAVY-CONTENT-CACHE";

    private final HeavyContentCache<String, String> cache;

    public CacheManagementAdminCommandHandler(final HeavyContentCache<String, String> cache) {
        this.cache = cache;
    }

    @Override public boolean supports(final AdminCommand adminCommand) {
        return adminCommand.isFor(COMMAND_KEYWORD);
    }

    @Override public void execute(final AdminCommand adminCommand) {
        cache.purge();
    }
}
