package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.Parameter;

public interface FeedListComponentInfo extends LatestNewsComponentInfo {

    /**
     * Boolean flag which indicates if events that happened in the past will not be shown.
     *
     * @return {@code true} if items should be hidden, {@code false} otherwise
     */
    @Parameter(
        name = "hidePastEvents",
        defaultValue = "true",
        displayName = "Hide Past Events"
    )
    Boolean getHidePastEvents();

    @Parameter(
        name = "titleText",
        required = false,
        defaultValue = "",
        displayName = "Title Text"
    )
    String getTitleText();

    @Parameter(
        name = "buttonText",
        required = false,
        defaultValue = "",
        displayName = "Button Text"
    )
    String getButtonText();

    @Parameter(
        name = "buttonDestination",
        required = false,
        defaultValue = "",
        displayName = "Button Destination"
    )
    String getButtonDestination();

    @Parameter(
        name = "secondaryButtonText",
        required = false,
        defaultValue = "",
        displayName = "Secondary Button Text"
    )
    String getSecondaryButtonText();

    @Parameter(
        name = "secondaryButtonDestination",
        required = false,
        defaultValue = "",
        displayName = "Secondary Button Destination"
    )
    String getSecondaryButtonDestination();
}
