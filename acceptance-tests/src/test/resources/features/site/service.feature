Feature: Ensure Service page displays required fields.

    Scenario: Check individual content fields (Service)
        Given I navigate to the "Service test document" page
        Then I should see service page titled "Service AcceptanceTestDocument"
        And I should also see:
            | Service Summary       | Service Lorem ipsum dolor sit amet, consectetur ... |
