Feature: Ensure Roadmapitem page displays required fields.

    Scenario: Check individual content fields (Roadmap test document)
        Given I navigate to the "Roadmap item 1" page
        Then I should see roadmapitem page titled "Roadmap Item 1"
        And I should also see:
            | Effective date       | 20 May 2018 to 31 July 2018 ... |
            | Standards       | REF123.14.09 ... |
            | Roadmapitem Summary | lorem ipsum summary content for roadmap ... |      
