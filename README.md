# NHS Digital Website - Hippo CMS

[![Build Status][build-status]][travis-project-page]

* To setup this project run `make init`.
* To run an initial build of all modules run `mvn clean verify` (only need to do this once).
* To start local server `make serve`.
* To run all tests `make test`.
* To get more help, simply run `make help`.




## Key Maven Build Operations

This project uses [Maven] as its build system. Unless stated otherwise, instructions below assume that Maven commands
will be executed from command line in the directory of the parent module.

The [IDE] of your choice may offer alternate (sometimes more convenient) mechanisms to execute those or similar
commands, so it's worth spending some time to explore its capabilities. In particular, [IntelliJ] is able to target
[Junit] tests without involving Maven, giving a more graphical feedback on the tests' status combined with the ability
to automatically re-run the tests whenever the code changes. It also offers the option of saving commonly used commands
as [Run Configurations][IntelliJ Run Configurations] for quick and easy recall.

Note: whenever you execute an operation that involves starting up the application, make sure you don't have an instance
of it already running in the background or the server will fail to start, complaining about the port being already
used.

To run:

**Unit tests**:
```
mvn test
```

**Full suite of tests**:
```
mvn verify -Pacceptance-test-hippo,acceptance-test
```
This will execute both unit and acceptance tests. An instance of Tomcat server will be automatically spawned and
applications under test deployed into it, prior to executing automated acceptance tests. The server will automatically
stop once the tests complete. Tomcat server gets automatically downloaded and installed in a temporary project space as
part of the build so it doesn't have to already be present in the development system.

Note that, by default, the acceptance tests will be executed in a 'headless' mode, that is no browser window will be
opened on the screen. In order to see the web browser interacted with by the automated accepance tests, disable headless
mode by adding parameter `-Dheadless=false` in the command line.

[Profile][Maven profiles] `acceptance-test` activates the tests themselves, while `acceptance-test-hippo` ensures
automatic startup and shutdown of the application.

<a name="run-hippo-standalone">**Standalone Hippo instance**:</a>

```
mvn verify
mvn -Pcargo.run
```

This will run Hippo in a standalone mode, useful during development and manual tests. The first line runs unit tests and
packages the application and the second one starts it.

Once the aplication has started, the console will display message `Press Ctrl-C to stop the Cargo container...`. You can
then access Hippo CMS by navigating to `http://localhost:8080/cms` in your web browser.

You can stop the server by following the aforementioned message and hitting `Ctrl-C` in the terminal window where you
started Hippo.

**Acceptance tests against standalone Hippo instance**

In cases where you only edit code of the acceptance tests (rather than production code), usually a lot of time can
be saved by keeping an instance of the application running in the background and execute acceptance tests repeatedly,
without having to restart the application. To do so, make sure to have the application already started and running in a
standalone mode ([see instructions above](#run-hippo-standalone)) and then, to run the tests, execute:
```
mvn verify -f acceptance-tests/pom.xml -Pacceptance-test
```

See [Automated Acceptance Tests](#automated-acceptance-tests) section for more details on how to further optimise the
development cycle when working on acceptance tests.

## Auto-export

When making changes to the CMS or console data, you may want your changes to be persisted in the yaml configuration 
to be boot strapped into the application on startup. The easiest way to do this is to turn auto-export on in the CMS
(gray button on bottom left of every CMS page) or in the Console (top menu options). Make sure you turn it on before 
making any of the changes and turn it off again once you are done. This will produce new/amended yaml files. You should 
make sure the changes are in the correct module (`application` / `development`) creating a new file for new nodes where appropriate.

### YAML Formatter

All the yaml repository data files are formatted with a custom groovy script when the project is built.
This is to make the format consistent so when comparing changes to a previous version it is a lot eaiser
to see what has actually changed. UUIDs are also removed as these are generated by hippo dynamically on start up.
You can run the formatter manually by running `make format-yaml`.

## UI development

### Development

> For best UI developer experience run the local dev server in one Terminal window, and run the SASS or LESS compiler with Maven in another one (remember to 'cd' into the appropriate folder)!

#### CLI commands

**Run LESS compiler watching for local dev**

	cd repository-data/webfiles
	mvn lesscss:compile -D lesscss.watch=true
	
**Run SASS compiler watching for local dev**

	cd repository-data/webfiles
	mvn com.github.warmuuh:libsass-maven-plugin:0.2.8-libsass_3.4.4:watch

**To provision and Run the local dev server without autoexport**

	make serve.noexport

**To provision and Run the local dev server with autoexport (default)**

	make serve

#### Auto export - on/off

There are 2 ways to run the server: with or without `autoexport`. When the app runs with `autoexport` on, it exports new- and updated files triggered by CMS content updates and additions. To avoid these files accidentally being tracked by git, you better run the app without `autoexport`.
	

	
#### About the SASS compiler

> The most up to date Maven LibSass plugin is [this one](https://github.com/warmuuh/libsass-maven-plugin). The latest version as of 16/01/2018 is 0.2.8 (important to know what version is currently used in the project because it has to be specified in both the `pom.xml` file and in the command line).
 
#### Front-end library

> We're using the [NHS Digital UI library](https://github.com/nhsuk/frontend-library) as a base for our UI components and styling. The currently available release ([0.8.0](https://github.com/nhsuk/frontend-library/releases/tag/0.8.0)) is used as described in the [documentation](https://github.com/nhsuk/frontend-library#using-the-scss-directly).

## Testing local site on VMs

In order to be able to test your localhost from a VM (such as VirtualBox) you'll have to add your IPv4 address to the server configuration. You can read how it can be done [here](docs/what-if/use-custom-host.md)

## Valtech branching strategy

Valtech follows RPS's `rebase` strategy - working off a `DW-0000-master` branch, which is kept in line with `master` daily, and all feature branches are created off `DW-0000-master`. Once a feature branch is complete, we squash all commits on it into 1 meaningful commit and merge it into `DW-0000-master` - 1 at a time.

### Step 0 - Checkout `DW-0000-master` branch
```
git checkout DW-0000-master
```

### Step 1 - Getting `DW-0000-master` up to date with `master` before creating a new feature branch
```
git pull --rebase origin DW-0000-master // pull latest updates of the branch from origin
git pull --rebase origin master // to make sure it's up to date locally
git push origin DW-0000-master // if it's out of sync with origin
```

### Step 2 - Create new feature branch
```
git branch DW-0000-my-feature
git checkout DW-0000-my-feature
```

**Make changes, create new commits...**

### Step 3 - Getting feature branch up to date with `DW-0000-master` branch - update origin
```
git pull --rebase origin DW-0000-feature // pull latest updates of the branch from origin
git push origin DW-0000-feature // push new commits to origin
```

### Step 4 - Once the feature is complete, squash the commits into 1 meaningful commit

>Always run step 1 and step 3 directly before doing step 4 - to make sure our feature branch is not misaligned with it's remote self and DW-0000-master

```
git rebase -i origin/DW-0000-master
git push origin DW-0000-feature
```

### Step 5 - Once Feature is complete, squash the commits into 1 meaningful commit and make a PR to the RPS master branch

```
git checkout DW-0000-master
git pull --rebase origin DW-0000-master
git merge origin DW-0000-feature
git push origin DW-0000-master
```

## Automated Acceptance Tests

Automated acceptance tests ensure that the implementation meets functional criteria specified in the requirements.
Framework used in this project to run these tests is [Cucumber-JVM] and the tests are specified as [BDD]-style
scenarios, written in a language called [Gherkin]; see the [language's reference][Gherkin] for guidance on syntax.

See [Key Maven Build Operations](#key-maven-build-operations) for instructions how to execute the automated acceptance
tests.

**Run Only Selected Scenarios**

When you work on scenarios covering a specific feature, you don't always have to, or want to, run all the acceptance
tests in the system. In such a case, you can speed up improve your change-the-test-code/run-the-tests cycle by only
running selected Cucumber scenario(s) rather than all.

To do that:
- tag selected scenario(s) with a [custom tag][Gherkin tags], e.g. `@RUN`, placing it in the line directly preceding
  the scenario,
- modify class `AcceptanceTest`, replacing value of attribute `tags` in the by `@CucumberOptions` annotation with
  your custom tag.

Remember to remove these custom tags and the config class change before executing a final complete suite of tests and
before pushing your changes to the central repo.

**Exclude Scenarios From Execution**

Occasionally it can be useful to temporarily exclude specific scenarios from executing. To do that, tag them with `@WIP`
\- class `AcceptanceTest` configures Cucumber to ignore scenarios annotated with this tag.




## More details

This Maven project has been generated using the official [Hippo Maven project archetype] v12.1.0.

Most of the project's structure has been retained with the few custom modifications described in the sections above
but you can find more details in the original README files auto-generated by the archetype:

- [top level readme][original top-level readme]
- [`repository-data` readme][original repository-data readme]



## What if...?

If you have a development related question, it's possible that we already have a
answer in [What If section][what if]




[original top-level readme]:        HIPPO.md
[original repository-data readme]:  repository-data/README.md
[what if]:                          docs/what-if.md
[Hippo Maven project archetype]:    https://www.onehippo.org/12/trails/getting-started/creating-a-project.html
[BDD]:                              https://en.wikipedia.org/wiki/Behavior-driven_development
[IDE]:                              https://en.wikipedia.org/wiki/Integrated_development_environment
[IntelliJ]:                         https://www.jetbrains.com/idea/
[IntelliJ Run Configurations]:      https://www.jetbrains.com/help/idea/creating-and-editing-run-debug-configurations.html
[Junit]:                            http://junit.org/junit5/
[Maven]:                            https://maven.apache.org/
[Maven profiles]:                   http://maven.apache.org/guides/introduction/introduction-to-profiles.html
[Cucumber-JVM]:                     https://cucumber.io/docs#reference
[Gherkin]:                          https://cucumber.io/docs/reference
[Gherkin tags]:                     https://cucumber.io/docs/reference#tags
[build-status]:                     https://travis-ci.org/NHS-digital-website/hippo.svg?branch=master
[travis-project-page]:              https://travis-ci.org/NHS-digital-website/hippo
