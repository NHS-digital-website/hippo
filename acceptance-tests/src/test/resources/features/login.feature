Feature: Login

  As an author I need to be able to log in to the system
  so that I can create a publication

  Scenario: Successful login
    Given I am on login page
    When I submit my valid admin credentials
    Then a dashboard is displayed

    Scenario: Unsuccessful login
      Given I am on login page
      When I submit my invalid credentials
      Then an error is displayed
      And I stay on the login page
