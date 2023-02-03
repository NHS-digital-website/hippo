@macro
Feature: Ensure dropdown macro is working as expected

    @snapshot
    Scenario: Check dropdown variants are visually correct
        Given I navigate to the "dropdown" macro test page

        When I click the button to open the "basic" dropdown variant
        Then I should see the following "dropdown" variants look visually correct:
            | basic |
        Then I click the button to close the "basic" dropdown variant

        When I click the button to open the "right aligned" dropdown variant
        Then I should see the following "dropdown" variants look visually correct:
            | right aligned |
        Then I click the button to close the "right aligned" dropdown variant

        When I search "united" in the search box for the "search" dropdown variant
        Then I should see the following "dropdown" variants look visually correct:
            | search |
        Then I click off to close the dropdown

        When I search "data" in the search box for the "multiselect" dropdown variant
        Then I should see the following "dropdown" variants look visually correct:
            | multiselect |
        Then I click off to close the dropdown

        When I search "1" in the search box for the "custom" dropdown variant
        Then I should see the following "dropdown" variants look visually correct:
            | custom |
        Then I click off to close the dropdown

        Given I view page on "mobile"

        When I click the button to open the "basic" dropdown variant
        Then I should see the following "dropdown" variants look visually correct:
            | basic |
        Then I click the button to close the "basic" dropdown variant

        When I click the button to open the "right aligned" dropdown variant
        Then I should see the following "dropdown" variants look visually correct:
            | right aligned |
        Then I click the button to close the "right aligned" dropdown variant

        When I search "united" in the search box for the "search" dropdown variant
        Then I should see the following "dropdown" variants look visually correct:
            | search |
        Then I click off to close the dropdown

        When I search "data" in the search box for the "multiselect" dropdown variant
        Then I should see the following "dropdown" variants look visually correct:
            | multiselect |
        Then I click off to close the dropdown

        When I search "1" in the search box for the "custom" dropdown variant
        Then I should see the following "dropdown" variants look visually correct:
            | custom |
        Then I click off to close the dropdown

    Scenario: Check button dropdown functionality
        Given I navigate to the "dropdown" macro test page
        When I click the button to open the "basic" dropdown variant
        And I wait 0.2 seconds
        Then the "basic" dropdown should be "open"
        And I click button with text "Leeds West CCG HQ (03CAR)"
        And I wait 0.2 seconds
        Then "Leeds West CCG HQ (03CAR)" should be the selected dropdown value
        And the "basic" dropdown should be "closed"

    Scenario: Check search dropdown functionality
        Given I navigate to the "dropdown" macro test page
        When I search "united" in the search box for the "search" dropdown variant
        And I wait 0.2 seconds
        Then the "search" dropdown should be "open"
        And I click button with text "United Kingdom"
        And I wait 0.2 seconds
        Then "United Kingdom" should be the selected dropdown value
        And the "search" dropdown should be "closed"
