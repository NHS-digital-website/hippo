Feature: Publication attachments - validation

    As an administrator I need to ensure that the CMS rejects disapproved file types
    so that there is less risk of using the system to spread malware

    As an administrator I need to ensure that the CMS prevents leaving blank/unused attachment fields in publications
    so that data in the repository is not polluted with redundant nodes


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
        Then the save is rejected with error message about blank attachment field being forbidden
