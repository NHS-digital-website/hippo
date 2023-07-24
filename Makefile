include env.mk
include vars.mk

export HOME

ifneq ($(HIPPO_MAVEN_USERNAME),)
MVN_OPTS ?= ${MVN_VARS} --global-settings "$(PWD)/.mvn.settings.xml"
endif
MVN_OPTS ?= ${MVN_VARS}

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
	mvn clean verify $(MVN_OPTS) \
	-Djava.locale.providers=COMPAT \
	-am -DskipTests=true \
	-pl site/components,site/webapp,cms,repository-data/development,repository-data/site-development,repository-data/local
	$(MAKE) run

serve.tests.enhance: essentials/target/essentials.war
	mvn clean verify $(MVN_OPTS) \
	-Djava.locale.providers=COMPAT \
	-am -DskipTests=false \
	-pl site/components,site/webapp,cms,repository-data/development,repository-data/site-development,repository-data/local
	$(MAKE) run

## Clean, build and start local hippo
# Clean and recompile only modules that we do customise.
# Warning: Experimental. Requires Maven v3+
serve.enhance: essentials/target/essentials.war
	mvn -T 1C clean verify $(MVN_OPTS) \
	-am -DskipTests=true \
	-pl site/components,site/webapp,cms,repository-data/development,repository-data/site-development,repository-data/local
	$(MAKE) run

## Serve without allowing auto-export
# Clean and recompile only modules that we do customise.
serve.noexport: essentials/target/essentials.war
	mvn -T 1C clean verify $(MVN_OPTS) \
	-am -DskipTests=true \
	-pl site,cms,repository-data/development,repository-data/local
	$(MAKE) run PROFILE_RUN=cargo.run,without-autoexport

## Start server using cargo.run
run:
	mvn $(MVN_OPTS) -P $(PROFILE_RUN) $(REPO_PATH)

## Run only acceptance tests tagged with "WIP"
# This target requires a running site instance (e.g. `make serve.noexport`)
test.wip:
	mvn verify $(MVN_OPTS) -f acceptance-tests/pom.xml \
		-Pacceptance-test \
		-Dheadless=false \
		-Dcucumber.options="src/test/resources/features --tags @WIP" \


## Format YAML files, run after exporting to reduce changes
format-yaml:
	mvn gplus:execute@format-yaml $(MVN_OPTS) \
		-pl repository-data/local

# install hooks and local git config
.git/.local-hooks-installed:
	@bash .git-local/install

env.mk:
	touch env.mk

frontend:
	cd repository-data/webfiles && npm install && NODE_ENV=development npm start

lint-frontend:
	cd repository-data/webfiles && npm run lint

lint-frontend-fix:
	cd repository-data/webfiles && npm run lint-fix

ci-pipeline-lint-frontend:
	cd repository-data/webfiles && npm install && npm run lint

## proxy all other targets to ci-cd/Makefile
# Usage: make test
#        make test.site
#        make test.unit
%:
	$(MAKE) -C ci-cd/ $@