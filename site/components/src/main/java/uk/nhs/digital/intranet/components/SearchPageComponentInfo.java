package uk.nhs.digital.intranet.components;

import org.hippoecm.hst.core.parameters.Parameter;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;

public interface SearchPageComponentInfo extends EssentialsListComponentInfo {

    @Parameter(name = "peopleLimit", defaultValue = "2")
    int getPeopleLimit();
}
