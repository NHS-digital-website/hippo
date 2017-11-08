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
    Scenario: Publishing a publication
        Given I have saved a publication
        When I publish the publication
        Then it is visible to consumers

    Scenario: Details are hidden from the end users in a released upcoming publication
        Given I have a released publication flagged as upcoming
        When I view the publication
        Then Title is visible
        And Nominal Publication Date field is visible
        And Disclaimer "(Upcoming, not yet published)" is displayed
        And All other publication's details are hidden

    Scenario: Nominal publication date is displayed in full when it falls before 8-week cut off
        Given I have a released publication with nominal date falling before 8 weeks from now
        When I view the publication
        Then Nominal Publication Date is displayed using format "1 Jan 2000"

    Scenario: Nominal publication date is displayed in part when it falls after 8-week cut off
        Given I have a released publication with nominal date falling after 8 weeks from now
        When I view the publication
        Then Nominal Publication Date is displayed using format "Jan 2000"
