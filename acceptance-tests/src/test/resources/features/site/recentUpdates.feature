Feature: Recent updates

    Scenario: Recent updates renders all published items
        Given I navigate to "Recent updates" page
        Then I should see page titled "Recent updates"
        And I can see the following links:
            | text                                     | href                                                                   |
            | Personal Demographics Service - FHIR API | /site/developer/api-catalogue/personal-demographics-service---fhir-api |
            | Hello World API                          | /site/developer/api-catalogue/hello-world-api                          |
