
BUILD_DIR ?= ../../ps-build
ENV ?= tst
HIPPO_MAVEN_PASSWORD ?=
HIPPO_MAVEN_USERNAME ?=
HOME ?= $(shell printenv HOME)
OD_ENV = $(shell bash ondemand-env-name $(ENV))
PWD = $(shell pwd)
RD_CONF = $(PWD)/rd.conf
VENV ?= $(PWD)/.venv
VERSION ?= $(shell git describe --tags --match "$$(cat $(PWD)/../.version)*")

PATH := $(VENV)/bin:$(PWD)/bin:$(PWD)/vendor:$(shell printenv PATH)
SHELL := env PATH=$(PATH) /bin/bash

export HIPPO_MAVEN_USERNAME
export HIPPO_MAVEN_PASSWORD

ifneq ($(HIPPO_MAVEN_USERNAME),)
MVN_OPTS ?= --file ../pom.xml -Drevision=$(VERSION) --global-settings "$(PWD)/../.mvn.settings.xml"
endif
MVN_OPTS ?= --file ../pom.xml -Drevision=$(VERSION)

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

## Sudo for AWS Roles
# Usage:
#   $(make aws-sudo PROFILE=profile-name)
#   $(make aws-sudo PROFILE=profile-with-mfa TOKEN=123789)
aws-sudo: $(VENV)
	@(printenv TOKEN > /dev/null && aws-sudo -m $(TOKEN) $(PROFILE) ) || ( \
		aws-sudo $(PROFILE) \
	)

## Test, tag, build and upload artefact
# You still have to push newly created tag
release: test version.bumpup
	# we have to call build manually, so that the VERSION is set properly.
	$(MAKE) build upload

## Run all tests
test: clean test.unit test.e2e

## Run unit tests
test.unit: clean
	mvn package $(MVN_OPTS) -DskipTests --quiet
	mvn test $(MVN_OPTS) --quiet

## Run BDD tests for "site" only.
test.site: clean
	mvn package $(MVN_OPTS) -DskipTests --quiet
	mvn verify $(MVN_OPTS) --quiet \
		-Dcucumber.options="src/test/resources/features/site" \
		-Pacceptance-test-hippo,acceptance-test

## Run BDD tests
test.e2e: clean
	mvn package $(MVN_OPTS) -DskipTests --quiet
	mvn verify $(MVN_OPTS) --quiet \
		-Pacceptance-test-hippo,acceptance-test

## Build distribution package
build:
	mvn verify $(MVN_OPTS) -DskipTests=true --quiet
	mvn $(MVN_OPTS) -P separate-dist -DskipTests=true
	mvn $(MVN_OPTS) -P on-demand-dist -DskipTests=true

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

## Upload build artefact
upload: $(VENV)
	@echo "Task: upload Hippo $(VERSION) build distribution to S3 bucket"
	aws s3 \
		cp ../target/website-$(VERSION)-cms-distribution.tar.gz \
		s3://artefacts.ps.digital.nhs.uk/hippo_authoring/$(VERSION)/website.tgz
	aws s3 \
		cp ../target/website-$(VERSION)-site-distribution.tar.gz \
		s3://artefacts.ps.digital.nhs.uk/hippo_delivery/$(VERSION)/website.tgz

## Upload distribution file to ondemand sftp server
ondemand.upload:
	echo "put ../target/website-$(VERSION)-on-demand-distribution.tar.gz /uploads/dists/$(VERSION).tar.gz" \
		| sftp nhs@static.hosting.onehippo.com

## Upload migration distribution file to ondemand sftp server
ondemand.upload-migration:
	echo "put ../target/website-$(VERSION)-distribution-with-migration-data.tar.gz /uploads/dists/$(VERSION)-with-migration-data.tar.gz" \
		| sftp nhs@static.hosting.onehippo.com

## Deploy to OnDemand environment
# Usage: make ondemand.deploy ENV=tst
#        make ondemand.deploy ENV=uat
#        make ondemand.deploy ENV=prd
ondemand.deploy: vendor/rd
	source rd.conf && vendor/rd run -p nhs -j "jobs/$(OD_ENV)/Deploy" -- \
		-bootstrap full \
		-distribution "$(VERSION).tar.gz"

ondemand.restart: vendor/rd
	vendor/rd run -p nhs -j "jobs/$(OD_ENV)/Control" -- \
		-action restart

deploy.vagrant: build
	cp ../target/website-$(VERSION)-site-distribution.tar.gz $(BUILD_DIR)/.artefacts/
	cp ../target/website-$(VERSION)-cms-distribution.tar.gz $(BUILD_DIR)/.artefacts/
	cd $(BUILD_DIR) && ROLE=hippo_authoring vagrant ssh -c "/home/hippo_authoring/shipit /vagrant/.artefacts/website-$(VERSION)-cms-distribution.tar.gz"

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

## Clean up
clean:
	mvn clean $(MVN_OPTS) --quiet

# Install Rundec CLI
vendor/rd: vendor
	cd vendor && curl -LO https://github.com/rundeck/rundeck-cli/releases/download/v1.0.22/rundeck-cli-1.0.22-all.jar
	echo -e "#!/bin/bash \nDIR=\$$( cd \"\$$( dirname \"\$${BASH_SOURCE[0]}\" )\" && pwd ) \njava -jar \$${DIR}/rundeck-cli-1.0.22-all.jar \$$@" > vendor/rd
	chmod +x vendor/rd

vendor:
	mkdir -p vendor

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
