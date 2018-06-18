Feature: As a site user I should be shown a styled error page
    when an error occurs on the site.

    Scenario: Navigate to an invalid root URL
        Given I navigate to the "invalid root" page
        Then I should see the page not found error page

    Scenario: Navigate to an invalid document URL
        Given I navigate to the "invalid document" page
        Then I should see the page not found error page

    Scenario: Navigate to an invalid sub document URL
        Given I navigate to the "invalid sub document" page
        Then I should see the page not found error page
