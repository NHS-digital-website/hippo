# NHS Digital Publication System - Migrator

The new, Hippo CMS based publication system requires that data from legacy systems is transferred into it and made
available from the go-live.

At the moment of writing, there are two such systems - NHS Digital [Clinical Indicators portal] and [GOSS]-based legacy
Publications System.

This module implements a [Spring Boot]-based command line application used to convert export files generated from these
legacy systems into a format understood by [EXIM] (Hippo import/export interface).

## Migration Process Overview
At a high-level, the migration process happens in two phases; one executed on local machine and one executed on the
server(s).

In the first phase (executed locally):
* The Migrator application is used to unpack the data exported from the legacey system(s) and convert
  it to JSON files in the format expected by [EXIM]. The process of conversion uses a number of 'mapping files' provided
  by NHS experts to correctly interpret and enrich values present in the original exported data.
* Using CMS and [Console] existing data (Taxonomy and the folder(s) expected to be created as part of the import) are
  taken offline (un-published) and deleted so that there are no conflicts with imported data - the process is not
  designed to support updates/patching; it requires 'clean slate' conditions.
* Custom [Updater Editor] Groovy [import script] (using [EXIM] interface) is used to read those JSON files to create
  actual folders and documents in a local CMS.
* Migration report generated in the first step, the locally imported data, and the application logs are reviewed to
  determine whether the conversion and import were successful and whether it's okay to proceed with the the import on
  the server(s).
* Assuming all went well up to this point, Using [Console], the newly imported content is exported using Hippo's 'XML
  Export' feature.

In the second phase:
* Using CMS and [Console] existing data is taken offline - just as described above for the local system.
* Using [Console] in the remote system, the content extracted locally via 'XML Export', gets imported via 'XML Import'.

Please DO see sections below for more details on executing individual steps.

## Using the Migrator application
Migrator application can be executed either via Maven plugin or as a standalone JAR file. The former method is most
useful during development as it offers the shorterst 'make a change -> see its effect' cycle.

### Via Maven plugin
To excute it as a Maven plugin, within terminal window navigate to the root directory of the project (parent directory
of this module) and execute:
```bash
mvn -f migrator/pom.xml spring-boot:run
```
This will assemble and execute the application which will display short usage info and quit. Only when options mentioned
in the info are specified, will the application actually try to perform some actions. When running the Migrator via
[Spring Boot Maven Plugin], the arguments have to be supplied as one, comma-separated string via
`-Drun.arguments=`; for example, to have the application decompress Nesstar ZIP export file and generate
Hippo-consumable files, one would execute:
```bash
mvn -f migrator/pom.xml spring-boot:run -Drun.arguments="--nesstarUnzipFrom=/tmp/MigrationPackage_2017_09_12_1706.zip,--nesstarConvert"
```

### As a JAR file
In order to run the application as a standalone JAR, first you need to generate it; the following will generate JAR file
under module's `target` directory:
```bash
mvn -f migrator/pom.xml package
```
Then you can run it as a normal Java application. To do so, from the directory where the JAR file exists, execute:
```bash
java -jar publication-system-migrator.jar
```
In this mode arguments are specified as follows:
```bash
java -jar publication-system-migrator.jar --nesstarUnzipFrom=/tmp/MigrationPackage_2017_09_12_1706.zip --nesstarConvert
```

### Example command line parameters
Individual steps of the conversion can be toggled on and off, depending on parameters provided. The intent was to
allow for execution of just selected steps during development of the Migrator in order to speed up the development
cycle.

As this resulted in a considerable number of parameters required to be defined, for convenience, the following provides
an example of a command line with a complete list of arguments required to execute all steps of the conversion in one
go:
```bash
java -jar publication-system-migrator.jar \
--nesstarUnzipFrom=/home/dev/migration/MigrationPackage_2018_01_15_1354.zip,\
--nesstarForceUnzip,\
--nesstarConvert,\
--nesstarAttachmentDownloadFolder=/home/dev/migration/nesstar-downloads,\
--nesstarCompendiumMappingFile=/home/dev/migration/Compendium Portal Migration - P code mapping v0.3.xlsx,\
--nesstarFieldMappingImportPath=/home/dev/migration/Coverage & Granularity for RPS - P code mapping v0.1.xlsx,\
--taxonomyDefinitionImportPath=/home/dev/migration/Taxonomy - CI Agreed v1.4 - with alphabetic list.xlsx,\
--taxonomyMappingImportPath=/home/dev/migration/Taxonomy for RPS - P code mapping v0.1.xlsx
```
Note that, while `nesstarAttachmentDownloadFolder` is optional, it's useful to point it to a location other than the
default one under `/tmp`, so that the downloaded files would be preserved between OS sessions, thus avoiding having to
spend time re-downloading them repeatedly.

Files included in the command line above are latest at the moment of writing; for their latest versions go to Confluence
page titled [Structure Mapping].

### Output

#### Execution parameters summary
At the end of successful execution Migrator will display a summary of actual execution parameters used.

#### JSON EXIM files
The program generates a number of JSON files, one file per item to be imported - one for each folder, publication, dataset and
so on. File names are prefixed with a zero-padded sequential number generated in the order the files were generated. The
idea is to create a file for parent folders before generating files for content to go into these folders. The
[import script] then reads the files in the same order to ensure that folders are created before the files.

While the JSON files are generated, whenever it is discovered that corresponding Dataset contains attachments,
The attachement files are downloaded and stored locally.

Once all the JSON files have been generated and attachments downloaded, all this content is then compressed into
a single ZIP file (under directory pointed to by `importPackageDir`).

#### Migration report
Upon completing the conversion, a report is generated in the format of Excel spreadsheet, logging any issues that may
require attention or an intervention of a human operator. Path to the report is displayed when the program finishes
and is pointed to by execution parameter `migrationReportFilePath`.

## Importing converted content locally
Having logged into CMS as an administrator, navigate to `Admin > Updater Editor` and click `Registry > MigratorImporterScript`
script. In parameters section, make sure that `sourceBaseFolderPath` points to where the EXIM JSON files are located.
This location is reported at the end of conversion in Migrator's console through value `hippoImportDir`.

Keep `Batch Size`  set to `1` to ensure that folder files are correctly processed; the reason is that EXIM appears to only commit
changes between individual batches and if parent and child folders are created in the same batch, the parent cannot be
found when the child is being created. Keeping batch size set to `1` ensures that changes from each import file are committed
before the next file is processed.

Click `Execute` to trigger the import. At the end examine the log presented in the [Updater Editor] and application log
file as not all exceptions bubble up to the editor's log.

## Server side activities (local and remote environments)

### Exporting via XML Export

At the moment of writing (2018-01-29) data that needs exporting from local system is Taxonomy and Clinical Indicators.
Given that only a single node can be selected in the Console, these need to be exported as two, separate ZIP files:
* Taxonomy: `/content/taxonomies/publication_taxonomy`
* Clinical Indicators: `/content/documents/corporate-website/publication-system/clinical-indicators`

### Removing existing, conflicting data prior to import
The import process has NOT been designed to cope with conflicts where documents or folders exist in the target
system with JCR paths matching those of newly imported items. Should the import be attempted where such path
conflicts occur, the behaviour and the end result is undefined.

Given that the import is planned to be a one-off activity, executed shortly before go-live in a 'blank' system
with no pre-existing data, it's not expected that such conflicts would occur in production. Having said that,
in case of a test system, it's expected that the import would take place several times. In such a scenario
it's best to remove conflicting, pre-existing data from the system prior to re-importing it (note that there
is no need to remove data that does not conflict with the imported one).

At the moment of writing (2018-01-29) such conflicting data is Taxonomy and Clinical Indicators. In order to remove
them prior to re-importing:
1. Take offline (un-publish) and then delete Taxonomy in CMS,
1. Take offline entire content of folder `Corporate Website > Publication System > Clinical Indicators` in CMS,
1. Delete node `/content/documents/corporate-website/publication-system/clinical-indicators` in Console.

Note that if the size of the data 'covered' by the node deleted in the last step, Console starts misbehaving in
that, for a while, certain nodes can stop expanding or collapsing. After some time (10-20min) Console should
start responding again. This suggests that, even though CMS shows the deleted content to be long gone, some
background processing may still be ongoing. For this reason, it's probably best to wait until Console starts
behaving again before attempting the import; it may be worth logging out and back in to ensure that the session
is fully cycled. This is to reduce a risk  of conflicts with data that may actually still be being deleted.

### Importing via XML Import
When importing ZIP files generated as described in the [Exporting via XML Export](#exporting-via-xml-export), make sure
to first import Taxonomy.

As the export files define paths relative to the parents of the nodes selected to be exported, ensure that you
imort them into corresponding parent nodes, that is execute 'XML Import' from context menu opened by right-clicking
the following nodes:
* For Taxonomy: `/content/taxonomies`
* For Clinical Indicators: `/content/documents/corporate-website/publication-system`

Selecting 'XML Import' opens a modal dialogue. Before completing it:
* Double check that it shows 'Import path' as described above,
* Tick the 'Immediate save after import' tickbox,
* Select correct file,
* Leave other options as they are.

Once you click 'Import', your web browser will start uploading the ZIP file. Watch the upload indicator; in the case of
Clinical Indicators it can take about 10 minutes to get to 100% as the file is nearly 1GB large. Once the 100% is reached
leave the web browser as it is for the next 15-20 minutes. You'll see that the UI session times out and a disclaimer
gets displayed, apologising for the service not being available. Do not close your web browser - it appears that,
despite this timeout, the import is actually ongoing in the background and terminating web browser session seems
to abort it.

As there is no explicit progress indication, the only way to verify that the process is ongoing is to periodically check
how responsive the system is. Logging into CMS or Console (in a separate browser session) and trying to expand
folders/noes is a way to do it. While the import is ongoing, the system may appear sluggish and nodes in Console may fail
to expand. Once they do start to expand and the relevant nodes/folders do show up, the import is over.

### Timings
For orientation, the following approximate timings were observed during import on the onDemand servers:
* System to become responsive after deletion of Clinical Indicators content: 20min
* Clinical Indicators file upload (close to 1GB): 10min
* Clinical Indicators import processing (Console to start responding, content to show up in CMS): 20min


[Clinical Indicators portal]:   https://indicators.hscic.gov.uk/webview/
[GOSS]:                         https://www.gossinteractive.com/content-management
[Spring Boot]:                  https://projects.spring.io/spring-boot/
[Spring Boot Maven Plugin]:     https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html
[EXIM]:                         https://onehippo-forge.github.io/content-export-import/index.html
[Updater Editor]:               https://www.onehippo.org/library/concepts/update/using-the-updater-editor.html
[Groovy]:                       http://groovy-lang.org/
[import script]:                ../repository-data/application/src/main/resources/hcm-config/configuration/update/MigratorImporterScript.groovy
[Console]:                      https://www.onehippo.org/library/concepts/content-repository/using-the-console.html
[structure mapping]:            https://confluence.digital.nhs.uk/display/CW/Structure+Mapping
