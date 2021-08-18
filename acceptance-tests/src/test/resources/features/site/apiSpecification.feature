Feature: API Specifications from Apigee

  Scenario: API Specification is rendered in Site
    Given I navigate to "Static API Specification" page
    Then I should see page titled "Retired API"
    And I can see labelled element "document.title" with content "Retired API"
    And I should see page with text  "static specification text in embedded html"

  Scenario: API Specification is available through Search
    Given I navigate to the "home" page
    When I search for "Retired API"
    And I click on link "Retired API"
    Then I should see page titled "Retired API"