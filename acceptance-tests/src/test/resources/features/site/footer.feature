Feature: Footer

    As a site user I should see the footer on every page so I can access the terms
    and conditions and other legal and contact information


    Scenario: Terms and conditions
        Given I navigate to the "home" page
        When I can click on the "Terms and Conditions" link
        Then I should see a page titled "Terms and Conditions"


    # Tests for each of the pages on the site
    Scenario: Publications Overview
        Given I navigate to the "publications overview" page
        Then I should see the footer

    Scenario: Search Results
        Given I navigate to the "home" page
        And I search for "test"
        Then I should see the footer

    Scenario: Publication
        Given I navigate to the "publication with datasets" page
        Then I should see the footer

    Scenario: Series
        Given I navigate to the "valid publication series" page
        Then I should see the footer

    Scenario: Dataset
        Given I navigate to the "publication with datasets dataset" page
        Then I should see the footer

    Scenario: Folder
        Given I navigate to the "acceptence tests folder" page
        Then I should see the footer

    Scenario: 404
        Given I navigate to the "invalid document" page
        Then I should see the footer

    Scenario: About
        Given I navigate to the "terms and conditions" page
        Then I should see the footer
