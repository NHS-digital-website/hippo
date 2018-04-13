Feature: S3 integration performance
    As a product owner, I want to simulate heavy usage of the S3 file transfers
    So that we can test if the application can cope with the load.

    # Test objective:
    # Given a number of concurrent transfers of files stored in S3 via CMS
    # When a user tries using CMS and Site
    # Then the CMS and Site are available (not necessarily quick, we just check they don't die)

    # Scenarios in this file are intended to only run in ad-hoc performance tests rather
    # than regularly, say as part of regular builds.
    #
    # Typically they would be run individually, using @WIP tag.

    # With many stable, slow, downloads, not saturating the pool -
    # does the application respond to other requests?
    Scenario: Concurrent CMS preview file downloads
        Given 20 'user-' users are logged in
        And each user has a Legacy Publication X open for CMS preview
        Then all users download file file-500MB.zip in parallel, initiated 15 seconds apart

    # With many small, quick downloads, exceeding pool capacity (set for this test to 10) -
    # does the application respond to other requests?
    Scenario: Concurrent CMS preview file downloads
        Given 55 'user-' users are logged in
        And each user has a Legacy Publication X open for CMS preview
        Then all users download file file-5MB.zip in parallel, initiated 2 seconds apart
