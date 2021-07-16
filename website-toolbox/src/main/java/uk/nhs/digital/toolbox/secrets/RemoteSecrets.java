package uk.nhs.digital.toolbox.secrets;

public interface RemoteSecrets {

    /**
     * Checks if a string starts with 'REMOTE::'.
     *
     * @param valuePath  A string that should start with 'REMOTE::'
     * @return true if the value starts with 'REMOTE::'
     */
    default boolean isRemoteValue(String valuePath) {
        return valuePath.toUpperCase().startsWith("REMOTE::");
    }

    /**
     * Removes 'REMOTE::' from the start of a string.
     *
     * @param valuePath A string that should start with 'REMOTE::'
     * @return The value after 'REMOTE::'
     */
    default String toRemoteAddress(String valuePath) {
        if (isRemoteValue(valuePath)) {
            return valuePath.substring(8);
        }
        return valuePath;
    }

    /**
     * A remote value getter.
     *
     * @param addressOfRemoteValue An address of a remote value
     * @return The value held on the remote system, null otherwise
     */
    String getRemoteValue(String addressOfRemoteValue);
}
