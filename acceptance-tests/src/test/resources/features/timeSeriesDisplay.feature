Feature: Display of publications forming a time series

    As a content consumer
    I want to be able to see a page which lists the title of all of the available publications in a publication series
    so that I can easily identify related publications.

    Scenario: List publications from the same time series
        Given I have a number of publications belonging to the same time series
        When I navigate to the time series
        Then I can see a list of released publications from the time series
