package uk.nhs.digital.intranet.components;

import org.hippoecm.hst.core.parameters.Parameter;

public interface IntranetSiteHeaderInfo {

    @Parameter(name = "enableSearch", defaultValue = "true")
    boolean isEnableSearchEnabled();

}
