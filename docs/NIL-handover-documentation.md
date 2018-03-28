# National Indicator Library Handover documentation

## Introduction

- The National Indicator Library comprises thousands of health and social care indicators. Indicators are studies for quality improvement. They consist of a purpose, definition, methodology and other pertinent data. When taken together, conclusions leading to concrete steps for quality improvement can hopefully be drawn.
- Indicators can be assured by the Information Governance Board (IGB) and those that are have an assurance date and a review date, after which their assurance is considered to have lapsed. Health care professionals might wish to add their indicators to the library, in which case they must contact IMAS using the forms on the IMAS help page. They will go through a review process where changes may be made and/or they may apply for assurance from the IGB.
- The general aim of this part of the site is to provide an easy experience for two types of user. Those who know exactly what indicator(s) they're looking for and can search directly, and those who aren't sure what exactly they're looking for and wish to browse for indicators until they find something useful to them.

## Dev Setup

* `git clone git@github.com:NHS-digital-website/hippo.git`
* To setup this project run `make init`.
* To run an initial build of all modules run `mvn clean verify` (only need to do this once).
* To start local server `make serve`.
* To run all tests `make test`.
* To get more help, simply run `make help`.
* Install EditorConfig in your IDE for code formatting/styling

For more detailed instructions consult the README.

## Document type

* The main document type for the NIL-space is ***Indicator***. This contains some fields and (sub-)document types of Topbar, Details and Methodology. These are logically separated groups containing more fields which have been sectioned out for the sake of clarity.
* There are two more document types: NI hub and NI landing. NI hub is the NIL root page on the site and contains static content explaining all about the National Indicator Library and features popular topics and links to searches based on them. NI Landing is the IMAS help page which contains information about and downloads of application forms for advice, assurance, and addition of your indicator to the library.

## Rendering pipeline

### Template

* The NIL-space templates are for Indicators, the National Indicators (NI) Hub and NI landing (IMAS help page). These are populated by the fields in their respective document types. References to specific fields are a concatenation of references to the document type tree for the field in question.
* E.g. `indicator.details.caveats.content` as Caveats sits within Details which sits within Indicator. Caveats is a rich text field requiring a reference to its content.
* Each page is made up of Common-space templates such as the footer and search bar, etc.

### Components

* A document type's Bean supplies the content of its fields for the template or passes another Bean for a sub-document type. A tool is available in Hippo Essentials called the Bean Writer which should be run after every change to a document type to rewrite the relevant Bean.
* Document types also possess Components which makes the Bean available before rendering.

### Headers/Labels/Resource Bundles

* Almost all headers/labels are pulled in from documents called Resource Bundles available in the Administration/National Indicator Library section of documents in the CMS.
* Note, all headers/labels must be manually updated in PROD or UAT via the Hippo CMS

### Styling

* At time of writing, the styling comes from LESS files in webfiles. These are converted into CSS before rendering.
* The prototype for the NIL-pages is available here: https://su0fn6.axshare.com/#g=1&p=home

## Search/facets

* Searchable fields are defined in SearchBarComponent.java. Note, Lucene requires a flat structure, so can only search fields at the top-level of a document type.
* Sorting behaviour is also defined in SearchBarComponent.java
* Facets are defined in facet.yaml (this must be updated manually on deployment via the Hippo Console)
* Custom display behaviour of facets is controlled in facets.ftl

### Components

* The main component for searching is SearchComponent.java which controls all things sorting and searching. It controls which document types are surfaced in the search; which fields are sorted with higher priority than others; how the tabs for searching different areas of the site are configured, and how the query is parsed.
* Other components involved in searching are the ones behind the search bar, search results and other related templates.

### Templates

* There are lots of templates involved, chiefly `results.ftl` which contains macros for how the individual search results of various document types are rendered on the page. Others include `searchbar.ftl` and `searchresults.ftl`.

### Lucene

* `lucene/indexing_configuration.xml` is where the fields to be searched for are specified.
* Top-level fields can only be searched by Lucene (so searching for document.somecomponent.somefield will not work)

## Acceptance tests

* There are lots of automated acceptance tests that run with Travis on every pull request. Unit tests are run when spinning up the local environment.
* There are unit/feature tests for the indicator pages, NI hub page and IMAS help page. These test for downloads/content.
* Within the testing framework, references to specific areas of a page are made (using xpath) with `data-uipath` which means that there's no confusion with classes and the styling.

## Sitemap/Hosts

* The sitemap is crucial for adding new pages - they must be added in there to be accessible.
* At time of writing, there are 5 environments live on the internet: Dev, Training, Test, UAT and Production.

## Import/Migration

* The published content for go-live was generated using the hippo migrator: https://github.com/NHS-digital-website/indicators-hippo-migrator
* The inputs to this were an Excel spreadsheet containing the Indicator information at field level, a Taxonomy spreadsheet and a set of PDF packs
* The original driving sheet and PDF packs can be found here: https://confluence.digital.nhs.uk/display/NIL/NIL+Development
* Taxonomy hub page can be found here: https://confluence.digital.nhs.uk/display/CW/Taxonomy
* The migrator project has a comprehensive README but essentially the NIL migration was run as follows:
```
  mvn -f pom.xml clean spring-boot:run "-Drun.arguments=--nationalIndicatorImportPath=/home/dev/imports/Indicators.xlsx,--nationalIndicatorAttachmentPath=/home/dev/imports/PDFpacks,--taxonomyDefinitionImportPath=/home/dev/Taxonomy.xlsx,--generateImportPackage"
```
* The resulting ZIP file was imported into Hippo locally via Admin/Update Editor/Migrator Import Script
* The National-Indicator-Library folder was then exported as XML via the console. This could then be imported via the console into any target environment

## Taxonomy

* The National Indicator Library uses the site-wide Taxonomy
* The classification of the original go-live content was done as part of the import/migration (see above)
* Taxonomy hub page can be found here: https://confluence.digital.nhs.uk/display/CW/Taxonomy

## Deployment

* (At time of writing) DEV & UAT are deployed to after any merge to master
* New content under the 'application' folder, e.g. repository-data/application/src/main/resources/hcm-content/content/documents/administration/national-indicator-library
  is automatically added to the CMS on deployment. HOWEVER existing content is not updated. This means that manual updates are required in conjunction with a release, e.g.
  adding a new header or label to an existing resource bundle. The same applies to facets.
* Security groups must be added manually as part of a deployment (otherwise the member lists will be wiped)

## Security/Pemissions/Groups

* The NIL content authoring/editing is controlled at group level in the same manner as publications etc.
* The components are all defined in YAML files within the GIT repository and can be viewed in the Console under hippo:configuration and comprise of domains, groups and queries (actions)
* hippo:domains - national-indicator-library/national-indicator-library-read
* hippo:groups - national-indicator-library-author/national-indicator-library-editor
* hippo:queries/templates - new-national-indicator-library-document/new-national-indicator-library-folder
* The groups have to be added manually via the Hippo Console (otherwise the member lists get wiped)
* Permissions are acceptance tested in userGroups.feature

## Git strategy

There is a strict set of rules when it comes to git strategy. It is intended that there will be one commit per pull request and each will be clearly marked with a JIRA ticket for transparency. Here are the steps involved:
* Branch from up-to-date master branch with the name and number of a JIRA ticket, e.g. NIL-150

```
$ git checkout -b NIL-150
```

* Develop!
* Before comitting, make sure all yaml is formatted with:

```
$ make format-yaml
```

* Beginning of commit message must start with "**[NIL-150]** Commit message..."
* Before submitting PR, you must rebase and squash to just one commit:

```shell
$ git fetch
$ git rebase -i origin/master
```

* Squash down all but one commit and resolve merge conflicts
* Pushing code to the repository is left as an exercise for the reader