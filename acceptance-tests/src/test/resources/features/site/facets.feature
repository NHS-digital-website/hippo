Feature: Faceted search

    As a content consumer I need to be able to filter search results so I can easily
    navigate to the documents I'm interested in.


    Scenario: All expected facets are displayed
        Given I navigate to the "home" page
        When I search for "zzFacets_Testzz"
        Then I should see the "DOCUMENT TYPE" list containing:
            | Statistical Publication (2) |
            | Data Set (1)                |
        And I should see the "CATEGORY" list containing:
            | Social care (2) |
            | Conditions (1)  |
            | Workforce (1)   |
        And I should see the "INFORMATION TYPE" list containing:
            | Experimental statistics (2) |
            | Audit (1)                   |
            | National statistics (1)     |
        And I should see the "GEOGRAPHICAL COVERAGE" list containing:
            | England (1)       |
            | International (1) |
            | Scotland (1)      |
        And I should see the "GEOGRAPHICAL GRANULARITY" list containing:
            | Cancer networks (2)  |
            | Ambulance Trusts (1) |
            | Care Trusts (1)      |
            | Country (1)          |
            | County (1)           |
        And I should see the "YEAR" list containing:
            | 2018 (2) |
            | 2017 (1) |


    Scenario: Months are not shown initially
        Given I navigate to the "home" page
        When I search for "zzFacets_Testzz"
        Then I should not see element with title "MONTH"


    Scenario: Months are shown when a year is selected
        Given I navigate to the "home" page
        When I search for "zzFacets_Testzz"
        And I click on the "2018" link
        Then I should see the "MONTH" list containing:
            | January (2) |
        And I should see the "YEAR" list containing:
            | 2018 x |
        When I click on the "2018" link
        Then I should not see element with title "MONTH"
        And I should see the "YEAR" list containing:
            | 2018 (2) |
            | 2017 (1) |


    Scenario: Navigation and filtering
        Given I navigate to the "home" page
        When I search for "zzFacets_Testzz"
        Then I should see 3 search results
        When I click on the "Statistical Publication" link
        Then I should see 2 search results
        And I should see the "DOCUMENT TYPE" list containing:
            | Statistical Publication x |
        And I should see the "CATEGORY" list containing:
            | Social care (1) |
            | Workforce (1)   |
        When I click on the "Cancer networks" link
        Then I should see 1 search results
        When I can click on the "Statistical Publication" link
        Then I should see 2 search results
