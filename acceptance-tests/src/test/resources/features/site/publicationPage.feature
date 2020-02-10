Feature: Publications with pages should be displayed correctly

    Scenario: Sectioned pages display
        Given I have a sectioned publication
        When I navigate to the "sectioned publication" page
        Then I can see the publication pages
        And I should see the key fact infographics
        When I click on the "First Page For Sectioned Pub" link
        Then I can see the publication header
        And I can see the publication pages
        And I should also see:
            | Publication Page Title | First Page For Sectioned Pub |
        And I can see the first page body sections
