@macro
Feature: Ensure code viewer macro is working as expected

    @snapshot
    Scenario: Check code viewer macro is visually correct
        Given I navigate to the "code viewer" macro test page
        Then I should see the following "code viewer" variants look visually correct:
            | basic |
            | no tabs or header |
            | syntax highlights |
        When I view page on "mobile"
        Then I should see the following "code viewer" variants look visually correct:
            | basic |
            | no tabs or header |
            | syntax highlights |

    Scenario: Check code viewer tabs are functional
        Given I navigate to the "code viewer" macro test page
        When I click "HTML" tab of "basic" code viewer variant
        Then I should see "basic" code viewer variant with code:
            | <button class="nhsd-a-button" type="button"> |
        When I click "Nunjucks" tab of "basic" code viewer variant
        Then I should see "basic" code viewer variant with code:
            | {% from "nhsd/njk/macros/atoms.njk" import nhsd_a_button %} |

    Scenario: Check open example link
        Given I navigate to the "code viewer" macro test page
        And I wait for a new page to open
        When I click open example link of "basic" code viewer variant
        Then a new page should open with url, "/site/automated-test-pages/macros?macro=code-viewer"

    Scenario: Check copy code button
        Given I navigate to the "code viewer" macro test page
        When I click "HTML" tab of "basic" code viewer variant
        And I click the copy button of the "basic" code viewer variant
        Then my clipboard should contain:
            | <button class="nhsd-a-button" type="button"> |

