Feature: Display of publications forming a series

    As a content consumer
    I want to be able to see a page which lists the title of all of the available publications in a publication series
    so that I can easily identify related publications.

    Scenario: List publications from the same series
        Given I navigate to the "valid publication series" page
        Then I can see "Lorem Ipsum Dolor 2014" link
        And I can see "Lorem Ipsum Dolor 2013" link
        And I can see "Lorem Ipsum Dolor 2012" link

    Scenario: Navigate from series to publication and back to series
        Given I navigate to the "valid publication series" series page
        When I click on link "Lorem Ipsum Dolor 2014"
        Then I should see publication page titled "Lorem Ipsum Dolor 2014"
        When I click on link "Time Series Lorem Ipsum Dolor"
        Then I should see series page titled "Time Series Lorem Ipsum Dolor"

    Scenario: Display multiparagraph summary
        Given I navigate to "series with publication with datasets" series page
        Then I should also see multiple "Series Summary" with:
            | Integer suscipit pulvinar lacus non porta. Aenean ut tempus ex. Proin ... |
            | Pellentesque rutrum neque at felis cursus, scelerisque faucibus lacus ... |
            | Fusce sapien quam, dictum eget commodo vel, finibus condimentum ...       |

    Scenario: Series/Collection label displayed for series document type
        Given I navigate to the "valid publication series" page
        Then I should see headers:
            | Series/Collection     |
        And I should not see headers:
            | Publication           |
            | Dataset               |
