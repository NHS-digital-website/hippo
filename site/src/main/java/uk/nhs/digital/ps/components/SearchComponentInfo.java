package uk.nhs.digital.ps.components;

import org.hippoecm.hst.core.parameters.Parameter;

public interface SearchComponentInfo {

    @Parameter(name = "limit", defaultValue = "10")
    Integer getLimit();
}
