Feature: As am author I need to create a new publication
    so that it is visible to end users

    @DiscardAfter
    Scenario: New Publication screen
        Given I am on the content page
        When I create a new publication
        Then an edit screen is displayed which allows me to populate details of the publication

    Scenario: Saving a publication
        Given I have a publication opened for editing
        When I populate and save the publication
        Then it is saved

    @TakeOfflineAfter
    Scenario: Publishing a publication
        Given I am on the content page
        And I create a new publication
        When I populate and save the publication
        And I publish the publication
        Then it is visible to consumers
