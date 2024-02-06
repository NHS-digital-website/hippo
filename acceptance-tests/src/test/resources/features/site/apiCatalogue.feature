Feature: API Catalogue in Developer hub

    Scenario: API Catalogue renders all results when no filters applied
        Given I navigate to "Static API Catalogue" page
        Then I should see page titled "Test API catalogue"

    Scenario: API Catalogue is available through Search
        Given I navigate to the "home" page
        When I search for "Test API catalogue"
        And I click on link "Test API catalogue"
        Then I should see page titled "Test API catalogue"
