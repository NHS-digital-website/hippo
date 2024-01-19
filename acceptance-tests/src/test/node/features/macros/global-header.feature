@macro
Feature: Ensure global header macro is working as expected

    Scenario: NHS Digital logo is displayed and link works for JS disabled browser
        Given I navigate to JS disabled "Cyber Alert" page
        Then I should see logo and click on logo

    Scenario: NHS Digital logo is displayed and link works
        Given I navigate to the "Cyber Alert" page
        Then I should see logo and click on logo

    Scenario: Search icon is displayed and link works
        Given I navigate to the "Cyber Alert" page
        Then I should see search icon
        When I click see search icon
        Then I should see search box

    Scenario: Search icon is displayed and link works for JS disabled browser
        Given I navigate to JS disabled "Cyber Alert" page
        Then I should see search icon
        When I click see search icon
        Then I should see search box