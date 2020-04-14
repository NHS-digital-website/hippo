Feature: API Specifications from Apigee

  Scenario: API Specification is rendered in Site
    Given I navigate to "Static API Specification" page
    Then I should see common content section with "static specification text in embedded html"

