Feature: Person Pages

    Scenario: Display George Lucastine's Person Pages
    The page should show each corresponding biography sub-heading of a given
    biography field. All fields are given in George Lucastine's Biography.
        When I navigate to "George Lucastine" page
        Then I should see the following:
            | Professional biography text           |
            | Previous positions/experience text    |
            | Non-NHS Digital positions/awards text |
            | Additional biography text             |
            | Personal biography text               |
        And I should see headers:
            | Professional biography             |
            | Previous positions / experience    |
            | Non-NHS Digital positions / awards |
            | Additional biography               |
            | Personal biography                 |

    Scenario: Display John Doe's Person Pages
    The page should show each corresponding biography sub-heading of a given
    biography field. Not all fields are given in John Doe's Biography.
        When I navigate to "John Doe" page
        Then I should see the following:
            | Professional biography text           |
            | Previous positions/experience text    |
        And I should see headers:
            | Professional biography             |
            | Previous positions / experience    |
        But I should not see headers:
            | Non-NHS Digital positions / awards |
            | Additional biography               |
            | Personal biography                 |

    Scenario: Display Cynthia Barker's Person Pages
    The page should not show the corresponding biography sub-heading of a given
    biography field when only one field is given.
        When I navigate to "Cynthia Barker" page
        Then I should see the following:
            | Non NHS Digital positions/awards text |
        And I should not see headers:
            | Professional biography             |
            | Previous positions / experience    |
            | Non-NHS Digital positions / awards |
            | Additional biography               |
            | Personal biography                 |
