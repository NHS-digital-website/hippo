package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.Parameter;

public interface FeedHubComponentInfo {
    @Parameter(
        name = "limit",
        defaultValue = "10",
        displayName = "Query Limit"
    )
    int getLimit();
}
