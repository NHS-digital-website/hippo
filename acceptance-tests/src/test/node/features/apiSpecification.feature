Feature: API Specifications from Apigee

  Scenario: API Specification is rendered in Site
    Given I navigate to "Static API Specification" page
    Then I should see api page titled "Hello World API - NHS Digital"
    And I can see document title with content "Hello World API"
    And I should see page with text "API status and roadmap"