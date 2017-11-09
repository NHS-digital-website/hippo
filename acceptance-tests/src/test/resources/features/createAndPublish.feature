Feature: As am author I need to create a new publication
    so that it is visible to end users

    @DiscardAfter
    Scenario: New Publication screen
        Given I am on the content page
        When I create a new publication
        Then an edit screen is displayed which allows me to populate details of the publication

    Scenario: Saving a publication
        Given I am on the edit screen
        When I populate and save the publication
        Then it is saved

    @NeedsExistingTestData
    @TakeOfflineAfter
    Scenario: Publishing a publication
        Given I have saved a publication
        When I publish the publication
        Then it is visible to consumers
