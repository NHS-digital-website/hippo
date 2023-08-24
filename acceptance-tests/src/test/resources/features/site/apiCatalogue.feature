Feature: API Catalogue in Developer hub

    Scenario: API Catalogue renders all results when no filters applied
        Given I navigate to "Static API Catalogue" page
        Then I should see page titled "Test API catalogue"
        And I can see labelled element "document.title" with content "Test API catalogue"
        And I should see the "Alphabetical navigation" list containing:
            | A | B | C | D | E | F | G | H | I | J | K | L | M | N | O | P | Q | R | S | T | U | V | W | X | Y | Z |
        And the index is rendered with entries:
            | text | href  | aria-label                                    |
            | E    | #e    | Jump to articles starting with the letter 'E' |
            | G    | #g    | Jump to articles starting with the letter 'G' |
            | H    | #h    | Jump to articles starting with the letter 'H' |
            | P    | #p    | Jump to articles starting with the letter 'P' |
        And I can see the following links:
            | text                                     | href                                                                   |
            | External Resource                        | https://google.com                                                     |
            | General document                         | /site/developer/api-catalogue/general-document-not-api-specification   |
            | Hello World API                          | /site/developer/api-catalogue/hello-world-api                          |
            | Personal Demographics Service - FHIR API | /site/developer/api-catalogue/personal-demographics-service---fhir-api |
        And I should see elements with attributes:
            | text         | class                                                                             |
            | Inpatient    | nhsd-a-tag nhsd-a-tag--bg-light-grey nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1 |
            | Demographics | nhsd-a-tag nhsd-a-tag--bg-light-grey nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1 |
            | Inpatient    | nhsd-a-tag nhsd-a-tag--bg-light-grey nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1 |
            | Outpatient   | nhsd-a-tag nhsd-a-tag--bg-light-grey nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1 |

    Scenario: API Catalogue renders filtered results when filters applied
        Given I navigate to "Static API Catalogue" page
        When I click on the label for "toggler_care-setting"
        And I click on the "Filter by Inpatient" labelled button
        Then the index is rendered with entries:
            | text | href  | aria-label                                    |
            | H    | #h    | Jump to articles starting with the letter 'H' |
            | P    | #p    | Jump to articles starting with the letter 'P' |
        And I can see the following links:
            | text                                     | href                                                                   |
            | Hello World API                          | /site/developer/api-catalogue/hello-world-api                          |
            | Personal Demographics Service - FHIR API | /site/developer/api-catalogue/personal-demographics-service---fhir-api |
        And I should see elements with attributes:
            | text         | class                                                                             |
            | Inpatient    | nhsd-a-tag nhsd-a-tag--bg-light-grey nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1 |
            | Inpatient    | nhsd-a-tag nhsd-a-tag--bg-light-grey nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1 |

    Scenario: API Catalogue renders filtered results when filters applied via taxonomy tag
        Given I navigate to "Static API Catalogue" page
        When I click on the "Filter by Inpatient" labelled tag
        Then the index is rendered with entries:
            | text | href  | aria-label                                    |
            | H    | #h    | Jump to articles starting with the letter 'H' |
            | P    | #p    | Jump to articles starting with the letter 'P' |
        And I can see the following links:
            | text                                     | href                                                                   |
            | Hello World API                          | /site/developer/api-catalogue/hello-world-api                          |
            | Personal Demographics Service - FHIR API | /site/developer/api-catalogue/personal-demographics-service---fhir-api |
        And I should see elements with attributes:
            | text         | class                                                                             |
            | Inpatient    | nhsd-a-tag nhsd-a-tag--bg-light-grey nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1 |
            | Inpatient    | nhsd-a-tag nhsd-a-tag--bg-light-grey nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1 |

    Scenario: API Catalogue is available through Search
        Given I navigate to the "home" page
        When I search for "Test API catalogue"
        And I click on link "Test API catalogue"
        Then I should see page titled "Test API catalogue"
