@service
    Feature: Ensure service page displays required fields.

    Scenario: Check individual content fields
        Given I navigate to "service with rich content - parent" page
        Then I should see the service page title "Service Document #1"
        And I should also see following items in service page:
            | Summary                   | Collaboratively morph magnetic growth strategies without seamless e-business ...  |
            | Top Tasks                 | Seamlessly target professional initiatives ...   |
            | Body section 1            | Section One    |
            | Body section 2            | Section Two    |
            | Further information       | Service Child Document #1  |
            | Page contents             | Summary  |
