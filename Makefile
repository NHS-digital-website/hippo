include env.mk

AWS_KEY ?=
AWS_SECRET ?=
HIPPO_MAVEN_PASSWORD ?=
HIPPO_MAVEN_USERNAME ?=
HOME ?= $(shell printenv HOME)
MVN_OPTS ?=
PWD = $(shell pwd)
SPLUNK_HEC ?= localhost
SPLUNK_TOKEN ?=
SPLUNK_URL ?=

export HIPPO_MAVEN_PASSWORD
export HIPPO_MAVEN_USERNAME
export HOME

ifneq ($(HIPPO_MAVEN_USERNAME),)
MVN_OPTS = --global-settings "$(PWD)/.mvn.settings.xml"
endif

## Prints this help
help:
	@awk -v skip=1 \
		'/^##/ { sub(/^[#[:blank:]]*/, "", $$0); doc_h=$$0; doc=""; skip=0; next } \
		 skip  { next } \
		 /^#/  { doc=doc "\n" substr($$0, 2); next } \
		 /:/   { sub(/:.*/, "", $$0); printf "\033[34m%-30s\033[0m\033[1m%s\033[0m %s\n\n", $$0, doc_h, doc; skip=1 }' \
		$(MAKEFILE_LIST)

## Initialise local project
init: .git/.local-hooks-installed

## Clean, build and start local hippo
# Clean and recompile only modules that we do customise.
serve: essentials/target/essentials.war
	mvn clean verify $(MVN_OPTS) -pl site,cms,repository-data/development,repository-data/local -am -DskipTests=true
	$(MAKE) run

## Serve without allowing auto-export
# Clean and recompile only modules that we do customise.
serve.noexport: essentials/target/essentials.war
	mvn clean verify $(MVN_OPTS) -pl site,cms,repository-data/development,repository-data/local -am -DskipTests=true
	mvn $(MVN_OPTS) -P cargo.run,without-autoexport

## Start server using cargo.run
run:
	mvn $(MVN_OPTS) -P cargo.run \
		-D splunk.token=$(SPLUNK_TOKEN) \
		-D splunk.url=$(SPLUNK_URL) \
		-D splunk.hec.name=$(SPLUNK_HEC) \
		-D aws.secretKey=$(AWS_SECRET) \
		-D aws.accessKeyId=$(AWS_KEY)

# we don't have to recompile it every time.
essentials/target/essentials.war:
	mvn clean verify $(MVN_OPTS) -pl essentials -am --offline -DskipTests=true

## Run acceptance tests against already running server (`make serve`)
test.site-running:
	mvn verify $(MVN_OPTS) -f acceptance-tests/pom.xml \
		-Pacceptance-test \
		-Dcucumber.options="src/test/resources/features/site"

## Run only acceptance tests taged with "WIP"
# This target requires running site (for instance `make serve.noexport`)
test.wip:
	mvn verify $(MVN_OPTS) -f acceptance-tests/pom.xml \
		-Pacceptance-test \
		-Dheadless=false \
		-Dcucumber.options="src/test/resources/features --tags @WIP"

## Run only acceptance tests taged with "WIP"
# This target requires running site (for instance `make serve.noexport`)
test.s3-upload:
	mvn verify $(MVN_OPTS) -f acceptance-tests/pom.xml \
		-Pacceptance-test \
		-Dheadless=false \
		-Dcucumber.options="src/test/resources/features/s3-upload"

## Format YAML files, run after exporting to reduce changes
format-yaml:
	mvn groovy:execute $(MVN_OPTS) \
		-Dsource=repository-data/development/src/main/script/YamlFormatter.groovy \
		-pl repository-data/development

## Update maven dependency versions
update-dependencies:
	mvn verify $(MVN_OPTS) versions:update-parent versions:use-latest-versions versions:update-properties versions:commit -U -DskipTests=true

## proxy all other targets to ci-cd/Makefile
# Usage: make test
#        make test.site
#        make test.unit
%:
	$(MAKE) -C ci-cd/ $@

# install hooks and local git config
.git/.local-hooks-installed:
	@bash .git-local/install

env.mk:
	touch env.mk
