Feature: As am author I need to create a new basic publication
    so that it is visible to end users

Scenario: New Publication screen
    Given I am on the content page
    When I create a new publication
    Then an edit screen is displayed which allows me to enter the title and summary for a publication

Scenario: Saving a publication
    Given I am on the edit screen
    When I enter title and summary and save the publication
    Then it is saved

Scenario: Publishing a publication
    Given I have saved a publication
    When I publish the publication
    Then it is visible to consumers
