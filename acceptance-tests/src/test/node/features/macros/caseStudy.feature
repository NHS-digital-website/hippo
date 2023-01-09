@macro
Feature: Ensure case study macro is working as expected

    @snapshot
    Scenario: Check case study variants are visually correct
        Given I navigate to the "case study" macro test page
        Then I should see the following "case study" variants look visually correct:
            | default |
            | mirrored |
        When I view page on "mobile"
        Then I should see the following "case study" variants look visually correct:
            | default |
            | mirrored |

    Scenario: Check case study content
        Given I navigate to the "case study" macro test page
        Then I should see "case study" variants with the following "heading" text:
            | default | Default Case Study |
            | mirrored | Mirrored Case Study |
        Then I should see "case study" variants with the following "summary" text:
            | default | NHS Digital graduate scheme recruit Alicia Bailey is working in our Digital Delivery Centre... |
            | mirrored | NHS Digital graduate scheme recruit Alicia Bailey is working in our Digital Delivery Centre... |
        Then I should see "case study" variants with the following "button" text:
            | default | Data blog posts |
            | mirrored | Data blog posts |
        Then I should see "case study" variants with an image with alt text:
            | default | Case study alt text |
            | mirrored | Case study alt text |