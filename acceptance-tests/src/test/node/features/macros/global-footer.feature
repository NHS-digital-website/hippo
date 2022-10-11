@macro
Feature: Ensure global footer macro is working as expected

    Scenario: Check footer content
        Given I navigate to the "footer" macro test page
        Then I should see "footer" variants with the following "footer text section" text:
            | default | Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua... |
        Then I should see "footer" variants with the following links:
            | default | NHS Digital | http://digital.nhs.uk |
            | default | About Us | http://digital.nhs.uk/about-nhsd-digital |
            | default | Google | http://www.google.com/ |
            | default | Back to top | # |

    @snapshot
    Scenario: Check global footer is visually correct
        Given I navigate to the "footer" macro test page
        Then I should see the following "footer" variants look visually correct:
            | default |
        When I view page on "mobile"
        Then I should see the following "footer" variants look visually correct:
            | default |
