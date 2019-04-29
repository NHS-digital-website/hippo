Feature: Ensure content block sections display required fields.

    Scenario: Check 'Section' and 'Emphasis box' content blocks
        Given I navigate to the "General test document" page
        Then I should see:
            | Section Title                 | Section One                           |
            | Section Content               | General Section One lorem content ... |
            | Emphasis Box Heading          | Important note heading                |
            | Emphasis Box Content          | Some important note in the content ...|
            | Iconlist Title                | My Icon List ...                      |
            | Iconlist Introduction         | Some introduction text to display ... |
            | Iconlistitem Heading          | Green arrow take 1 ...                |
            | Iconlistitem Description      | A green arrow 1 description here      |
            | Gallery section Title         | Test Gallery Section heading          |
            | Gallery section Description   | Some description lorem ipsum          |
