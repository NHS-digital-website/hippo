package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.*;
import org.onehippo.cms7.essentials.components.info.*;

@FieldGroupList({@FieldGroup(titleKey = "Buttons", value = {"viewAllUrl", "viewUpcoming"})})
public interface PublicationListComponentInfo extends EssentialsListComponentInfo {
    @Parameter(
        name = "viewAllUrl",
        displayName = "View all URL",
        required = true,
        defaultValue = "search/document-type/publication/publicationStatus/true?area=data&sort=date"
    )
    String getViewAllUrl();

    @Parameter(
        name = "viewUpcoming",
        displayName = "View upcoming",
        required = true,
        defaultValue = "search/publicationStatus/false?area=data&sort=date"
    )
    String getViewUpcomingUrl();

    @Parameter(
        name = "Display Tier 3 Publications",
        defaultValue = "publicationsystem:publicationtier"
    )
    Boolean getDisplayTier3();
}
