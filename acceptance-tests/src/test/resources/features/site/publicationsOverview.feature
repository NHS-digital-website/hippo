Feature: Publications' overview

    As an end user
    I need to have an overview of available publications
    So that I can stay up to date with the latest and upcoming publications


    Scenario: Upcoming and recent publications are listed in one place
        Given Published and upcoming publications are available in the system
        When I navigate to the "publications overview" page
        Then I can see upcoming publications
        And I can see recent publications
