
Feature: National Indicator hub page and sub sections

    As a site user I should see a summary page of all the National Indicator
    sections so I can navigate to each one individually to find out more.

    Scenario: National Indicators hub page
        Given I navigate to the "nihub" page
        Then I should see a page titled "National Indicator Library"

    Scenario: NI landing pages - Advice from our experts
        Given I navigate to the "nihub" page
        When I click on link "Find out more about these services"
        Then I should see page titled "How the Indicator Methodology Assurance Service (IMAS) can help you"
