# Testing performance of S3 integration

Test exercising impact on the running application incurred by S3 transfers performed
via CMS has been implemented as a Cucumber-driven test, whose feature file(s) can be
found under `/acceptance-tests/src/test/resources/features/s3-performance`. The test
does not run automatically as part of the build, it has to be triggered manually.

At the moment of writing, the test focuses on downloads initiated from 'CMS preview'
mode. See the actual scenarios and [S3 performance test Confluence page] for more details and
assumptions made.

The test simulates a number of users concurrently logging in, each opening their
own, pre-defined test document in 'CMS preview' and attempting to download a file
'attached' to the document, where the file itself is stored in S3.

The entire test process comprises the following phases:
1. Creation of test data (documents) and test config (users in group 'author'),
1. Loading of the documents into the tested environment,
1. Running Cucumber scenarios that trigger multiple, concurrent S3 file transfers,
1. Manually interacting with CMS and Site (performing typical navigate/search/edit
   documents activities), looking for any noticeable slow-downs coming from the multiple
   transfers going in the background,
1. Reviewing the number of failed automated downloads/interactions,
1. Potentially tweaking scenarios and/or [S3 configuration][S3 Confluence page] and re-running the
   test.


## To create test documents and users

### Ensure files are available in S3 for download
Test documents prepared for testing download expect a set of files to already be available in
S3. See template files for what files and at what locations they expect (e.g.
`legacy-publication-download.yaml.template`). If corresponding files are not there, generate and
upload them.

The following command can be used on Linux to generate a file of required size with random content
(update `count` argument and the file name with the number of megabytes needed):
```bash
dd if=/dev/urandom of=file-100MB.zip bs=1M count=100  
```
 

### Generate YAML files
Execute:
```bash
make prep.s3-perf-test-docs TEST_DOCS_COUNT=X
```


This will generate 'X' number of `*.yaml` files under
`repository-data/local/src/main/resources/hcm-content/content/documents/corporate-website/publication-system/acceptance-tests/concurrent-s3-access-test` 
making them ready to bootstrap the next time the local instance of the application
is started from scratch. The argument is optional and, if missing, defaults to 100.

Make sure to not to commit the generated test `*.yaml` files as, given their (typically large)
number, they would noticeably slow down application startup (splitting them into multiple folders
could *potentially* remedy that). 

The generated files represent:
* X 'download' users (named `user-N`) 
* X 'upload' users (named `user-upload-N`)
* X Legacy Publications prepared for testing download (named `legacy-publication-N`) 
* X Legacy Publications prepared for testing upload (named `legacy-publication-N`)
* folder containing the files (`Publication System > Acceptance Tests > Concurrent S3 Access Test`) 
* group definition assigning all test users to group 'author'
... where `N` is a value from `1` to `X`

Documents prepared for *upload* don't specify any attachments. Documents prepared for *download*
specify a set of attachments, the same set in each doc. There is one document per each test user to
enable parallel interactions (while it's possible for many users download from the same document
during preview, only one user can interact with given document in edit mode, hence the 1:1 mapping).



### Bootstrap YAML files

Build and start local instance from scratch to bootstrap all the test users and documents.

If your're planning to run the test against a non-local system, export the bootstrapped data
from your local Hippo Console as XML Export and import it to the remote system. The nodes to
be migrated this way are:
* users
* group 'author'
* folder
  `/content/documents/corporate-website/publication-system/acceptance-tests/concurrent-s3-access-test/`
  containing test documents 

Note that migrating entire 'users' node and entire group definition has a potential of overwriting
existing users and existing group definition in the target system - assuming it's even possible
to import a node when node with matching path already exists. This has not been tested and during
past test run the users and the group were modified manually, only having documents migrated in
bulk.


## To execute the tests

If running the tests against local instance, make sure the application is running with your
AWS credentials that allow it to access S3 bucket; if you have them configured in file `env.mk`
and you started the application with `make run` they will be applied correctly.

In performance tests you'll typically run a single scenario at a time; for this reason, make
sure to annotate chosen scenario with `@WIP` tag otherwise the following command won't find
any scenarios to run. 

To run the tests execute:
```bash
make test.s3-perf CMS_URL=<your-url-here> SITE_URL=<your-site-url-here>
```     
Where `CMS_URL` and `SITE_URL` specify URLs of CMS and Site applications and are only needed
if you run tests against aremote instance; if not provided the URLs default to localhost.  

				
		
[S3 performance test Confluence page]: https://confluence.digital.nhs.uk/display/CW/S3+integration+impact+on+CMS+performance+test
[S3 Confluence page]:                  https://confluence.digital.nhs.uk/display/CW/S3+Integration
