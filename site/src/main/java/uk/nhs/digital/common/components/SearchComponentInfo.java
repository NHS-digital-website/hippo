package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.parameters.Parameter;

public interface SearchComponentInfo {

    @Parameter(name = "limit", defaultValue = "10")
    Integer getLimit();
}
