Feature: General doctype tests

    Scenario: Check page meta data
        Given I navigate to the "General test document" page
        Then I should see page titled "General AcceptanceTestDocument - NHS Digital"

    @snapshot
    Scenario: Verify page is visually correct
        Given I navigate to the "General test document" page
        And the page should look visually correct
        When I view page on "mobile"
        Then the page should look visually correct
