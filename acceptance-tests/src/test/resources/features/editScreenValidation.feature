Feature: Edit screen - validation

    As an administrator I need to ensure that the CMS rejects invalid values in the publication edit screen
    so that there is less risk of invalid data being stored in the repository


    @DiscardAfter
    Scenario: Title validation
        Given I am on the edit screen
        When I populate the title with text longer than the maximum allowed limit of 250 characters
        And I save the publication
        Then the save is rejected with error message containing "Title must be 250 characters or less"


    @DiscardAfter
    Scenario: Summary validation
        Given I am on the edit screen
        When I populate the summary with text longer than the maximum allowed limit of 1000 characters
        And I save the publication
        Then the save is rejected with error message containing "Summary must be 1000 characters or less"


    @DiscardAfter
    Scenario: Forbidden file type upload rejection
        Given I have a publication opened for editing
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
    Scenario: Blank attachment field rejection
        Given I have a publication opened for editing
        When I add an empty upload field
        And I save the publication
        Then the save is rejected with error message containing "There is an attachment field without an attachment."


    @DiscardAfter
    Scenario: Blank Granularity field rejection
        Given I have a publication opened for editing
        When I add an empty Granularity field
        And I save the publication
        Then the save is rejected with error message containing "Granularity has a 'Choose One' placeholder without a value."


    @DiscardAfter
    Scenario: Blank Related Links field rejection
        Given I have a publication opened for editing
        When I add an empty related link field
        And I save the publication
        Then the save is rejected with error message containing "There is a related link field with no URL provided."


    @DiscardAfter
    Scenario: Blank Information Type field rejection
        Given I have a publication opened for editing
        When I add an empty Information Type field
        And I save the publication
        Then the save is rejected with error message containing "Information Type has a 'Choose One' placeholder without a value."
