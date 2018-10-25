Feature: Ensure Links list page displays required fields.

    Scenario: Check individual content fields (Links List)
        Given I navigate to the "Links list test document" page
        Then I should see links list page titled "Links list AcceptanceTestDocument"
        And I should also see:
            | Links List Summary           | Links list Lorem ipsum dolor sit amet, consectetur ... |
