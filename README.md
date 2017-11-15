# NHS Digital Publication System - Hippo CMS

[![Build Status][build-status]][travis-project-page]

* To setup this project run `make init`.
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

This Maven project has been generated using the official [Hippo Maven project archetype] v12.0.1.

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
[build-status]:                     https://travis-ci.org/NHS-digital-website/ps-hippo.svg?branch=master
[travis-project-page]:              https://travis-ci.org/NHS-digital-website/ps-hippo
