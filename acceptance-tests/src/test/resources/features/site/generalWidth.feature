Feature: Setting the width of a General page.

    Scenario: The page is set to have side navigation
        When I navigate to the "With Side Navigation" page
        Then I should see side navigation
        And the page should not be in wide mode

    Scenario: The page is set to have no side navigation
        When I navigate to the "Without Side Navigation" page
        Then I should not see side navigation
        And the page should not be in wide mode

    Scenario: The page is set to have no side navigation and to be wide
        When I navigate to the "Without Side Navigation & Wide" page
        Then I should not see side navigation
        And the page should be in wide mode
