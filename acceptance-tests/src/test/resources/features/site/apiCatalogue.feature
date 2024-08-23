
Feature: API Catalogue in Developer hub

    Scenario: API Catalogue renders all results when no filters applied
        Given I navigate to "Static API Catalogue" page
        Then I should see page titled "API and integration catalogue"
        And I can see labelled element "document.title" with content "API and integration catalogue"
        Then I can click on the "Page 2" link
        Then I can click on the "Page 3" link
        Then I can click on the "Page 1" link

@skip
    Scenario: API Catalogue renders filtered results when filters applied
        Given I navigate to "Static API Catalogue" page
        When I click on the label for "toggler_care-setting"
        And I click on the "Filter by Transport / infrastructure" link
        And I can see the following links:
            | text                                     | href                                                                   |
            | Message Exchange for Social Care and Health (MESH) API | /site/developer/api-catalogue/message-exchange-for-social-care-and-health-api |
        And I should see elements with attributes:
            | text         | class                                                                             |
            | Transport / infrastructure    | nhsd-a-checkbox__label nhsd-!t-margin-bottom-0 nhsd-t-body-s     |
    @skip
    Scenario: API Catalogue renders filtered results when filters applied via taxonomy tag
        Given I navigate to "Static API Catalogue" page
        When I click on the label for "toggler_care-setting"
        And I click on the "Filter by Transport / infrastructure" labelled tag
        Then the index is rendered with entries:
            | text | href  | aria-label                                    |
            | P    | #p    | Jump to articles starting with the letter 'P' |
        And I can see the following links:
            | text                                     | href                                                                   |
            | Personal Demographics Service - FHIR API | /site/developer/api-catalogue/personal-demographics-service---fhir-api |
        And I should see elements with attributes:
            | text         | class                                                                             |
            | Inpatient    | nhsd-a-tag filter-tag-yellow-highlight nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1 |

    Scenario: API Catalogue is available through Search
        Given I navigate to the "home" page
        When I search for "Test API catalogue"
        And I click on link "Test API catalogue"
        Then I should see page titled "API and integration catalogue"

    Scenario: API Catalogue search returns a results
        Given I navigate to "Static API Catalogue" page
        When I enter search term "Deprecated"
        Then I should see the search result with the id "deprecated-api"

