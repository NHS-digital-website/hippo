
PWD = $(shell pwd)
VENV ?= $(PWD)/.venv
VERSION ?= $(shell git describe --tags)
PS_BUILD_DIR ?= ../../ps-build

PATH := $(VENV)/bin:$(shell printenv PATH)
SHELL := env PATH=$(PATH) /bin/bash

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

## Run unit tests
test:
	mvn --file ../pom.xml -Drevision=$(VERSION) clean test

## Build distribution package
build:
	mvn --file ../pom.xml -Drevision=$(VERSION) verify
	mvn --file ../pom.xml -Drevision=$(VERSION) -P separate-dist

## Upload build artefact
upload: $(VENV)
	@echo "Task: upload Hippo $(VERSION) build distribution to S3 bucket"
	aws s3 \
		cp ../target/publication-system-$(VERSION)-cms-distribution.tar.gz \
		s3://artefacts.ps.digital.nhs.uk/hippo_authoring/$(VERSION)/publication-system.tgz
	aws s3 \
		cp ../target/publication-system-$(VERSION)-site-distribution.tar.gz \
		s3://artefacts.ps.digital.nhs.uk/hippo_delivery/$(VERSION)/publication-system.tgz

deploy.vagrant: build
	cp ../target/publication-system-$(VERSION)-site-distribution.tar.gz $(PS_BUILD_DIR)/.artefacts/
	cp ../target/publication-system-$(VERSION)-cms-distribution.tar.gz $(PS_BUILD_DIR)/.artefacts/
	cd $(PS_BUILD_DIR) && ROLE=hippo_authoring vagrant ssh -c "/home/hippo_authoring/shipit /vagrant/.artefacts/publication-system-$(VERSION)-cms-distribution.tar.gz"

## Create new version tag based on the nearest tag
version.bumpup:
	@git fetch --tags
	@git tag $$((git describe --abbrev=0 --tags | grep $$(cat ../.version) || echo $$(cat ../.version).-1) | perl -pe 's/^(v(\d+\.)*)(-?\d+)(.*)$$/$$1.($$3+1).$$4/e')
	$(MAKE) version.print
	@echo "Remember to push new tag to remote server."

## Prints current version
version.print:
	@echo "- - -"
	@echo "Current version: $(VERSION)"
	@echo "- - -"

## Clean up
clean:
	mvn --file ../pom.xml -Drevision=$(VERSION) clean
	rm -rf $(VENV)

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
