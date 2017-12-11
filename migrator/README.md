# NHS Digital Publication System - Migrator

The new, Hippo CMS based publication system requires that data from legacy systems is transferred into it and made
available from the go-live.

At the moment of writing, there are two such systems - NHS Digital [Clinical Indicators portal] and [GOSS]-based legacy
Publications System. 

This module implements a [Spring Boot]-based command line application used to convert export files generated from these
legacy systems into a format understood by [EXIM] (Hippo import/export interface).

## The process
The migration process comprises the following, high-level steps:
1) Unpack the exported data files received from the legacy systems.
1) Extract the actual data from the unpacked files and, from it, generate JSON files in the format required by [EXIM].
1) Logging in as an administrator, and using [Updater Editor], execute Groovy [import script] to import the generated
   JSON files.

The Migrator application facilitates execution of the first two steps. Groovy script used in the last step is included
in the codebase of module `repository-data/application` and automatically bootstrapped into the system.  

## Execution
Migrator application can be executed either via Maven plugin or as a standalone JAR file. The former method is most
useful during development as it offers the shorterst 'make a change -> see its effect' cycle.

### Via Maven plugin
To excute it as a Maven plugin, within terminal window navigate to the root directory of the project (parent directory
of this module) and execute:
```
mvn -f migrator/pom.xml spring-boot:run
``` 
This will assemble and execute the application which will display short usage info and quit. Only when options mentioned
in the info are specified, will the application actually try to perform some actions. When running the Migrator via
[Maven plugin](Spring Boot Maven Plugin), the arguments have to be supplied as one, comma-separated string via
`-Drun.arguments=`; for example, to have the application decompress Nesstar ZIP export file and generate
Hippo-consumable files, one would execute:
```
mvn -f migrator/pom.xml spring-boot:run -Drun.arguments="--nesstarUnzipFrom=/tmp/MigrationPackage_2017_09_12_1706.zip,--nesstarConvert"
```

### As a JAR file
In order to run the application as a standalone JAR, first you need to generate it; the following will generate JAR file
under module's `target` directory:
```
mvn -f migrator/pom.xml package
```
Then you can run it as a normal Java application. To do so, from the directory where the JAR file exists, execute:
```
java -jar publication-system-migrator.jar
```
In this mode arguments are specified as follows:
```
java -jar publication-system-migrator.jar --nesstarUnzipFrom=/tmp/MigrationPackage_2017_09_12_1706.zip --nesstarConvert
```
 
## Generating the import files
At the end of successful execution Migrator will display a summary of actual execution parameters used. One of them
will indicate where the Hippo import files were generated (by default it's `/tmp/exim-import`, unless overriden).

The program generates a number of JSON, one file per item to be imported - one for each folder, publication, dataset and
so on. File names are prefixed with a zero-padded sequential number generated in the order the files were generated. The
idea is to create a file for parent folders before generating files for content to go into these folders. The
[import script] then reads the files in the same order to ensure that folders are created before the files.

## Importing
Having logged into CMS as an administrator, navigate to `Admin > Updater Editor` and click `Registry > MigratorImporterScript`
script. In parameters section, set `sourceBaseFolderPath` to point to the directory containing the import JSON files.

Keep `Batch Size`  set to `1` to ensure that folder files are correctly processed; the reason is that EXIM appears to only commit
changes between individual batches and if parent and child folders are created in the same batch, the parent cannot be
found when the child is being created. Keeping batch size set to `1` ensures that changes from each import file are committed
before the next file is processed. 

Click `Execute` and watch the log.       
      

[Clinical Indicators portal]:   https://indicators.hscic.gov.uk/webview/
[GOSS]:                         https://www.gossinteractive.com/content-management
[Spring Boot]:                  https://projects.spring.io/spring-boot/
[Spring Boot Maven Plugin]:     https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html
[EXIM]:                         https://onehippo-forge.github.io/content-export-import/index.html
[Updater Editor]:               https://www.onehippo.org/library/concepts/update/using-the-updater-editor.html
[Groovy]:                       http://groovy-lang.org/
[import script]:                ../repository-data/application/src/main/resources/hcm-config/configuration/update/MigratorImporterScript.groovy
