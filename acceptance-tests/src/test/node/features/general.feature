Feature: Ensure General page displays required fields.

    @chrome @firefox @all
    Scenario: Check individual content fields (General)
        Given I navigate to the "General test document" page
        Then I should see page titled "General AcceptanceTestDocument - NHS Digital"
