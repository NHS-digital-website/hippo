Feature: Ensure Hub page displays required fields.

    Scenario: Check individual content fields (Hub)
        Given I navigate to the "Hub test document" page
        Then I should see hub page titled "Hub AcceptanceTestDocument"
        And I should also see:
            | Hub Summary           | Hub Lorem ipsum dolor sit amet, consectetur ... |

    Scenario: Check individual content fields (Visual Hub)
        Given I navigate to the "Visual Hub test document" page
        Then I should see hub page titled "VisualHub AcceptanceTestDocument"
        And I should also see:
            | Visual Hub Summary           | Visual Hub Lorem ipsum dolor sit amet, ... |
