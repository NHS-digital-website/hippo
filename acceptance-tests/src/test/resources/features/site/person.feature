Feature: Person Pages

    Scenario: Display George Lucastine's Person Pages
    The page should show each corresponding biography sub-heading of a given
    biography field. All fields are given in George Lucastine's Biography.
        When I navigate to "George Lucastine" page
        Then I should see the following:
            | Professional biography text           |
        And I should see headers:
            | Responsibilities                   |

    Scenario: Display John Doe's Person Pages
    The page should show each corresponding biography sub-heading of a given
    biography field. Not all fields are given in John Doe's Biography.
        When I navigate to "John Doe" page
        Then I should see the following:
            | Professional biography text           |
        And I should see headers:
            | Responsibilities                   |
        But I should not see headers:
            | Biography                    |
            | Awards                             |

    Scenario: Display Cynthia Barker's Person Pages
    The page should not show the corresponding biography sub-heading of a given
    biography field when only one field is given.
        Then I should not see headers:
            | Responsibilities                   |
