Feature: General doctype tests

    Scenario: Check page meta data
        Given I navigate to the "General test document" page
        Then I should see page titled "General AcceptanceTestDocument - NHS England Digital"

    Scenario: Verify update emphasis boxes
        Given I navigate to the "General test document" page
        When I see an emphasis box with heading "Critical update"
        Then emphasis box should contain text "A critical update about this document"
        And emphasis box should have a "red" border colour
        When I see an emphasis box with heading "Important update"
        Then emphasis box should contain text "An important update about this document"
        And emphasis box should have a "yellow" border colour
