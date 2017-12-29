Feature: Series edit screen - validation

    As an administrator I need to ensure that the CMS rejects invalid values in the series edit screen
    so that there is less risk of invalid data being stored in the repository


    @DiscardAfter
    Scenario: Title validation
        Given I have a series opened for editing
        And I populate the series
        When I clear the title
        And I save the series
        Then the save is rejected with error message containing "A mandatory string input field is empty"


    @DiscardAfter
    Scenario: Summary validation
        Given I have a series opened for editing
        And I populate the series
        When I clear the summary
        And I save the series
        Then the save is rejected with error message containing "A mandatory string input field is empty"


    @DiscardAfter
    Scenario: Long title validation
        Given I have a series opened for editing
        When I populate the title with text longer than the maximum allowed limit of 250 characters
        And I save the series
        Then the save is rejected with error message containing "Title must be 250 characters or less"


    Scenario: No validation errors for a populated series
        Given I have a series opened for editing
        When I populate the series
        And I save the series
        Then it is saved
