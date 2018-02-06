Feature: As a content editor I need to set up URL redirects so legacy documents with
    links continue to work on the new site.

    Scenario: Redirect to shmi hub page
        When I navigate to the "shmi" page
        Then I should see page titled "Summary Hospital-level Mortality Indicator (SHMI)"
