# Contributors' Workflow

There is no ONE way of working with git, but we highly recommend following these
few simple steps




## Setup

Make sure you do run `make init` after you clone the repo. It will setup some handy
git commands like `git create-branch` and `git delete-gone` (and possible more in
the future).

To build this project you need access to `hippo-maven2-enterprise` maven repository.
If you already have username and password, simply open `env.mk` and add these two
lines.

```
HIPPO_MAVEN_USERNAME ?= username
HIPPO_MAVEN_PASSWORD ?= password
```




## Before You Start

Let say that you are working on a problem (ticket) `ABC-321`. First thing to do is
to create a local branch. You can use this handy shortcut

```
git fetch
git create-branch ABC-321
```

This will create new `ABC-321` branch based on `origin/master` and setup remote
branch tracking. Simple!




## Work, Work, Work

Work as usual, commit as often as you want.

![Work, work, work](https://m.popkey.co/85f465/dWeqg.gif)




## Open Pull Request

Open pull request when you done, and allow other developers to comment and request
changes in your code.

Once all questions are answered, all changes made, continue to next step.




## Prepare Final Commit

Once you're finished, you've fixed all typos and problems from your new code, it's
time to prepare nice single commit with meaningful description and subject. In
order to do so, you should rebase with the latest `origin/master` and squash all
your commits into single commit.

More information on git rebase is available here:
https://git-scm.com/book/en/v2/Git-Tools-Rewriting-History

```
git fetch
git rebase -i origin/develop
```

Once you have your single commit, make sure that the message is descriptive. This
is an example of good commit message:

```
[ABC-321] Support local config override

You can create `vagrant/config_override.yml` file to add/override any Ansible vars.
That file itself is ignored by git, so you will not break or change configuration
for anyone else.
```

This is example of a useless commit message:

```
fixed config
```

What ever you write, make sure that the future you can read it in few weeks/months
time and understand the "what and why".

A good article about commit messages: https://chris.beams.io/posts/git-commit/




## After PR is Merged

Once the PR is merged and the task branch deleted, you might want to run

```
git fetch --prune
```

This will delete local (origin) branches that no longer exist on the origin server.
