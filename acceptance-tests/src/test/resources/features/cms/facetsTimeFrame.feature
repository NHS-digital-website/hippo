# Disabling this as it doesn't work with time zones. Have raised a ticket with BR
# https://issues.onehippo.com/browse/NHSDIGITAL-108

#Feature: Faceted Time Frame search
#
#    As a content consumer I need to be able to filter search results so I can easily
#    navigate to the documents I'm interested in.
#
#    # Note these are in the CMS folder because they need to create publications with
#    # a date relative to today and they do so via the CMS.
#
#    @DeleteAfter
#    Scenario: Relative date - today
#        Given I have a published publication with nominal date falling before 0 days from now
#        When I navigate to the "home" page
#        And I search for the publication
#        Then I should see the list with title "TIMEFRAME" containing:
#            | today (1)      |
#            | this week (1)  |
#            | this month (1) |
#            | this year (1)  |
#
#
#    @DeleteAfter
#    Scenario: Relative date - yesterday
#        Given I have a published publication with nominal date falling before -1 days from now
#        When I navigate to the "home" page
#        And I search for the publication
#        # Just check it includes 'yesterday' as we don't know if yesterday is this week/month/year or not
#        Then I should see the list with title "TIMEFRAME" including:
#            | yesterday (1) |
#        And I should see the list with title "TIMEFRAME" not including:
#            | today ... |
#
#
#    @DeleteAfter
#    Scenario: Relative date - last year
#        Given I have a published publication with nominal date falling before -1 years from now
#        When I navigate to the "home" page
#        And I search for the publication
#        Then I should see the list with title "TIMEFRAME" containing:
#            | before this year (1) |
#
#
#    @DeleteAfter
#    Scenario: Relative date - this week
#        Given I have a published publication with nominal date falling this week
#        When I navigate to the "home" page
#        And I search for the publication
#        # Don't know if it's this month or year but it's not today or yesterday
#        Then I should see the list with title "TIMEFRAME" including:
#            | this week (1) |
#        And I should see the list with title "TIMEFRAME" not including:
#            | today ...     |
#            | yesterday ... |
#
#
#    @DeleteAfter
#    Scenario: Relative date - this month
#        Given I have a published publication with nominal date falling this month
#        When I navigate to the "home" page
#        And I search for the publication
#        Then I should see the list with title "TIMEFRAME" containing:
#            | this month (1) |
#            | this year (1)  |
#
#
#    @DeleteAfter
#    Scenario: Relative date - this year
#        Given I have a published publication with nominal date falling this year
#        When I navigate to the "home" page
#        And I search for the publication
#        Then I should see the list with title "TIMEFRAME" containing:
#            | this year (1)  |
