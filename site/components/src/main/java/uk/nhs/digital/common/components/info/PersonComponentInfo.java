package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.Parameter;

public interface PersonComponentInfo {

    @Parameter(
        name = "businessUnitsLimit",
        displayName = "Business units limit",
        defaultValue = "10"
    )
    int getBusinessUnitsLimit();
}
