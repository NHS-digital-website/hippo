Feature: CMS Preview

    @DeleteAfter
    Scenario: Preview works
        Given I have a publication opened for editing
        And I populate and save the publication
        When I preview the document
        Then Title is shown
