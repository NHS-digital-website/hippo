@macro
Feature: Ensure banner macro is working as expected

    Scenario: Check banner content
        Given I navigate to the "banner" macro test page
        Then I should see "banner" variants with the following "heading" text:
            | image banner | Image banner |
            | image banner mirrored | Image banner mirrored |
            | video banner | Video banner |
        Then I should see "banner" variants with the following "summary" text:
            | image banner | NHS Digital collects data from GP practices to help support... |
            | image banner mirrored | NHS Digital collects data from GP practices to help support... |
            | video banner | NHS Digital collects data from GP practices to help support... |
        Then I should see "banner" variants with the following "button" text:
            | image banner | Call to action |
            | image banner mirrored | Call to action |
            | video banner | Call to action |
        Then I should see "banner" variants with an image with alt text:
            | image banner | Image banner image |
            | image banner mirrored | Mirrored image banner image  |

    @snapshot
    Scenario: Check banners are visually correct
        Given I navigate to the "banner" macro test page
        Then I should see the following "banner" variants look visually correct:
            | image banner |
            | image banner mirrored |
            | video banner |
        When I view page on "mobile"
        Then I should see the following "banner" variants look visually correct:
            | image banner |
            | image banner mirrored |
            | video banner |

    Scenario: Check video banner video
        Given I navigate to the "banner" macro test page
        When "video banner" YouTube embed has loaded
        And I scroll target "video" element into view
        And I wait 1 second
        And I click target "video" element
        Then "video banner" YouTube embed should start playing
