Feature: Ensure Video page displays required fields.

    Scenario: Check individual content fields (Video)
        Given I navigate to the "Video Type" page
        Then I should see video page titled "Video Type Document Test"
        And I should also see:
            | Video Summary      | Video Type Test NHS Digital leads the digital revolution ... |
            | Introduction       |This is to test video type document                           |
        And I should see "Video Link" on page
        And I should see "Block Link" on page
