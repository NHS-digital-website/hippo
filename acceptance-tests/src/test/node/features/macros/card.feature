@macro
Feature: Ensure card macro is visually correct

    @snapshot
    Scenario: Check cards are visually correct
        Given I navigate to the "card" macro test page
        Then I should see the following "card" macros look visually correct:
            | basic |
            | button |
            | image |
            | author |
        When I view page on "mobile"
        Then I should see the following "card" macros look visually correct:
            | basic |
            | button |
            | image |
            | author |

    Scenario: Check cards have correct titles
        Given I navigate to the "card" macro test page
        Then I should see "card" macros with following heading text:
            | basic | Basic card |
            | button | Button card |
            | image | Image card |
            | author | Author card |
