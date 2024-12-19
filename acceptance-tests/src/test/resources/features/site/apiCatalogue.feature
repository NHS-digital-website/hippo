Feature: API Catalogue in Developer hub

    Scenario: API Catalogue renders all results when no filters applied
        Given I navigate to "Static API Catalogue" page
        Then I should see page titled "API and integration catalogue"
        And I can see labelled element "document.title" with content "API and integration catalogue"
        Then I can click on the "Page 2" link
        Then I can click on the "Page 3" link
        Then I can click on the "Page 1" link
        Then I click on the "Next" labelled button
        Then I click on the "Back" labelled button

    Scenario: API Catalogue renders filtered results when filters applied
        Given I navigate to "Static API Catalogue" page
        When I click on the label for "responsive_toggler_integration-type"
        And I can see the following links:
            | text                                     | href                                                                   |
            | Access Control Service HL7 V3 API | /site/developer/api-catalogue/access-control-service-hl7-v3 |
        When I click on the link named "Access Control Service HL7 V3 API"
        Then I should see page titled "Access Control Service HL7 V3 API"

    Scenario: API Catalogue renders filtered results when filters applied via taxonomy tag
        Given I navigate to "Static API Catalogue" page
        When I click on the label for "responsive_toggler_integration-type"
        And I can see the following links:
            | text                                     | href                                                                   |
            | Access Control Service HL7 V3 API | /site/developer/api-catalogue/access-control-service-hl7-v3 |
        When I click on the link named "HL7 V3"
        And I can see the following links:
            | text                                     | href                                                                   |
            | Access Control Service HL7 V3 API | /site/developer/api-catalogue/access-control-service-hl7-v3 |
        When I click on the link named "Access Control Service HL7 V3 API"
        Then I should see page titled "Access Control Service HL7 V3 API"

    Scenario: API Catalogue is available through Search
        Given I navigate to the "home" page
        When I search for "Test API catalogue"
        And I click on link "Test API catalogue"
        Then I should see page titled "API and integration catalogue"

    Scenario: API Catalogue search returns a results
        Given I navigate to "Static API Catalogue" page
        When I enter search term "Deprecated"
        Then I should see the search result with the id "deprecated-api"

