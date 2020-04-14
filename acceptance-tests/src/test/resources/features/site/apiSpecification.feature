Feature: API Specifications from Apigee

  Scenario: API Specification is rendered in Site
    Given I navigate to "Static API Specification" page
    Then I should see page titled "Test API Specification"
    And I can see labelled element "website.general.summary" with content "Test specification summary"
    And I should see article content with "static specification text in embedded html"

