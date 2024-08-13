include ../vars.mk

BUILD_DIR ?= ../../ps-build
ENV ?= tst
OD_ENV = $(shell bash ondemand-env-name $(ENV))
RELEASE_DATE = $(shell d=1; while (($$(date --date +$${d}day +%u) >= 6)); do ((++d)); done; date --date +$${d}day +%Y-%m-%d)
VENV ?= $(PWD)/.venv
VERSION ?= $(shell git describe --tags --match "$(VERSION_BASE)*" || echo $(VERSION_BASE).0)
VERSION_BASE ?= $(shell cat "$(PWD)/../.version")

CI ?= false

PLAYWRIGHT_PROFILE = playwright-test
ifeq ($(CI),true)
PLAYWRIGHT_PROFILE = playwright-install,playwright-test
endif

PATH := $(VENV)/bin:$(PWD)/bin:$(PWD)/vendor:$(shell printenv PATH)
SHELL := env PATH=$(PATH) /bin/bash

ifneq ($(HIPPO_MAVEN_USERNAME),)
MVN_OPTS ?= --file ../pom.xml -Drevision=$(VERSION) ${MVN_VARS} --global-settings "$(PWD)/../.mvn.settings.xml"
endif
MVN_OPTS ?= --file ../pom.xml -Drevision=$(VERSION) ${MVN_VARS}

ifeq ($(TRAVIS_EVENT_TYPE),cron)
TRAVIS_BUILD_CMD = test.e2e
endif

.PHONY: .phony

## Prints this help
help:
	@awk -v skip=1 \
		'/^##/ { sub(/^[#[:blank:]]*/, "", $$0); doc_h=$$0; doc=""; skip=0; next } \
		 skip  { next } \
		 /^#/  { doc=doc "\n" substr($$0, 2); next } \
		 /:/   { sub(/:.*/, "", $$0); printf "\033[34m%-30s\033[0m\033[1m%s\033[0m %s\n\n", $$0, doc_h, doc; skip=1 }' \
		$(MAKEFILE_LIST)

## Initialise local project
init: $(VENV)

## Test, tag, build and upload artefact
# You still have to push newly created tag
release: test version.bumpup
	# we have to call build manually, so that the VERSION is set properly.
	$(MAKE) build upload

## Run tests (legacy)
test:
	mvn --file ../pom.xml -Drevision=$(VERSION) clean test

## Run unit tests
test.unit:
	mvn package $(MVN_OPTS) -DskipTests --quiet
	mvn test $(MVN_OPTS) --quiet

## Run BDD tests for "site" only with playwright (visual test).
test.site:
	mvn clean verify $(MVN_OPTS) -f acceptance-tests/pom.xml --quiet \
		-Djava.locale.providers=COMPAT \
		-Dcucumber.options="src/test/resources/features/site" \
        -Dcucumber.filter.tags="not @skip" \
		-Pacceptance-test-hippo,acceptance-test,$(PLAYWRIGHT_PROFILE)

## Run acceptance tests against already running server (`make serve`) with playwright (visual test).
test.site-running:
	mvn verify $(MVN_OPTS) -f acceptance-tests/pom.xml \
		-Djava.locale.providers=COMPAT \
		-Dcucumber.options="src/test/resources/features/site" \
		-Dcucumber.filter.tags="not @skip" \
		-Pacceptance-test,$(PLAYWRIGHT_PROFILE)

# Updates snapshots for all tests tagged @snapshot
test.update-snapshots:
	cd ../acceptance-tests/src/test/node && npm run update-snapshots

## Run tests for cms only, without development data
test.cms:
	mvn clean verify $(MVN_OPTS) --quiet \
		-Dcucumber.options="src/test/resources/features/cms" \
		-Pacceptance-test-hippo,acceptance-test,acceptance-without-development-data

## Run BDD tests
test.e2e:
	$(MAKE) test.site
	$(MAKE) test.cms

## Build distribution package
build:
	mvn verify $(MVN_OPTS) -DskipTests=true
	mvn $(MVN_OPTS) -P dist -DskipTests=true

## Set a success status on current commit
# You would normally run this target after running (and passing) all tests
# Usage: make github.status.success
github.status.success:
	bash bin/set-commit-status "success" "Version $(VERSION) passed unit and site tests."

## Updates 'ENV' tag and pushes is back to github
# Usage: make git.update-environment-tag
#        git.update-environment-tag ENV=uat VERSION=v2.0.31
git.update-environment-tag:
	git tag --force $(ENV) $(VERSION)
	git push --force --no-verify origin tag $(ENV)


## Create new version tag based on the nearest tag
version.bumpup:
	@git fetch --tags
	@git tag $$((git describe --abbrev=0 --tags --match "$$(cat ../.version)*" || echo $$(cat ../.version).-1) | perl -pe 's/^(v(\d+\.)*)(-?\d+)(.*)$$/$$1.($$3+1).$$4/e')
	$(MAKE) version.pprint
	@echo "Remember to push new tag to remote server."

## Prints current version
version.print:
	@echo "$(VERSION)"

## Pretty prints current version
version.pprint:
	@echo "- - -"
	@echo "Current version: $(VERSION)"
	@echo "- - -"

## Ensure that commit message contains JIRA id
check.commit-msg:
ifeq ("$(shell git log -n1 --no-merges --pretty=%ce)","noreply@github.com")
	@echo "GitHub commit. No need to check."
else
	@git log -n1 --no-merges --pretty=%B > $(PWD)/.commit-msg
	@head -n1 $(PWD)/.commit-msg | grep -E "^\[[A-Z]+-[0-9]+\]" || (echo "Missing JIRA id in the commit message. Expecting '[ABC-123] ...' but got" && cat .commit-msg && exit 1)
	python $(PWD)/../.git-local/hooks/commit-msg $(PWD)/.commit-msg
	@rm $(PWD)/.commit-msg
endif

## Ensure that `make format-yaml` was run
check.yaml:
	cd .. && make format-yaml
	@if git status | grep ".yaml" | grep "modified"; then \
		echo "Modified yaml files found. Run 'make format-yaml' before submitting pull request."; \
		exit 1; \
	else \
		echo "No YAML changes."; \
	fi

## Clean up
clean:
	mvn clean $(MVN_OPTS) --quiet

# get python virtualenv
$(VENV):
	@which virtualenv > /dev/null || (\
		echo "please install virtualenv: http://docs.python-guide.org/en/latest/dev/virtualenvs/" \
		&& exit 1 \
	)
	virtualenv $(VENV)
	$(VENV)/bin/pip install -U "pip<9.0"
	$(VENV)/bin/pip install pyopenssl urllib3[secure] requests[security]
	$(VENV)/bin/pip install -r requirements.txt --ignore-installed
	virtualenv --relocatable $(VENV)

.phony:
