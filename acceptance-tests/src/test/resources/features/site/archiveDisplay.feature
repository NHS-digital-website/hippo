Feature: Display of publications forming a archive

    As a content consumer
    I want to be able to see a page which lists the title of all of the available publications in a publication archive
    so that I can easily identify related publications.

    Scenario: List publications from the same archive
        Given I navigate to the "valid publication archive" page
        Then I can see "Lorem Ipsum Dolor 2004" link
        And I can see "Lorem Ipsum Dolor 2003" link
        And I can see "Lorem Ipsum Dolor 2002" link

    Scenario: Direct navigation to archive
        Given I navigate to the "valid publication archive direct" page
        Then I can see "Lorem Ipsum Dolor 2004" link
        And I can see "Lorem Ipsum Dolor 2003" link
        And I can see "Lorem Ipsum Dolor 2002" link

    Scenario: Navigate from archive to publication and back to archive
        Given I navigate to the "valid publication archive" archive page
        When I click on link "Lorem Ipsum Dolor 2004"
        Then I should see publication page titled "Lorem Ipsum Dolor 2004"
        When I click on link "Time Archive Lorem Ipsum Dolor"
        Then I should see archive page titled "Time Archive Lorem Ipsum Dolor"

    Scenario: Display multiparagraph summary
        Given I navigate to "valid publication archive" archive page
        Then I should also see multiple "Archive Summary" with:
            | Integer suscipit pulvinar lacus non porta. Aenean ut tempus ex. Proin ... |
            | Integer suscipit pulvinar lacus non porta. Aenean ut tempus ex. Proin ... |
            | Integer suscipit pulvinar lacus non porta. Aenean ut tempus ex. Proin ... |

    Scenario: Archive label displayed for archive document type
        Given I navigate to the "valid publication archive" page
        Then I should see headers:
            | Archive   |
        And I should not see headers:
            | Publication           |
            | Data set              |
            | Series/Collection     |
