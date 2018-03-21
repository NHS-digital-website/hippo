Feature: CMS Preview

    Scenario: Preview works
        Given I am on the content page
        When I have a publication opened for editing
        And I populate and save the publication
        Then I can preview the document
