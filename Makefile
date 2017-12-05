PWD = $(shell pwd)

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
serve:
	mvn clean verify
	mvn -P cargo.run

## Serve without allowing auto-export
serve.noexport:
	mvn clean verify
	mvn -P cargo.run,without-autoexport

## Run all tests
test:
	$(MAKE) -C ci-cd/ $@

## Run acceptance tests against already running server (`make serve`)
test.site-running:
	mvn verify -f acceptance-tests/pom.xml \
		-Pacceptance-test \
		-Dcucumber.options="src/test/resources/features/site"

test.%:
	$(MAKE) -C ci-cd/ $@

## Format YAML files, run after exporting to reduce changes
format-yaml:
	mvn groovy:execute \
		-Dsource=repository-data/development/src/main/script/YamlFormatter.groovy \
		-pl repository-data/development

# install hooks and local git config
.git/.local-hooks-installed:
	@bash .git-local/install
