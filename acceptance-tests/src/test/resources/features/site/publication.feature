Feature: Ensure publication page displays required fields.

Scenario: Check individual content fields
    Given I navigate to "publication with rich content" page
    Then I should see publication page titled "publication with rich content"
    And I should also see:
        | Publication Summary                   | Etiam vitae tincidunt lectus. Nulla posuere ultricies ...         |
        | Publication Key Facts                 | Cras eget elementum erat. Aliquam ornare urna ut ligula ...       |
        | Publication Geographic Coverage       | England                                                           |
        | Publication Granularity               | NHS Trusts                                                        |
        | Publication Administrative Sources    | Mauris pretium orci ac gravida accumsan. Cras mattis massa ...    |

Scenario: Display Resource Links, Attachments and Datasets in one list
    Given I navigate to "publication with datasets" page
    Then I should see publication page titled "publication with datasets"
    And I should also see "Publication Resources" with:
        | attachment.pdf; size: 7.2 kB      |
        | Etiam Placerat Arcu Dataset       |
        | publication-with-datasets Dataset |
        | Related resource link             |

Scenario: Display Related Links list
    Given I navigate to "publication with datasets" page
    Then I should see publication page titled "publication with datasets"
    And I should also see "Publication Related Links" with:
        | Test google.com link  |

Scenario: Display taxonomy list
    Given I navigate to "publication with datasets" page
    Then I should see publication page titled "publication with datasets"
    And I should also see:
        | Publication Taxonomy | People, patients and geography |
