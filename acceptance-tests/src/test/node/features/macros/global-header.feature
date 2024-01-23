@macro
Feature: Ensure global header macro is working as expected

    Scenario: Text is displayed for menu options and links work
        Given I navigate to the "Cyber Alert" page
        Then I click on the link named "Develop"
        Then I should see text "Developer"

    Scenario: Text is displayed for menu options and links work for JS disabled browser
        Given I navigate to JS disabled "Cyber Alert" page
        Then I click on the link named "Develop"
        Then I should see text "Developer"

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
