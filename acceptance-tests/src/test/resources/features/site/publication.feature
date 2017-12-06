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
        | Publication Date Range | 01/11/2017 to 01/02/2018       |
        | Publication Taxonomy   | People, patients and geography |

Scenario: Display multiparagraph summary
    Given I navigate to "publication with datasets" page
    Then I should also see multiple "Publication Summary" with:
        | Lorem ipsum dolor sit amet, consectetur adipiscing elit ...   |
        | Nunc at quam sed tellus ultricies sagittis. Cras et sem ...   |
        | Quisque blandit viverra pulvinar. Morbi accumsan bibendum ... |

Scenario: Display multiparagraph key facts
    Given I navigate to "publication with rich content" page
    Then I should also see multiple "Publication Key Facts" with:
        # paragraphs
        | Cras eget elementum erat. Aliquam ornare urna ut ligula ...   |
        | Pellentesque gravida rhoncus nunc sed rhoncus. In ...         |
        # bullet list
        | Morbi eget congue turpis. Fusce in purus mollis ...           |
        | Aliquam erat volutpat. Quisque eget massa ...                 |
