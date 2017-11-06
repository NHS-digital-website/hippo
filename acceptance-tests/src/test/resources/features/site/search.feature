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
