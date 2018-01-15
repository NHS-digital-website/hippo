Feature: Searchable (hidden) flag tests.

    As a Content Editor / Author
    I want to control visibility of publication and it's dataset documents in
    search results by setting "upcoming / finalised" flag on publication document.
    So that upcoming documents does not appear in search results.

    As a User
    I don't want to see "upcoming" documents listed on search results page
    So that it does not pollute the page and does not distract me.

    Scenario: Searchable Flag test
        Given I have a publication opened for editing
        And I populate the publication
        And I save the publication
        And I publish the publication
        When I navigate to "home" page
        And I search for the publication
        Then I should see publication in search results
        # now set it to "upcoming" and publish
        When I navigate to CMS
        And I open publication for editing
        And I set publication as upcoming
        And I save the publication
        And I publish the publication
        When I navigate to "home" page
        And I search for the publication
        Then I should not see publication in search results
