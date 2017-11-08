Feature: Single publication display details

    As a compliance officer
    I need to ensure that only permitted details of individual publication are displayed to the end users
    So that the system does not disclose data until it is officially published


    Scenario: Details are hidden from the end users in a published upcoming publication
        Given I have a published publication flagged as upcoming
        When I view the publication
        Then Title is shown
        And Nominal Publication Date field is shown
        And Disclaimer "(Upcoming, not yet published)" is displayed
        And All other publication's details are hidden

    Scenario: Nominal publication date is displayed in full when it falls before 8-week cut off
        Given I have a published publication with nominal date falling before 8 weeks from now
        When I view the publication
        Then Nominal Publication Date is displayed using format "1 Jan 2000"

    Scenario: Nominal publication date is displayed in part when it falls after 8-week cut off
        Given I have a published publication with nominal date falling after 8 weeks from now
        When I view the publication
        Then Nominal Publication Date is displayed using format "Jan 2000"
