Feature: As am author I need to create a new publication
    so that it is visible to end users

    @DiscardAfter
    Scenario: New Publication screen
        Given I am on the content page
        When I create a fully populated publication
        Then an edit screen is displayed which allows me to populate details of the publication

    @DeleteAfter
    Scenario: Saving a publication
        Given I have a publication opened for editing
        When I populate and save the publication
        Then it is saved

    @DeleteAfter
    Scenario: Publishing a publication
        Given I am on the content page
        And I create a fully populated publication
        When I populate and save the publication
        And I publish the publication
        Then it is visible to consumers

    Scenario: Restricted document types in publication system folders
        Given I am on the content page
        When I click the "Add new document..." menu option on the "Corporate Website/Publication System" folder
        Then I should see the document options:
            | Archive             |
            | Series / Collection |
            | Data set            |
            | Publication         |
            | Legacy Publication  |

    Scenario: Restricted document types in new publication system folders
        Given I am on the content page
        When I create a new folder
        And I click the "Add new document..." menu option on the folder
        Then I should see the document options:
            | Archive             |
            | Series / Collection |
            | Data set            |
            | Publication         |
            | Legacy Publication  |

    Scenario: Restricted document types in ci hub folder
        Given I am logged in as admin on the content page
        Then I shouldn't have a "Add new folder..." menu option on the "Corporate Website/Publication System/CI hub" folder
        When I click the "Add new CI hub document..." menu option on the "Corporate Website/Publication System/CI hub" folder
        Then I should see the document options:
            | cihub     |
            | cilanding |

    Scenario: Restricted document type in about folders
        Given I am logged in as admin on the content page
        Then I shouldn't have a "Add new folder..." menu option on the "Corporate Website/About" folder
        When I click the "Add new about document..." menu option on the "Corporate Website/About" folder
        # No document options as you should automatically be creating an about type
        Then I should see no document options
