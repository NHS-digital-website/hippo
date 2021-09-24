Feature: Ensure Blog page displays required fields.

    Scenario: Check individual content fields (Blog)
        Given I navigate to the "Blog test document 1" page
        Then I should see blog page titled "Blog AcceptanceTestDocument 1"
        Then I should see blog page summary "Browse our blog page to see what is happening"
        And I should also see:
            | Date of Publication    | 18 September 2020 |
            | Taxonomy Tags          | Falls, Breaks and fractures |
            | Categories             | Content, How we work |
            | Lead Paragraph         | Test lead paragraph caption details here...It has ... |
            | Backstory              | Backstory lorem details here. Lorem ipsum ... |
            | Contact Us             | Here are my contact details over a couple of lines ... |

    Scenario: Check latest blogs in the same folder are displayed
        Given I navigate to the "Blog test document 2" page
        When I can click on the "Blog AcceptanceTestDocument 1" link
        Then I should see blog page titled "Blog AcceptanceTestDocument 1"
        Given I navigate to the "Blog test document 1" page
        When I can click on the "Blog AcceptanceTestDocument 2" link
        Then I should see blog page titled "Blog AcceptanceTestDocument 2"

    Scenario: Check latest blogs located in sub folder to the document are displayed
        Given I navigate to the "Blog test document 2" page
        When I can click on the "Blog AcceptanceTestDocument 3" link
        Then I should see blog page titled "Blog AcceptanceTestDocument 3"
