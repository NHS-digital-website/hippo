Feature: Basic search
    As a content consumer I need to be able to search for publications
    that I am interested in so that I can use them

    Scenario: Clickable links
        Given I navigate to the "home" page
        When I search for "loremipsumdolor"
        And I click on link "loremipsumdolor"
        Then I should see page titled "loremipsumdolor"

    Scenario: Search term retained
        Given I navigate to the "home" page
        When I search for "loremipsumdolor"
        Then I should see:
            | Search Box | loremipsumdolor |

    Scenario: Clickable pagination links
        Given I navigate to the "home" page
        When I search for "lorem"
        Then I can click on the "Page 2" link
        And I should see:
            | Pagination page | 2 |
        When I can click on the "Previous" link
        Then I should see:
            | Pagination page | 1 |
        When I can click on the "Next" link
        Then I should see:
            | Pagination page | 2 |
        When I can click on the "First" link
        Then I should see:
            | Pagination page | 1 |

    Scenario: Publication searchable fields (title, summary, keyfacts)
        Given I navigate to the "home" page
        When I search for "titleTestApricots"
        Then I should see 1 search results
        And I can click on link "Publication fields titleTestApricots"
        When I search for "summaryTestBeetroot"
        Then I should see 1 search results
        And I can click on link "Publication fields titleTestApricots"
        When I search for "keyfactsTestCourgettes"
        Then I should see 1 search results
        And I can click on link "Publication fields titleTestApricots"

    Scenario: Dataset searchable fields (title, summary)
        Given I navigate to the "home" page
        When I search for "titleTestPotatoes"
        Then I should see 1 search results
        And I can click on link "dataset fields titleTestPotatoes"
        When I search for "summaryTestWalnuts"
        Then I should see 1 search results
        And I can click on link "dataset fields titleTestPotatoes"

    Scenario: Series searchable fields (title, summary)
        Given I navigate to the "home" page
        When I search for "titleTestStrawberries"
        Then I should see 1 search results
        And I can click on link "series fields titleTestStrawberries"
        When I search for "summaryTestBlackberries"
        Then I should see 1 search results
        And I can click on link "series fields titleTestStrawberries"

    Scenario: Indicator searchable fields (title, definition)
        Given I navigate to the "home" page
        When I search for "National Indicator"
        Then I should see 3 search results
        And I can click on link "National Indicator"
        When I search for "SearchableNationalIndicatorDefinition"
        Then I should see 1 search results
        And I can click on link "National Indicator"
        When I search for "SearchableNationalIndicatorPurpose"
        Then I should see 1 search results
        And I can click on link "National Indicator"

    Scenario: Pdf text is not matched with the search term
        Given I navigate to the "home" page
        And I search for "_Attachment_Search_Term_"
        Then I can see the search description matching "No results for: _Attachment_Search_Term_"

    Scenario: Using the sort by options
        Given I navigate to the "home" page
        When I search for "WeightSearchTerm"
        Then I should see the weight search test results ordered by relevance
        When I can click on the "Order by date" link
        Then I should see the weight search test results ordered by date
        When I can click on the "Order by relevance" link
        Then I should see the weight search test results ordered by relevance

    Scenario: Upcoming publications are ordered correctly in increasing date order
        Given I navigate to the "search" page
        When I click on the "Upcoming" link
        And I click on the "National statistics" link
        Then I should see search results starting with:
            | 2026 - Upcoming |

    Scenario: Search results description is shown correctly with and without search terms
        When I navigate to the "search" page
        Then I can see the search description matching "\d+ results sorted by relevance\."
        When I click on the "Order by date" link
        Then I can see the search description matching "\d+ results sorted by date\."
        When I search for "test"
        Then I can see the search description matching "\d+ results containing 'test', sorted by relevance\."
        When I click on the "Order by date" link
        Then I can see the search description matching "\d+ results containing 'test', sorted by date\."

    Scenario: Search terms are displayed correctly on the results page
        Given I navigate to the "home" page
        When I search for "test_search"
        Then I should see the search term "test_search" on the results page

    Scenario: Quoted search terms are displayed correctly on the results page
        Given I navigate to the "home" page
        When I search for ""test_search""
        Then I should see the search term ""test_search"" on the results page

    Scenario: Searching with no search term provided displays the results page with full, unfiltered result set
        Given I navigate to the "home" page
        When I search for ""
        Then I should see the list with title "DOCUMENT TYPE" including:
            | Publication ( ...         |
            | Data set ( ...            |
            | Series / Collection ( ... |
            | Methodology ( ...         |

    Scenario: Navigating to the search page displays the results page with full, unfiltered result set
        Given I navigate to the "search" page
        Then I should see the list with title "DOCUMENT TYPE" including:
            | Publication ( ...         |
            | Data set ( ...            |
            | Series / Collection ( ... |
            | Methodology ( ...         |

    Scenario: Navigating to the search page displays the results page with full, unfiltered result set list, sorted and relevance list updated to results
        Given I navigate to the "search" page
        Then I should see the list with title "CATEGORY/TOPIC" including:
            | Data and information ( ...         |
            | Technology and Architecture ( ...            |
            | Audiences ( ... |
            | Service Types ( ...         |
            | Service catalogue ( ...         |
            | API and integration catalogue ( ...         |
            | Content Types ( ...         |
        When I search for "WeightSearchTerm"
        Then I should see the weight search test results ordered by relevance
        When I can click on the "Order by date" link
        Then I should see the list with title "DOCUMENT TYPE" including:
            | Publication ( ...         |
            | Data set ( ...            |
            | Series / Collection ( ... |
            | Methodology ( ...         |
        Then I should see the weight search test results ordered by date
        When I can click on the "Order by relevance" link
        Then I should see the list with title "DOCUMENT TYPE" including:
            | Publication ( ...         |
            | Data set ( ...            |
            | Series / Collection ( ... |
            | Methodology ( ...         |
        Then I should see the weight search test results ordered by relevance

    Scenario: Each document type label is correctly displayed in search results
        Given I navigate to the "home" page
        When I search for "Bare Minimum"
        Then I should see search results which also include:
            | Publication         | Bare Minimum Publication      |
            | Data set            | Bare Minimum Dataset          |
            | Methodology         | Bare Minimum Indicator        |
        When I navigate to the "home" page
        When I search for "series"
        When I can click on the "Search Test Series Summary" link
        Then I should see page titled "Search Weights Publication"
        When I search for "archive"
        When I can click on the "Time Archive Lorem Ipsum Dolor" link
        Then I should see page titled "Time Archive Lorem Ipsum Dolor"

    Scenario: Latest publication links for series
        Given I navigate to the "search" page
        When I click on the "Series / Collection" button
        Then I can click on the "Time Series Lorem Ipsum Dolor" link
        And I should see publication page titled "Time Series Lorem Ipsum Dolor"

    Scenario: No latest publication links for series without flag
        Given I navigate to the "search" page
        When I click on the "Series / Collection" button
        Then I should not see element with title "Latest 2019"
        When I click on the "Series without latest" link
        Then I can click on the "Latest 2019" link

    Scenario: Searchable taxonomy
        Given I navigate to the "search" page
        When I search for "TaxonomySearchTerm"
        Then I should see 1 search result
        And I can click on link "taxonomy test"

    Scenario: Same results for taxonomy and it's synonyms
        Given I navigate to the "search" page
        When I search for "SynonymSearchTerm"
        Then I should see 1 search result
        And I can click on link "synonym test"
        When I search for "SynonymTest"
        Then I should see 1 search result
        And I can click on link "synonym test"