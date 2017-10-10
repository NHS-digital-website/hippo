Feature: As an administrator I need to ensure that the system rejects disapproved file types
    so that there is less risk of using the system to spread malware

@EndWithSaveAndClose
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
