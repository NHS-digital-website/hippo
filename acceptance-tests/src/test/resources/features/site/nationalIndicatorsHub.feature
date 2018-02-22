
Feature: National Indicator hub page and sub sections

    As a site user I should see a summary page of all the National Indicator
    sections so I can navigate to each one individually to find out more.

    Scenario: National Indicators hub page
        Given I navigate to the "nihub" page
        Then I should see a page titled "National Indicator Library"

    Scenario: NI landing pages - Advice from our experts
        Given I navigate to the "nihub" page
        When I click on link "Advice from our experts"
        Then I should see page titled "Get advice for how to develop an indicator"        

    Scenario: NI landing pages - Add your indicator to the library
        Given I navigate to the "nihub" page
        When I click on link "Add your indicator to the library"
        Then I should see page titled "Add your indicator to the library"         

    Scenario: NI landing pages - Apply for independent assurance
        Given I navigate to the "nihub" page
        When I click on link "Apply for independent assurance"
        Then I should see page titled "Apply to have your indicator assured"        

    Scenario: NI landing pages - IGB
        Given I navigate to the "nihub" page
        When I click on link "Find out more about the team"
        Then I should see page titled "Indicator Governance Board (IGB)"                          