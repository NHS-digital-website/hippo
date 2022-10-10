@macro
Feature: Ensure Filter Menu macro is working as expected

    Scenario: Filter Menu content
        Given I navigate to the "Cyber Alert" page
        Then I should see "resultsNum" results when search text is "searchText":
            | 0 | nhs |
            | 1 | cry |

    Scenario: Filter Menu content
        Given I navigate to the "Cyber Alert" page
        Then I should see "resultsNum" results when click on filter "filterType":
            | 1 | High              | severity    |
            | 1 | 2019              | year        |
            | 1 | Insecure software | threat type |

    @snapshot
    Scenario: Check Filter Menu are visually correct
        Given I navigate to the "filterMenu" macro test page
        Then I should see the following "filterMenu" variants look visually correct:
            | feedhub filter menu     |
            | cyber alert filter menu |
        When I view page on "mobile"
        Then I should see the following "filterMenu" variants look visually correct:
            | feedhub filter menu     |
            | cyber alert filter menu |