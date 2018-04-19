
Feature: National Indicator summary page

    As a site user I should see a summary page of all the Indicator information
    so I can view each and assess the usefulness of the indicator.

    Scenario: Indicator Summary page - fields
        Given I navigate to the "sample-indicator" page
        Then I should see a page titled "People with stroke admitted to an acute stroke unit within 4 hours of arrival at hospital"
        Then I can see labelled element "assuredStatus" with content "Independently assured by Information Governance Board (IGB)"
        Then I can see labelled element "publishedBy" with content "NHS Digital"
        Then I can see labelled element "assuranceDate" with content "14 Dec 2017"
        Then I can see labelled element "reportingPeriod" with content "Annually"
        Then I can see labelled element "basedOn" with content "Royal College of Physicians’ Sentinel Stroke National Audit Programme (SSNAP)"
        Then I can see labelled element "contactAuthor" with content "Hugo"
        Then I can see labelled element "reportingLevel" with content "CCG"
        Then I can see labelled element "reviewDate" with content "14 Dec 2018"
        Then I can see labelled element "purpose" with content "Patients who have had a stroke should be admitted directly to a specialist acute stroke unit."
        Then I can see labelled element "definition" with content "The percentage of people with stroke admitted to an acute stroke unit within 4 hours of arrival to hospital"
        Then I can see labelled element "data-source" with content "Royal College of Physicians’ Sentinel Stroke National Audit Programme (RCP SSNAP)"
        Then I can see labelled element "numerator" with content "Of the denominator, the number of patients whose first ward of admission is a stroke unit AND wh"
        Then I can see labelled element "denominator" with content "All patients admitted to hospital with a primary diagnosis of stroke, exc"
        Then I can see labelled element "calculation" with content "The numerator is divided by the denominator and multiplied by 100 to provide a pe"
        Then I can see labelled element "caveats" with content "The patterns of providing care may vary between organisations in terms of hospital"
        Then I can see labelled element "interpretations" with content "A high percentage of patients with stroke admitted to an acute stroke"

    Scenario: Indicator Summary Page - taxonomy links
        Given I navigate to the "sample-indicator" page
        When I click on "Search for Cancer" button
        Then I should see the list with title "CATEGORY/TOPIC" containing:
            | Conditions ...                      |
            | Social care ...                     |
        Given I navigate to the "sample-indicator" page
        When I click on "Search for Conditions" button
        Then I should see the list with title "CATEGORY/TOPIC" including:
            | Conditions ...                      |
            | Workforce ...                       |
