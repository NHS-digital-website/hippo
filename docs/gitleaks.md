# GitLeaks

## Baseline Information
Gitleaks has been retro-added to this codebase, after a Pen Test reviled what turned out to be false positives.

We ran `gitleaks detect` (v8.30.0) against the repository history to produce the `.gitleaksignore` baseline. Every fingerprint in that file was manually reviewed and confirmed to be either historical noise or benign sample data. The findings fall into four buckets:

1. **Bloomreach taxonomy exports** – files such as `repository-data/development/src/main/resources/hcm-content/content/documents/corporate-website/api-catalogue.yaml`, `repository-data/application/src/main/resources/hcm-content/content/taxonomies.yaml`, and the taxonomy filter mapping YAMLs only contain public slugs/UUIDs that power site navigation. They reference concepts like `interoperability_2` or placeholders such as `[REDACTED_SHARED_KEY]` but never include live secrets. The exports remain under version control so the authoring team can iterate locally.
2. **Legacy reCAPTCHA keys** – historical commits stored a hard-coded key (`6LdK9HIUAAAAAKYqxARfxX087UPwOmT2Xv_OkBZ0`) inside Freemarker templates (`repository-data/webfiles/.../eforms-default.ftl`, `.../macro/eforms.ftl`, `.../sign-up.ftl`). Current templates now resolve the key dynamically via `uk.nhs.digital.freemarker.utils.SecretsAccessor` (see `repository-data/webfiles/src/main/resources/site/freemarker/common/eforms/eforms-default.ftl:115`), and the underlying Google credentials were rotated. These entries only exist in history.
3. **Test fixtures and mocks** – various integration tests (`cms/src/test/java/uk/nhs/digital/apispecs/jobs/ApiSpecSyncFromApigeeJobIntegrationTest.java`, `.../ApiSpecSyncFromProxygenJobIntegrationTest.java`, and `cms/src/test/java/uk/nhs/digital/externalstorage/workflow/externalFilePublish/ExternalFilePublishTaskTest.java`) define fabricated credentials to drive mocked WireMock or S3 behaviour. Accompanying JSON fixtures such as `cms/src/test/resources/test-data/api-specifications/ApiSpecSyncFromApigeeJobIntegrationTest/auth-access-token-response.json` only contain dummy tokens used to assert parsing logic.
4. **Static configuration/tests** – entries like `conf/log4j2-dist.xml`, `cms/src/main/webapp/WEB-INF/hst-config.properties`, and catalogue unit tests reference literal strings (e.g. `publicationsystem:earlyaccesskey`, or `key=with=equal=signs`) that Gitleaks misidentifies as API keys. These are structural identifiers rather than authentication data.

Because none of the findings expose an active credential, `.gitleaksignore` simply records the fingerprints so the repo history can stay intact without force-pushing rewritten commits. We considered a force-push but ruled it out as would not solve the false positive issues.

## Local usage
1. Install Docker locally (GitLeaks is executed via the official container to avoid extra tooling).
2. Run `make check.gitleaks` from the repository root. The `ci-cd/Makefile` target pulls `zricethezav/gitleaks:latest`, scans the full git history so `.gitleaksignore` is honoured, and stores a SARIF report at `ci-cd/target/gitleaks-report.sarif` for inspection.
3. The command exits non-zero on any new finding. View the SARIF file locally via IntelliJ’s *SARIF Viewer* plugin (or open the JSON with `jq`) to identify the offending file/line.

## CI/CD behaviour
- The pull-request workflow now includes a "Scan for leaked secrets" step immediately after the commit message validation. It executes the same `make check.gitleaks` command, ensuring local and CI behaviour match.
- The GitHub Action fails the PR when GitLeaks detects a secret. The SARIF artefact is uploaded as a workflow artefact so authors can download the detailed report if needed (or inspect the job log which echoes the offending matches).
- `.gitleaksignore` lives at the repo root and is automatically picked up by CLI and CI runs. Only add new fingerprints after confirming that the finding is either a false positive or the credential has been revoked/rotated.

## Handling failures / updating the baseline
1. Pull the branch locally and re-run `make check.gitleaks` to reproduce the failure.
2. If the leak is real:
   - Remove the secret from git history (revoke the credential, rotate it externally, and force-push if necessary) **or** move it into a proper secret store (Vault, GitHub secrets, etc.).
   - Document the rotation with the owning team.
   - **If history already contains the secret**, follow this pattern:
     - Create a temporary branch from the offending commit (or from your feature branch).
     - Rewrite history with `git rebase -i <commit-before-leak>` (mark the bad commit as `edit`) or use `git filter-repo` when the token appears in multiple commits.
     - Remove the secret from the working tree, run `git commit --amend --no-edit`, and `git rebase --continue`.
     - Push the rewritten branch with `git push --force-with-lease origin <branch>` so the remote now contains the scrubbed commit. Coordinate with the team before force-pushing shared branches and ensure the upstream credential has been rotated/revoked.
3. If the leak is a false positive:
   - Add context explaining why it is safe (include file path + reason) to `.gitleaksignore` using the `commit:file:rule:line` fingerprint reported by GitLeaks.
   - Re-run the scan to verify the baseline still passes.
4. Commit the code changes (and `.gitleaksignore` updates if applicable) and push. The PR workflow will pass once the scanner reports zero new secrets.