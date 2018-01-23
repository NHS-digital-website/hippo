Feature: Clinical Indicator hub page and sub sections

    As a site user I should see a summary page of all the Clinical Indicator
    sections so I can navigate to each one individually to find out more.

    Scenario: Clinical Indicators hub page
        Given I navigate to the "home" page
        Then I should see a page titled "Clinical Indicators"

    Scenario: CI landing pages - CCG Outcomes (and back link)
        Given I navigate to the "home" page
        When I click on link "Clinical Commissioning Group Outcomes Indicator Set"
        Then I should see page titled "CCG Outcomes - Indicator Set"
        When I click on link "Back to Clinical Indicators"
        Then I should see a page titled "Clinical Indicators"

    Scenario: CI landing pages - Compendium
        Given I navigate to the "home" page
        When I click on link "Compendium of Population Health Indicators"
        Then I should see page titled "Compendium of Population Health Indicators"

    Scenario: CI landing pages - Social Care
        Given I navigate to the "home" page
        When I click on link "Social Care"
        Then I should see page titled "Social Care"

    Scenario: CI landing pages - NHS Outcomes Framework
        Given I navigate to the "home" page
        When I click on link "NHS Outcomes Framework"
        Then I should see page titled "NHS Outcomes Framework"

    Scenario: CI landing pages - SHMI
        Given I navigate to the "home" page
        When I click on link "Summary Hospital-level Mortality Indicator (SHMI)"
        Then I should see page titled "Summary Hospital-level Mortality Indicator (SHMI)"

    Scenario: CI landing pages - POMI
        Given I navigate to the "home" page
        When I click on link "Patient Online Management Information (POMI)"
        Then I should see page titled "Patient Online Management Information (POMI)"
