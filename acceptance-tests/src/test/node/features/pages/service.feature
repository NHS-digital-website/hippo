Feature: Service doctype tests

    Scenario: Verify update emphasis boxes
        Given I navigate to the "Service test document" page
        When I see an emphasis box with heading "Information update"
        Then emphasis box should contain text "Information about this document"
        And emphasis box should have a "blue" border colour
