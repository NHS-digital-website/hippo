Feature: General doctype tests

    Scenario: Check page meta data
        Given I navigate to the "General test document" page
        Then I should see page titled "General AcceptanceTestDocument - NHS Digital"
