Feature: Ensure Published Work and Published Work chapter page displays required fields.

    Scenario: Check individual content fields (Published Work)
        Given I navigate to the "Published Work test document" page
        Then I should see published work page titled "Published Work AcceptanceTestDocument"
        And I should also see:
            | Published Work Summary                | Published Work Lorem ipsum dolor sit amet, consectetur ... |

    Scenario: Check individual content fields (Published Work Chapter)
        Given I navigate to the "Published Work Chapter test document" page
        Then I should see published work chapter page titled "Published Work Chapter 1"
        And I should also see:
            | Published Work Chapter Summary        | Published Work Chapter Lorem ipsum dolor sit amet, consectetur ... |
