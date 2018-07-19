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
            | Publication Date Range                | 10 Feb 2015 to 15 Sep 2016                                        |
            | Publication Date                      | 10 Oct 2016                                                       |

    Scenario: Check coverage date range when dates are equal
        Given I navigate to "coverage date publication" page
        Then I should see publication page titled "Coverage dates Document"
        And I should also see:
            | Publication Date Range                | Snapshot on 27 Jan 2018                                           |

    Scenario: Display Resource Links, Attachments and Datasets in one list
        Given I navigate to "publication with datasets" page
        Then I should see publication page titled "publication with datasets"
        And I should also see "Publication Data Sets" with:
            | Etiam Placerat Arcu Dataset       |
            | publication-with-datasets Dataset |
        And I should also see "Publication Resources" with:
            | attachment.pdf [pdf, size: 42.2 kB]   |
            | Related resource link             |

    Scenario: Display Related Links list
        Given I navigate to "publication with datasets" page
        Then I should see publication page titled "publication with datasets"
        And I should also see "Publication Related Links" with:
            | Test google.com link  |

    Scenario: Headers don't display for empty fields
        Given I navigate to the "bare minimum publication" page
        Then I should see page titled "Bare Minimum Publication"
        And I should not see headers:
            | Geographic Coverage:      |
            | Geographical Granularity: |
            | Date Range:               |
            | Key Facts                 |
            | Administrative Sources    |
            | Related Links             |
            | Resources                 |

    Scenario: Headers display for populated fields
        Given I navigate to the "publication with datasets" page
        Then I should see headers:
            | Publication Date:         |
            | Geographic Coverage:      |
            | Geographical Granularity: |
            | Date Range:               |
            | Summary                   |
            | Key Facts                 |
            | Administrative Sources    |
            | Data Sets                 |
            | Resources                 |
            | Related Links             |

    Scenario: Publication label displayed for publication document type
        Given I navigate to the "bare minimum publication" page
        Then I should see headers:
            | Publication |
        And I should not see headers:
            | Series / Collection |
            | Data set            |
            | Archive             |

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

    Scenario: Show attachments with and without display names
        Given I navigate to "attachment test publication" page
        Then I should see publication page titled "Attachment Test Publication"
        And I should also see "Publication Resources" with:
            | attachment.pdf [pdf, size: 7.2 kB]       |
            | Attachment with text [pdf, size: 7.2 kB] |
        And I can download following files:
            | attachment.pdf       | attachment.pdf      |
            | Attachment with text | attachment-text.pdf |

    Scenario: Dataset order on a publication
        Given I navigate to "ordered publication" page
        Then I should see "Publication Data Sets" with:
            | 1.1 Dataset  |
            | 1.9 Dataset  |
            | 1.10 Dataset |
            | 2.0 Dataset  |

    Scenario: National Statistics kite mark on National statistic information type only
        Given I navigate to the "national statistic publication" page
        Then I should also see:
            | Publication Information Types | National statistics           |
        And I can see "National Statistics" image
        When I navigate to "publication with rich content" page
        Then I should also see:
            | Publication Information Types | Experimental statistics       |
        And I should not see element with title "National statistics"

    Scenario: Geographic Coverage displays appropriate label based on selections
        Given I navigate to the "Geographic Coverage - Great Britain" page
        Then I should also see:
            | Publication Geographic Coverage | Great Britain                                  |
        When I navigate to the "Geographic Coverage - United Kingdom" page
        Then I should also see:
            | Publication Geographic Coverage | United Kingdom                                 |
        When I navigate to the "Geographic Coverage - British Isles" page
        Then I should also see:
            | Publication Geographic Coverage | British Isles                                  |
        When I navigate to the "Geographic Coverage - Other combination" page
        Then I should also see:
            | Publication Geographic Coverage | England, Northern Ireland, Republic of Ireland |
