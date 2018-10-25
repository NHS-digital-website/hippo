Feature: Ensure GDPR page displays required fields.

    Scenario: Check individual content fields (GDPR Summary)
        Given I navigate to the "GDPR Summary test document" page
        Then I should see GDPR Summary page titled "GDPR Summary AcceptanceTestDocument"
        And I should also see:
            | GDPR Summary summary      | GDPR Summary Lorem ipsum dolor sit amet, consectetur ... |

    Scenario: Check individual content fields (GDPR Transparency)
        Given I navigate to the "GDPR Transparency test document" page
        Then I should see GDPR Transparency page titled "GDPR Transparency AcceptanceTestDocument"
        And I should also see:
            | GDPR Transparency summary | GDPR Transparency Lorem ipsum dolor sit amet, consectetur ... |
