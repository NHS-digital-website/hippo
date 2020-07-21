package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.Parameter;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;

public interface SearchComponentInfo extends EssentialsListComponentInfo {

    @Parameter(name = "contentSearchEnabled")
    boolean isContentSearchEnabled();

    @Parameter(name = "contentSearchTimeOut")
    long getContentSearchTimeOut();

    @Parameter(name = "fallbackEnabled", defaultValue = "true")
    boolean isFallbackEnabled();
}
