package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.DropDownList;
import org.hippoecm.hst.core.parameters.Parameter;

public interface JobsFeedComponentInfo {
    @Parameter(
        name = "header",
        required = true,
        defaultValue = "Job Listings",
        displayName = "Header"
    )
    String getHeader();

    @Parameter(
        required = true,
        name = "numToDisplay",
        defaultValue = "4",
        displayName = "Number to Display"
    )
    String getNumToDisplay();

    @Parameter(
        name = "button1Text",
        displayName = "Button 1 text"
    )
    String getButton1Text();

    @Parameter(
        name = "button1Url",
        displayName = "Button 1 url"
    )
    String getButton1Url();

    @Parameter(
        name = "button2Text",
        displayName = "Button 2 text"
    )
    String getButton2Text();

    @Parameter(
        name = "button2Url",
        displayName = "Button 2 url"
    )
    String getButton2Url();

    @Parameter(
        required = true,
        name = "feedMasterUri",
        displayName = "Feed master URI"
    )
    String getFeedMasterUri();

    @Parameter(
        name = "vacancyType",
        defaultValue = "Internal and external",
        displayName = "Vacancy type"
        )
    @DropDownList({"External only", "Internal only", "Internal and external"})
    String getVacancyType();

    @Parameter(
        name = "keywords",
        displayName = "Keywords"
    )
    String getKeywords();

    @Parameter(
        name = "keywordRule",
        displayName = "Keyword Rule"
        )
    @DropDownList({"All", "Any"})
    String getKeywordRule();

    @Parameter(
        name = "postcode",
        displayName = "Postcode"
    )
    String getPostcode();
}
