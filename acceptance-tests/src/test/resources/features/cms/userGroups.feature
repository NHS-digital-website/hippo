Feature: As a user I want the CMS content to be restricted so I'm not able to do things
    in the system that I shouldn't be allowed to

    Scenario: Only admin can see folders that aren't publication system
        When I am logged in as admin on the content page
        Then I should see the "Administration" folder
        And I should see the "Corporate Website/About" folder
        When I am logged in as ci-editor on the content page
        Then The "Administration" folder should have the menu options including:
            | No actions available |
        And I should not see the "Corporate Website/About" folder
        When I am logged in as ci-author on the content page
        Then The "Administration" folder should have the menu options including:
            | No actions available |
        And I should not see the "Corporate Website/About" folder

    Scenario: National Indicator Library authors and editors can only see the NIL folder
        When I am logged in as ni-editor on the content page
        Then The "Administration" folder should have the menu options including:
            | No actions available |
        And I should not see the "Corporate Website/About" folder
        And I should see the "Corporate Website/National Indicator Library" folder
        When I am logged in as ni-author on the content page
        Then The "Administration" folder should have the menu options including:
            | No actions available |
        And I should not see the "Corporate Website/About" folder
        And I should see the "Corporate Website/National Indicator Library" folder

    Scenario: Everyone can see publication system folder menus according to their role
        When I am logged in as admin on the content page
        Then The "Corporate Website/Publication System" folder should have the menu options including:
            | Add new document...      |
            | Add new folder...        |
            | Publish all in folder... |
            | Edit allowed content...  |
        And The "Corporate Website/Publication System/Acceptance Tests" folder should have the menu options including:
            | Add new document...      |
            | Add new folder...        |
            | Publish all in folder... |
            | Edit allowed content...  |
        When I am logged in as ci-editor on the content page
        Then The "Corporate Website/Publication System" folder should have the menu options including:
            | No actions available |
        And The "Corporate Website/Publication System/Acceptance Tests" folder should have the menu options including:
            | Add new document...      |
            | Add new folder...        |
            | Publish all in folder... |
        And The "Corporate Website/Publication System/Acceptance Tests" folder should have the menu options not including:
            | Edit allowed content...  |
        When I am logged in as ci-author on the content page
        Then The "Corporate Website/Publication System" folder should have the menu options including:
            | No actions available |
        And The "Corporate Website/Publication System/Acceptance Tests" folder should have the menu options including:
            | Add new document...      |
            | Add new folder...        |
        And The "Corporate Website/Publication System/Acceptance Tests" folder should have the menu options not including:
            | Publish all in folder... |
            | Edit allowed content...  |

    Scenario: National Indicator Library users can see NIL folder menus according to their role
        When I am logged in as ni-editor on the content page
        Then The "Corporate Website/National Indicator Library" folder should have the menu options including:
            | Add new indicator...      |
            | Add new folder...        |
            | Publish all in folder... |
        And The "Corporate Website/National Indicator Library" folder should have the menu options not including:
            | Edit allowed content...  |
        When I am logged in as ni-author on the content page
        Then The "Corporate Website/National Indicator Library" folder should have the menu options including:
            | Add new indicator...      |
            | Add new folder...        |
        And The "Corporate Website/National Indicator Library" folder should have the menu options not including:
            | Publish all in folder... |
            | Edit allowed content...  |

    Scenario: Only admin can add new folders to corporate website
        When I am logged in as admin on the content page
        Then The "Corporate Website" folder should have the menu options including:
            | Add new folder... |
        When I am logged in as ci-editor on the content page
        Then The "Corporate Website" folder should have the menu options including:
            | No actions available |
        When I am logged in as ci-author on the content page
        Then The "Corporate Website" folder should have the menu options including:
            | No actions available |

    Scenario: Author and editor can both copy documents
        Given I have a publication opened for editing
        And I populate and save the publication
        When I am logged in as ci-editor on the content page
        Then I can copy the publication
        When I am logged in as ci-author on the content page
        Then I can copy the publication
