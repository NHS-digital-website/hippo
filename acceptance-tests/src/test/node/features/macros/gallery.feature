@macro
Feature: Ensure gallery organism displays correctly

    Scenario: Check gallery organism image only
        Given I navigate to the "gallery" macro test page
        Then I should see "gallery" variants with an image with alt text:
            | gallery organism image only         | Abstract lights |
            | gallery organism title only         | Abstract lights |
            | gallery organism description only   | Abstract lights |
            | gallery organism download card only | Abstract lights |
        Then I should see "gallery" variants with the following "title" text:
            | gallery organism title only         | About NHS Digital                      |
            | gallery organism download card only | #AT_ONLY NHS Digital About Us AT_ONLY# |
        Then I should see "gallery" variants with the following "description" text:
            | gallery organism description only | #AT_ONLY Items can include a description if needed, like this. AT_ONLY# |
        Then I should see "gallery" variants with the following "link" text:
            | gallery organism download card only | http://localhost:8080/site/binaries/content/assets/test-resources/nhsdigitalaboutus.pdf |
        Then I should see "gallery" variants with the following "fileFormat" text:
            | gallery organism download card only | pdf |
        Then I should see "gallery" variants with the following "length" text:
            | gallery organism download card only | 818.97 bytes |

    @snapshot
    Scenario: Check gallery are visually correct
        Given I navigate to the "gallery" macro test page
        Then I should see the following "gallery" variants look visually correct:
            | gallery organism image only         |
            | gallery organism title only         |
            | gallery organism description only   |
            | gallery organism download card only |
        When I view page on "mobile"
        Then I should see the following "gallery" variants look visually correct:
            | gallery organism image only         |
            | gallery organism title only         |
            | gallery organism description only   |
            | gallery organism download card only |