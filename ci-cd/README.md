# CI & CD

Integration & Delivery scripts.

As usual, run `make help` for help.

In order to release new distribution package run:

```
make release
```

If (as a developer) you want to upload you current branch with some unfinished
feature, run:

```
make build upload version.print
...
- - -
Current version: v0.0.1-3-g75a572f
- - -
```

Copy the version number printed on the screen and ask you friendly DevOps to
deploy this to environment of your choice.




## Environments Names

When using `ENV` parameter in `make` commands, please use the following 3 character
names.

* dev -> dev
* trg -> training
* tst -> test
* prd -> prod
* uat -> acct
