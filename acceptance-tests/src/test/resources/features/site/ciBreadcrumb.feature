Feature: As a user navigating to clinical indicator content, I need logical breadcrumbs
    to help me browse around the various (often related) content.

    Scenario: Series, publication, dataset (not within ci folder) breadcrumb
        Given I navigate to the "series with publication with datasets" page
        Then I should see the list with title "Navigation" including:
            | series-with-publication-with-datasets |
        And I should see the list with title "Navigation" not including:
            | Data and information                   |
        When I navigate to the "series publication with datasets" page
        Then I should see the list with title "Navigation" including:
            | series-with-publication-with-datasets |
            | series publication with datasets      |
        And I should see the list with title "Navigation" not including:
            | Data and information                   |
        When I navigate to the "series-publication-with-datasets Dataset" page
        Then I should see the list with title "Navigation" including:
            | series-with-publication-with-datasets     |
            | series publication with datasets          |
            | series-publication-with-datasets Dataset  |
        And I should see the list with title "Navigation" not including:
            | Data and information                   |

    Scenario: Dataset (within CI folder) breadcrumb
        Given I navigate to the "dataset under CI folder" page
        Then I should see the list with title "Navigation" including:
            | Data and information           |
            | CCG Outcomes - Indicator Set  |
            | CCG series                    |
            | CCG publication               |
            | CCG dataset                   |

    Scenario: Archive (within CI folder) breadcrumb
        Given I navigate to the "ccg archive publication" page
        Then I should see the list with title "Navigation" including:
            | Data and information          |
            | CCG Outcomes - Indicator Set  |
            | CCG Archive                   |
            | CCG Archive publication       |

    Scenario: CI landing page (i.e. SHMI landing) breadcrumb
        Given I navigate to the "SHMI landing" page
        Then I should see the list with title "Navigation" including:
            | Data and information                               |
            | Summary Hospital-level Mortality Indicator (SHMI) |

    Scenario: NIL document (not within ci folder) breadcrumb
        Given I navigate to the "sample-indicator" page
        Then I should see the list with title "Navigation" including:
            | People with stroke admitted to an acute stroke unit within 4 hours of arrival at hospital     |
        And I should see the list with title "Navigation" not including:
            | Data and information   |

    Scenario: Legacy Publication (not within ci folder) breadcrumb
        Given I navigate to the "legacy publication" page
        Then I should see the list with title "Navigation" including:
            | Legacy Publication    |
        And I should see the list with title "Navigation" not including:
            | Data and information   |
