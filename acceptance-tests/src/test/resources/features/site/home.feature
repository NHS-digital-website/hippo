@home
    Feature: Ensure home page displays required fields.

    Scenario: Check individual content fields
        Given I navigate to "homeland" page
        Then I should see the home page title "Search"
        And I should also see following items in home page:
            | Top label                     | NHSS Digital provides technology, information and ...    |
            | Search                        | Search   |
            | Data and information          | Data and information   |
            | Systems and services          | Systems and services    |
            | Find data and information     | Find data and information    |
            | Indicator library             | Indicator library    |
            | data and information summary  | NHS Digital has responsibility for standardising ...    |
            | See all data and publications | See all data and publications    |
            | View Indicator Library        | View Indicator Library    |
            | Latest publications           | Latest publications    |
            | Systems and services          | Systems and services    |
            | Systems and services summary  | We are the national provider of information ...    |
            | Most popular services         | Most popular services    |
            | View all services             | View all services    |
            | Latest news                   | Latest news    |
            | View all news                 | View all news    |
            | Upcoming events               | Upcoming events    |
            | View all events               | View all events    |
            | How we look after your information            | How we look after your information    |
            | Find out how we look after your information   | Find out how we look after your information    |
            | About us                      | About us    |
