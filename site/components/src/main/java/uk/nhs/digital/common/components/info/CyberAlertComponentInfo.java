package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.Parameter;

public interface CyberAlertComponentInfo {

    @Parameter(
        name = "Number to Display",
        required = true,
        defaultValue = "3",
        description = "Number of alerts to display"
    )
    int getNumberOfAlertsToDisplay();
}
