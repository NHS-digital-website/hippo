# What If I want to reset/import accounts and permissions

This document explain how to re-import users and groups configuration.

> **Note:** Please note that this will remove all users and groups configuration
> and replace it with new users and groups.

First you need to create 3 text files, each containing users for "editor", "author"
and "admin" role. The format of these files needs to be "Full name, email@address"

```
Wojtek Oledzki, wojtek.oledzki@something.something.foo
```

Now, you can generate `users.yaml` and `groups.yaml` config files:

* Generate `users.yaml` by running
  `ci-cd/bin/hippo/users-and-groups/users-yaml hippo-authors hippo-editors hippo-admins > users.yml`
* Generate `groups.yaml` by running
  `ci-cd/bin/hippo/users-and-groups/groups-yaml hippo-authors hippo-editors hippo-admins > groups.yml`

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

Each user will get random password which you can export from the `users.yaml` file
using this command:

```
grep "#" users.yml \
  | grep "@"
  | awk '{ print "New account " $3 " for " $2" was created with password: " $4}'
```
