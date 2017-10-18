Feature: Basic search

    As a content consumer I need to be able to search for publications
    that I am interested in so that I can use them

    Scenario: Clickable links
        Given I am on the site homepage
        When I search for a publication
        Then I want clickable result links which take me to the publication

    Scenario: Search term retained
        Given I am on the site homepage
        When I search for a publication
        Then the results page retains the original search term
