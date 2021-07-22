Feature: API Catalogue in Developer hub

    Scenario: API Catalogue renders all results when no filters applied
        Given I navigate to "Static API Catalogue" page
        Then I should see page titled "Test API catalogue"
        And I can see labelled element "document.title" with content "Test API catalogue"
        And I should see the "Alphabetical navigation" list containing:
            | A | B | C | D | E | F | G | H | I | J | K | L | M | N | O | P | Q | R | S | T | U | V | W | X | Y | Z |
        And the index is rendered with entries:
            | text | href  | aria-label                                    |
            | A    | #a    | Jump to articles starting with the letter 'A' |
            | B    | #b    | Jump to articles starting with the letter 'B' |
            | E    | #e    | Jump to articles starting with the letter 'E' |
            | N    | #n    | Jump to articles starting with the letter 'N' |
        And I can see the following links:
            | text                             | href                                                           |
            | A Doc With Mapped Taxonomy Tags  | /site/developer/api-catalogue/doc-with-mapped-taxonomy-tags-a  |
            | B Doc With Mapped Taxonomy Tags  | /site/developer/api-catalogue/doc-with-mapped-taxonomy-tags-b  |
            | External Resource                | https://google.com                                             |
            | No Mapped Taxonomy Tags          | /site/developer/api-catalogue/doc-with-no-mapped-taxonomy-tags |
        And I should see elements with attributes:
            | text         | class                                                                             |
            | Inpatient    | nhsd-a-tag nhsd-a-tag--bg-light-grey nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1 |
            | Demographics | nhsd-a-tag nhsd-a-tag--bg-light-grey nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1 |
            | Inpatient    | nhsd-a-tag nhsd-a-tag--bg-light-grey nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1 |
            | Outpatient   | nhsd-a-tag nhsd-a-tag--bg-light-grey nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1 |

    Scenario: API Catalogue renders filtered results when filters applied
        Given I navigate to "Static API Catalogue" page
        When I click on the label for "toggler_care-setting"
        And I click on the "Filter by Inpatient" button
        Then the index is rendered with entries:
            | text | href  | aria-label                                    |
            | A    | #a    | Jump to articles starting with the letter 'A' |
            | B    | #b    | Jump to articles starting with the letter 'B' |
        And I can see the following links:
            | text                             | href                                                           |
            | A Doc With Mapped Taxonomy Tags  | /site/developer/api-catalogue/doc-with-mapped-taxonomy-tags-a  |
            | B Doc With Mapped Taxonomy Tags  | /site/developer/api-catalogue/doc-with-mapped-taxonomy-tags-b  |
        And I should see elements with attributes:
            | text         | class                                                                             |
            | Inpatient    | nhsd-a-tag nhsd-a-tag--bg-light-grey nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1 |
            | Inpatient    | nhsd-a-tag nhsd-a-tag--bg-light-grey nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1 |

    Scenario: API Catalogue is available through Search
        Given I navigate to the "home" page
        When I search for "Test API catalogue"
        And I click on link "Test API catalogue"
        Then I should see page titled "Test API catalogue"
