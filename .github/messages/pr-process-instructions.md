## Team PR (Pull Request) Process

1. **All PRs are opened as a draft**
   - Don’t worry if you forget to set your PR as a draft; the script has our backs.

2. **All automated tests must pass**
   - Ensure the CI pipeline is green before moving forward.

3. **A developer must review and approve the code**
  - This can happen before or after the PR is marked “Ready for review”, but final approval is required.
  - The developer who approves must be in the “PR Approver” group.

4. **A tester must mark this PR as “Ready for review”**
   - This indicates that testing is complete.
   - Testing includes obtaining approval from the Product Owner (or proxy) and ensuring all requirements (including edge cases) are met.

5. **Merging the code**
   - Before merging, the developer who opened the PR must squash the commits down to a single commit.
   - The “merge” button should be pressed by the developer who opened the PR, and they must be available the following day to support the change as it goes into production.

By following these steps, we keep our process consistent and ensure high-quality merges.