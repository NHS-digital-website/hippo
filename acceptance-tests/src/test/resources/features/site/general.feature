Feature: Ensure General page displays required fields.

    Scenario: Check individual content fields (General)
        Given I navigate to the "General test document" page
        Then I should see general page titled "General AcceptanceTestDocument"
        Then I should see general page summary "General Lorem ipsum dolor sit amet, consectetur ..."
