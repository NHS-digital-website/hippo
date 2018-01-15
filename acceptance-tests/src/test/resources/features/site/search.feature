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
        When I can click on the "Last" link
        Then I can click on the "First" link
        # Check for "Last" link intentionally followed by click on "First" link
        # since 'last' page number could increase if more lorem test documents added.

    Scenario: Search partial match (publication, dataset, series)
        Given I navigate to the "home" page
        When I search for "titleTestApric"
        Then I should see 1 search results
        And I can click on link "Publication fields titleTestApricots"
        When I search for "titleTestPota"
        Then I should see 1 search results
        And I can click on link "dataset fields titleTestPotatoes"
        When I search for "titleTestStrawb"
        Then I should see 1 search results
        And I can click on link "series fields titleTestStrawberries"

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

    Scenario: Pdf text is not matched with the search term
        Given I navigate to the "home" page
        And I search for "_Attachment_Search_Term_"
        Then I should see 0 search results

    Scenario: Search boosts are applied correctly to the different fields
        Given I navigate to the "home" page
        And I search for "WeightSearchTerm"
            # Search results should go Series -> Publication -> Dataset
            # within those groups the order should be Title -> Summary (-> Key Facts)
        Then I should see search results starting with:
            | Search Test Series Title          |
            | Search Test Series Summary        |
            | Search Test Publication Title     |
            | Search Test Publication Summary   |
            | Search Test Publication Key Facts |
            | Search Test Dataset Title         |
            | Search Test Dataset Summary       |

    Scenario: Search terms are displayed correctly on the results page
        Given I navigate to the "home" page
        When I search for "test_search"
        Then I should see the search term "test_search" on the results page

    Scenario: Quoted search terms are displayed correctly on the results page
        Given I navigate to the "home" page
        When I search for ""test_search""
        Then I should see the search term ""test_search"" on the results page

    Scenario: Blank search terms display the correct results page
        Given I navigate to the "home" page
        When I search for " "
        Then I should see the blank search results page
