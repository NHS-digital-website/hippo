# What if you want to release hot-fix

Hot-Fix release process can be tricky, and cause a lot of confusion.

We aim to make and keep it simple. Here are the steps to follow for a quick and
easy Hot-Fix release.




## Definiton

To keep things simple we define **hot-fix** as release that needs to be deployed
to production ASAP and takes priority over any other feature/bug fix that is
already on UAT.




## Steps

* [Create Hot-Fix Branch](#create-hot-fix-branch)
* [Apply Fix From Branch](#apply-fix-from-branch)
* [Test, Build and Upload](#test-build-and-upload)
* [Deploy Hot Fix](#deploy-hot-fix)
* [Merge Fix to Master](#merge-fix-to-master)


### Create Hot-Fix Branch

```
git fetch
git fetch --tags
git branch hot-fix prd
git checkout hot-fix
git push --no-verify --set-upstream origin hot-fix
```


### Apply Fix From Branch

If you already have a fix ready in your branch, we just need to cherry pick it.
First we need to know the full hash of our commit

```
git log origin/RPS-394 --pretty="%H %s" -n3

b9615d216263b42eccae266f1a828bb7eda12604 [RPS-394] HotFix for missing hidden fields
5397cd40e629466029e7dbb432b47b260a153fac [DW-105] Adding type field to general document type
332bd70fab999eda4c7f0061c4baf73ce4fd1cc2 [RPS-379] Allow authors to copy documents
```

now it's simple as

```
git cherry-pick b9615d216263b42eccae266f1a828bb7eda12604
```


### Test, Build and Upload

```
cd ci-cd
make clean test build ondemand.upload
```

Also, run `make version.print` to know what version you've just build and deploy,
it will be in format of `v2.0.36-1-ge52901ac`


### Deploy Hot Fix

Now you need to:

1. Let the following people know that you are about to release hot-Fix
   * Development teams
   * Business SMEs
   * Product Owners
1. Don't panic and follow these easy Steps

* Login to RunDeck - https://deploys.onehippo.com/user/login
* Make a note of the current UAT version (`v2.0.41`) - https://uat.nhsd.io/version
* Deploy your hot-fix (`v2.0.36-1-ge52901ac`) to UAT.
* Test your hot-fix on UAT.
* Deploy your hot-fix to PRD.
* Confirm hotfix working as expected on PRD.
* Deploy previous version (`v2.0.41`) to UAT.


### Merge Fix to Master

Now, when your fix is on production environment make sure to merge your branch to
master branch in a standard way (pull request).

**IMPORTANT** Make sure that development teams are aware that the next version
release to PRD MUST contain your fix. In other words, in our example, next version
that can be release is v2.0.42 (which will contain your fix).
