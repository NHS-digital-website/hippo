Feature: Footer

    As a site user I should see the footer on every page so I can access the terms
    and conditions and other legal and contact information

#    Scenario: Terms and conditions
#        Given I navigate to the "home" page
#        When I click on the "Terms and conditions" link
#        Then I should see a page titled "Terms and Conditions"
#        And I should see the content "1              About these terms ..."
#        When I navigate to the "publication with datasets" page
#        Then I can click on the "Terms and conditions" link
#        And I should see a page titled "Terms and Conditions"
#
#    Scenario: Accessibility help
#        Given I navigate to the "home" page
#        When I click on the "Accessibility help" link
#        Then I should see a page titled "Accessibility help"
#        And I should see the content "Our site aims to comply with the World Wide Web Consortium's (W3C's) Web Accessibility Guidelines ..."
#        When I navigate to the "publication with datasets" page
#        Then I can click on the "Accessibility help" link
#        And I should see a page titled "Accessibility help"
#
#    Scenario: Privacy and cookies
#        Given I navigate to the "home" page
#        When I click on the "Privacy and cookies" link
#        Then I should see a page titled "Privacy and cookies"
#        And I should see the content "This Privacy Notice tells you what to expect when NHS Digital collects personal information ..."
#        When I navigate to the "publication with datasets" page
#        Then I can click on the "Privacy and cookies" link
#        And I should see a page titled "Privacy and cookies"

    # Tests for each of the pages on the site can see footer, and a footer link works
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
