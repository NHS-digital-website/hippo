Feature: Faceted search

    As a content consumer I need to be able to filter search results so I can easily
    navigate to the documents I'm interested in.


    Scenario: All expected facets are displayed
        Given I navigate to the "home" page
        When I search for "zzFacets_Testzz"
        Then I should see the list with title "DOCUMENT TYPE" containing:
            | Publication (2) |
            | Data set (1)    |
        And I should see the list with title "PUBLICATION STATUS" containing:
            | Published (2) |
        And I should see the list with title "INFORMATION TYPE" containing:
            | Experimental statistics (2) |
            | Audit (1)                   |
            | National statistics (1)     |
        And I should see the list with title "GEOGRAPHICAL COVERAGE" containing:
            | England (1)          |
            | Northern Ireland (1) |
            | Scotland (1)         |
        And I should see the list with title "GEOGRAPHICAL GRANULARITY" containing:
            | Cancer networks (2)  |
            | Ambulance Trusts (1) |
            | Care Trusts (1)      |
            | Country (1)          |
            | County (1)           |
        And I should see the list with title "YEAR" containing:
            | 2018 (2) |
            | 2017 (1) |

    Scenario: All expected NIL facets are displayed
        Given I navigate to the "home" page
        When I search for "NilTaxonomySearchTerm"
        Then I should see the list with title "DOCUMENT TYPE" containing:
            | Methodology (3) |
        And I should see the list with title "GEOGRAPHICAL COVERAGE" containing:
            | England (3)  |
        And I should see the list with title "ASSURED" containing:
            | Yes (2)  |
            | No (1)   |
        And I should see the list with title "PUBLISHED BY" containing:
            | NHS Digital (3)  |
        And I should see the list with title "REPORTING LEVEL" containing:
            | CCG (1)                                           |
            | CCG and National (1)                              |
            | CCG and National GP registered population (1)     |

    Scenario: Months are not shown initially
        Given I navigate to the "home" page
        When I search for "zzFacets_Testzz"
        Then I should not see element with title "MONTH"

    Scenario: Months are shown when a year is selected
        Given I navigate to the "home" page
        When I search for "zzFacets_Testzz"
        And I click on the "2018" link
        Then I should see the list with title "MONTH" containing:
            | January (2) |
        And I should see the list with title "YEAR" containing:
            | 2018   |
        When I click on the "2018" link
        Then I should not see element with title "MONTH"
        And I should see the list with title "YEAR" containing:
            | 2018 (2) |
            | 2017 (1) |

    Scenario: Navigation and filtering
        Given I navigate to the "home" page
        When I search for "zzFacets_Testzz"
        Then I should see 3 search results
        When I click on the "Publication" link
        Then I should see 2 search results
        And I should see the list with title "DOCUMENT TYPE" containing:
            | Publication   |
        When I click on the "Cancer networks" link
        Then I should see 1 search results
        When I can click on the "Publication" link
        Then I should see 2 search results

    Scenario: Sticky facets when searching for new queries
        Given I navigate to the "home" page
        When I search for "zzFacets_Testzz"
        And I click on the "Publication" link
        And I click on the "2018" link
        Then I should see 1 search results
        When I search for "WeightSearchTerm"
        Then I should see 2 search result
        And I should see the list with title "DOCUMENT TYPE" containing:
            | Publication   |
        And I should see the list with title "YEAR" containing:
            | 2018   |

    Scenario: Reset facets
        Given I navigate to the "home" page
        When I search for "zzFacets_Testzz"
        And I click on the "England" link
        Then I should see 1 search results
        When I can click on the "Reset" link
        Then I should see 3 search results
        And I should see the list with title "GEOGRAPHICAL COVERAGE" including:
            | England (1) |

    Scenario: Sort by maintains selected facets
        Given I navigate to the "home" page
        When I search for "zzFacets_Testzz"
        And I click on the "Cancer networks" link
        And I click on the "Order by date" link
        Then I can see the search description matching "2 results containing 'zzFacets_Testzz', sorted by date."
        And I should see the list with title "GEOGRAPHICAL GRANULARITY" including:
            | Cancer networks |

    Scenario: Upcoming publications are returned
        Given I navigate to the "search" page
        Then I should see the list with title "PUBLICATION STATUS" containing:
            | Published ( ... |
            | Upcoming ( ...  |
        When I click on the "Upcoming" button

    Scenario: Facet Category tree drill down
        Given I navigate to the "home" page
        When I search for "zzFacets_Testzz"
        And I should see 3 search results
        When I click on the "Facets Other Publication" link
        Then I should see page titled "Facets Other Publication"
        When I search for "zzFacets_Testzz"
        When I click on the "Facets Publication" link
        Then I should see page titled "Facets Publication"
        When I search for "zzFacets_Testzz"
        When I click on the "Facets Dataset" link
        Then I should see page titled "Facets Dataset"