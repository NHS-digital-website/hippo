Feature: As am author I need to create a new publication
    so that it is visible to end users

@DiscardAfter
Scenario: New Publication screen
    Given I am on the content page
    Then an edit screen is displayed which allows me to populate details of the publication

Scenario: Saving a publication
    Given I am on the edit screen
    When I populate and save the publication
    Then it is saved

@NeedsExistingTestData
Scenario: Publishing a publication
    Given I have saved a publication
    When I publish the publication
    Then it is visible to consumers

@DiscardAfter
Scenario: Title validation
    Given I am on the edit screen
    When I populate the title with text which exceeds maximum allowed limit of 250 characters long
    And I save the publication
    Then validation error message is shown and contains "Title must be 250 characters or less"

@DiscardAfter
Scenario: Summary validation
    Given I am on the edit screen
    When I populate the summary with text which exceeds maximum allowed limit of 1000 characters long
    And I save the publication
    Then validation error message is shown and contains "must be less then 1000 characters"
