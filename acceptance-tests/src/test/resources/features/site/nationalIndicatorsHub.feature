Feature: National Indicator hub page and sub sections

    As a site user I should see a summary page of all the National Indicator
    sections so I can navigate to each one individually to find out more.

    Scenario: National Indicators hub page
        Given I navigate to the "NIL Hub" page
        Then I should see a page titled "National Indicator Library"

    Scenario: NI landing pages - Advice from our experts
        Given I navigate to the "NIL Hub" page
        When I click on link "Find out more about these services"
        Then I should see page titled "How the Indicator Methodology Assurance Service (IMAS) can help you"

    Scenario: IMAS Help page link
        Given I navigate to the "NIL Help" page
        Then I should see section "advice" with hyperlink "Advice form"
        Then I should see section "add" with hyperlink "Summary form"
        Then I should see section "apply" with hyperlink "Application form"
        Then I should see section "apply" with hyperlink "Application guidance"

    Scenario: IMAS downloads
        Given I navigate to the "NIL Help" page
        Then I should see page titled "How the Indicator Methodology Assurance Service (IMAS) can help you"
        And I can download following files:
    | Advice form          | Request IMAS help.docx                 |
    | Summary Form         | Add your indicator to the library.docx |
    | Application form     | Application Form.docx                  |
    | Guidance form        | Application Guidance.docx              |

    Scenario: IMAS popular topic links
        Given I navigate to the "NIL Hub" page
        Then I should see section "popular-topics" with hyperlink "Accidents and emergencies"
        Then I should see section "popular-topics" with hyperlink "Alcohol"
        Then I should see section "popular-topics" with hyperlink "Autism"
        Then I should see section "popular-topics" with hyperlink "Cancer"
        Then I should see section "popular-topics" with hyperlink "Child Health"
        Then I should see section "popular-topics" with hyperlink "Diabetes"
        Then I should see section "popular-topics" with hyperlink "Mental Health"
        Then I should see section "popular-topics" with hyperlink "Mortality"
        Then I should see section "popular-topics" with hyperlink "Obesity"
        Then I should see section "popular-topics" with hyperlink "Outpatients"
        Then I should see section "popular-topics" with hyperlink "Population health"
        Then I should see section "popular-topics" with hyperlink "Pregnancy"
        Then I should see section "popular-topics" with hyperlink "Smoking"
