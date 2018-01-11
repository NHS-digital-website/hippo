Feature: Footer

    As a site user I should see the footer on every page so I can access the terms
    and conditions and other legal and contact information


    Scenario: Terms and conditions
        Given I navigate to the "home" page
        When I click on the "Terms and conditions" link
        Then I should see a page titled "Terms and Conditions"
        And I should see the content "1              About these terms ..."

    Scenario: Accessibility help
        Given I navigate to the "home" page
        When I click on the "Accessibility help" link
        Then I should see a page titled "Accessibility help"
        And I should see the content "Our site aims to comply with the World Wide Web Consortium's (W3C's) Web Accessibility Guidelines ..."


    # Tests for each of the pages on the site can see footer, and a footer link works
    Scenario: Publications Overview
        Given I navigate to the "publications overview" page
        Then I should see the footer
        When I click on the "Terms and conditions" link
        Then I should see a page titled "Terms and Conditions"

    Scenario: Search Results
        Given I navigate to the "home" page
        And I search for "test"
        Then I should see the footer
        When I click on the "Terms and conditions" link
        Then I should see a page titled "Terms and Conditions"

    Scenario: Publication
        Given I navigate to the "publication with datasets" page
        Then I should see the footer
        When I click on the "Terms and conditions" link
        Then I should see a page titled "Terms and Conditions"

    Scenario: Series
        Given I navigate to the "valid publication series" page
        Then I should see the footer
        When I click on the "Terms and conditions" link
        Then I should see a page titled "Terms and Conditions"

    Scenario: Dataset
        Given I navigate to the "publication with datasets dataset" page
        Then I should see the footer
        When I click on the "Terms and conditions" link
        Then I should see a page titled "Terms and Conditions"

    Scenario: Folder
        Given I navigate to the "acceptence tests folder" page
        Then I should see the footer
        When I click on the "Terms and conditions" link
        Then I should see a page titled "Terms and Conditions"

    Scenario: 404
        Given I navigate to the "invalid document" page
        Then I should see the footer
        When I click on the "Terms and conditions" link
        Then I should see a page titled "Terms and Conditions"

    Scenario: About
        Given I navigate to the "terms and conditions" page
        Then I should see the footer
        When I click on the "Accessibility help" link
        Then I should see a page titled "Accessibility help"
