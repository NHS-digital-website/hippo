Feature: Dataset edit screen - validation

    As an administrator I need to ensure that the CMS rejects invalid values in the dataset edit screen
    so that there is less risk of invalid data being stored in the repository


    @DiscardAfter
    Scenario: Title validation
        Given I have a dataset opened for editing
        And I populate the dataset
        When I clear the title
        And I save the dataset
        Then the save is rejected with error message containing "A mandatory string input field is empty"


    @DiscardAfter
    Scenario: Summary validation
        Given I have a dataset opened for editing
        And I populate the dataset
        When I clear the summary
        And I save the dataset
        Then the save is rejected with error message containing "A mandatory string input field is empty"


    @DiscardAfter
    Scenario: Nominal date validation
        Given I have a dataset opened for editing
        And I populate the dataset
        When I clear the nominal date
        And I save the dataset
        Then the save is rejected with error message containing "A required field is not present"


    @DiscardAfter
    Scenario: Long title validation
        Given I have a dataset opened for editing
        When I populate the title with text longer than the maximum allowed limit of 250 characters
        And I save the dataset
        Then the save is rejected with error message containing "Title must be 250 characters or less"


    @DiscardAfter
    Scenario: Forbidden file type upload rejection
        Given I have a dataset opened for editing
        When I try to upload a file of one of the forbidden types:
            | EXE |
            | MSI |
            | BAT |
            | COM |
            | CMD |
            | CSH |
            | SH  |
        Then the upload is rejected with an error message


    @DiscardAfter
    Scenario: Blank Attachment field rejection
        Given I have a dataset opened for editing
        When I add an empty upload field
        And I save the dataset
        Then the save is rejected with error message containing "There is an attachment field without an attachment."


    @DiscardAfter
    Scenario: Blank Granularity field rejection
        Given I have a dataset opened for editing
        When I add an empty Granularity field
        And I save the dataset
        Then the save is rejected with error message containing "Granularity has a 'Choose One' placeholder without a value."


    @DiscardAfter
    Scenario: Blank Resource Links field rejection
        Given I have a dataset opened for editing
        When I add an empty resource link field
        When I populate and save the dataset
        Then the save is rejected with error message containing "A mandatory string input field is empty"


    Scenario: No validation errors for a populated dataset
        Given I have a dataset opened for editing
        When I populate and save the dataset
        Then it is saved
