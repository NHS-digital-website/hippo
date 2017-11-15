# What If I want to reset test accounts and permissions

In order to reset (!) users and groups settings follow these steps.

> **Note:** Please note that this will remove all users and groups configuration
> and replace it with "test" users and groups

Generate users and groups config:

* Generate `users.yaml` by running
  `ci-cd/bin/get-hippo-users-yaml ci-cd/test-users > users.yaml`
* Generate `groups.yaml` by running
  `ci-cd/bin/get-hippo-groups-yaml ci-cd/test-users > groups.yaml`

Import users and groups config to Hippo:

* Login to `cms/console`.
* On the left tree navigation select "hippo:configuration" node.
* Right click on "hippo:configuration" and select "YAML Import".
* Paste content of `users.yaml` file to text field under "Or paste your yaml dump
  here:"
* Right click on "hippo:configuration" again and again select "YAML Import".
* Paste content of `groups.yaml` file to text field under "Or paste your yaml dump
  here:"
* Click on "Write changes to repository" to persist changes.
