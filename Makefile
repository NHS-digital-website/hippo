include env.mk

HIPPO_MAVEN_PASSWORD ?=
HIPPO_MAVEN_USERNAME ?=
HOME ?= $(shell printenv HOME)
# speed up dev compilation
# http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/2b2511bd3cc8/src/share/vm/runtime/advancedThresholdPolicy.hpp#l34
MAVEN_OPTS ?= "-XX:+TieredCompilation -XX:TieredStopAtLevel=1"
PWD = $(shell pwd)
SPLUNK_TOKEN ?=

export HIPPO_MAVEN_PASSWORD
export HIPPO_MAVEN_USERNAME
export HOME

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
	mvn clean verify -pl site,cms,repository-data/development -am -DskipTests=true
	$(MAKE) run

## Serve without allowing auto-export
# Clean and recompile only modules that we do customise.
serve.noexport: essentials/target/essentials.war
	mvn clean verify -pl site,cms,repository-data/development -am -DskipTests=true
	mvn -P cargo.run,without-autoexport

## Start server using cargo.run
run:
	mvn -P cargo.run -Dsplunk.token=$(SPLUNK_TOKEN)

# we don't have to recompile it every time.
essentials/target/essentials.war:
	mvn clean verify -pl essentials -am --offline -DskipTests=true

## Run acceptance tests against already running server (`make serve`)
test.site-running:
	mvn verify -f acceptance-tests/pom.xml \
		-Pacceptance-test \
		-Dcucumber.options="src/test/resources/features/site"

## Run only acceptance tests taged with "WIP"
# This target requires running site (for instance `make serve.noexport`)
test.wip:
	mvn verify -f acceptance-tests/pom.xml \
		-Pacceptance-test \
		-Dheadless=false \
		-Dcucumber.options="src/test/resources/features --tags @WIP" \

## Format YAML files, run after exporting to reduce changes
format-yaml:
	mvn groovy:execute \
		-Dsource=repository-data/development/src/main/script/YamlFormatter.groovy \
		-pl repository-data/development

## Update maven dependency versions
update-dependencies:
	mvn verify versions:update-parent versions:use-latest-versions versions:update-properties versions:commit -U -DskipTests=true

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
