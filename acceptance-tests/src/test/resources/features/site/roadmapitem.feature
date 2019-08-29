Feature: Ensure Roadmapitem page displays required fields.

    Scenario: Check individual content fields (Roadmap test document)
        Given I navigate to the "Roadmap item 1" page
        Then I should see roadmapitem page titled "Roadmap Item 1"
        And I should also see:
            | Standards       | REF123.14.09 ... |
            | Roadmapitem Summary | lorem ipsum summary content for roadmap ... |
        And I should not see:
            | Effective date       | 20 May 2018 to 31 July 2018 ... |

    Scenario: Check individual content fields (Roadmap test document)
        Given I navigate to the "Roadmap item 2" page
        Then I should see roadmapitem page titled "Roadmap Item 2"
        And I should also see:
            | Effective date       | 10 August 2018 to 19 November 2018 ... |
            | Standards       | REF345.2553 ... |
            | Roadmapitem Summary | Roadmap item 2 summary - lorem ipsum summary content for roadmap ... |
