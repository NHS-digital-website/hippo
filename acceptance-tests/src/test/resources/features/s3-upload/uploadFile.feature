Feature: Load test for uploading attachments

    As a product owner, I want to simulate heavy usage of the S3 file upload
    So that we can test if the application can cope with the load.

    Scenario: Large File Upload
        Given I have a legacy publication opened for editing
        When I populate the legacy publication
        And I try to upload files of different size:
            | 1MB |
            | 1MB |
            | 1MB |
            | 1MB |
            | 1MB |
