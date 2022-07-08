Feature: Ensure General page displays required fields.

    Scenario: Check individual content fields (General)
        Given I navigate to the "General test document" page
        Then I should see general page titled "General AcceptanceTestDocument"
        Then I should see general page summary "General Lorem ipsum dolor sit amet, consectetur ..."

    @playwright
    Scenario: Check individual content fields (General) (playwright)
        Given I navigate to the "General test document" with playwright
        Then I should see general page titled "General AcceptanceTestDocument" with playwright
        Then I should see general page summary "General Lorem ipsum dolor sit amet, consectetur ..." with playwright