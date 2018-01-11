# What If I want to reset test accounts and permissions

In order to reset (!) users and groups settings follow these steps.

> **Note:** Please note that this will remove all users and groups configuration
> and replace it with new users and groups.

Generate users and groups config from 2 text files (testers and admins):

* Generate `users.yaml` by running
  `ci-cd/bin/get-hippo-users-yaml test-users admin-users > users.yaml`
* Generate `groups.yaml` by running
  `ci-cd/bin/get-hippo-groups-yaml test-users admin-users > groups.yaml`

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

Example `test-users` file:

```
John Smith
Muriel Williams
```

and example `admin-users` file:

```
Nelly Brown
Ronny Evans
```

Users listed in `test-users` files will get 2 accounts:

* john.smith.a (author)
* john.smith.e (editor)
* muriel.williams.a (author)
* muriel.williams.e (editor)

Users listed in `admin-users` file will get 1 account

* nelly.brown (admin)
* ronny.evans (admin)
