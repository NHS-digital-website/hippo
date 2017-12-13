Feature: As a consumer I need to be able to navigate to publication data sets
    so that I can download related files from them

Scenario: Search individual data set by name
    Given I navigate to the "home" page
    When I search for "publication with datasets Dataset"
    And I click on link "publication-with-datasets Dataset"
    Then I should see data set page titled "publication-with-datasets Dataset"
    And I should also see:
        | Dataset Summary               | Mauris ex est, dapibus in dictum ut, elementum sit amet ...       |
        | Dataset Granularity           | NHS Trusts                                                        |
        | Dataset Geographic Coverage   | England                                                           |
        | Dataset Date Range            | 01/02/2016 to 01/07/2017                                          |

Scenario: View resource links and download attachments from dataset
    Given I navigate to the "publication with datasets Dataset" data set page
    Then I should also see "Dataset Resources" with:
        | Google Resource Link          |
        | attachment.pdf; size: 7.2 kB  |
    And I can download following files:
        | attachment.pdf                |

Scenario: Find data set in publication resources
    Given I navigate to "publication with datasets" publication page
    When I click on link "publication-with-datasets Dataset"
    Then I should see data set page titled "publication-with-datasets Dataset"

Scenario: Find data set in publication that is part of series
    Given I navigate to "series with publication with datasets" series page
    When I click on link "series publication with datasets"
    Then I should see publication page titled "series publication with datasets"
    And I should also see:
        | Publication Summary           | Maecenas pharetra, magna ut pulvinar mattis, augue nisi ...       |
    When I click on link "series-publication-with-datasets Dataset"
    Then I should see data set page titled "series-publication-with-datasets Dataset"
    And I should also see:
        | Dataset Summary               | Sed viverra, odio nec eleifend sodales, ligula lectus varius ...  |
        | Dataset Granularity           | NHS Health Boards                                                 |
        | Dataset Geographic Coverage   | Great Britain                                                     |

Scenario: Click back to publication from data set
    Given I navigate to "publication with datasets Dataset" data set page
    When I click on "publication with datasets" link
    Then I should see publication page titled "publication with datasets"

Scenario: Hide dataset if publication is 'upcoming'
    Given I navigate to the "upcoming publication dataset" dataset page
    Then I should see "Page not found" error page

Scenario: Display multiparagraph summary
    Given I navigate to "publication with datasets dataset" dataset page
    Then I should also see multiple "Dataset Summary" with:
        | Mauris ex est, dapibus in dictum ut, elementum sit amet odio. Proin ...       |
        | Cras fringilla odio sit amet tellus pellentesque posuere. Etiam semper ...    |
        | Curabitur quis eros nisi. Nulla leo est, elementum non gravida suscipit ...   |
