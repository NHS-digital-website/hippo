Feature: Login

    As an author I need to be able to log in to the system
    so that I can create a publication

    Scenario: Successful login
        Given I am on login page
        When I submit my valid admin credentials
        Then I can open the dashboard page

    Scenario: Unsuccessful login
        Given I am on login page
        When I submit invalid credentials
        Then A login error is displayed
        And I stay on the login page

    Scenario: Password rules
        Given I am logged in as admin
        When I change my password to "admin"
        Then I can see the password error messages
