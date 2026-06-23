Feature: Checking banner configuration in pages

    Scenario: This page should not have a banner
        When I navigate to the "General test document" page
        Then I should not see banner
    
    Scenario: This page should have a banner
        When I navigate to the "Without side Navigation" page
        Then I should see banner

    Scenario: This page should  have a banner
        When I navigate to the "Without Side Navigation & Wide" page
        Then I should see banner
