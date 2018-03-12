Feature: Full taxonomy gets populated when picking taxonomy leaves

    @DeleteAfter
    Scenario: Full taxonomy gets populated
        Given I am on the content page
        And I create a publication with taxonomy
        And I populate and save the publication
        And I publish the publication
        When I navigate to the "home" page
        And I search for the publication
        Then I can see the full taxonomy in the faceted navigation
        When I search for a publication taxonomy term
        Then I can click on the publication
