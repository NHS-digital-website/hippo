AWS_KEY ?=
AWS_SECRET ?=
GOOGLE_RECAPTCHA_SECRET_KEY ?=
HIPPO_MAVEN_PASSWORD ?=
HIPPO_MAVEN_USERNAME ?=
HOME ?= $(shell printenv HOME)
PWD = $(shell pwd)
SPLUNK_HEC ?= localhost
SPLUNK_TOKEN ?=
SPLUNK_URL ?= http://localhost
PROFILE_RUN ?= cargo.run
S3_BUCKET ?= files.local.nhsd.io
S3_REGION ?= eu-west-1
APIGEE_SYNC_ENABLED ?= false
APIGEE_SYNC_CRON ?= 0 0/1 * ? * *
APIGEE_TOKEN_URL ?= https://login.apigee.com/oauth/token
APIGEE_SPECS_ALL_URL ?= https://apigee.com/dapi/api/organizations/nhsd-nonprod/specs/folder/home
APIGEE_SPECS_ONE_URL ?= https://apigee.com/dapi/api/organizations/nhsd-nonprod/specs/doc/{specificationId}/content
APIGEE_USER ?=
APIGEE_PASS ?=
APIGEE_OTPKEY ?=
APIGEE_BASIC ?= ZWRnZWNsaTplZGdlY2xpc2VjcmV0

#-Dsplunk.token=$(SPLUNK_TOKEN) \
#	-Dsplunk.url=$(SPLUNK_URL) \
#	-Dsplunk.hec.name=$(SPLUNK_HEC) \

MVN_VARS = -Ddynamic.bean.generation=false \
	-Dexternalstorage.aws.bucket=$(S3_BUCKET) \
	-Dexternalstorage.aws.region=$(S3_REGION) \
	-Dspring.profiles.active=local \
    -Ddevzone.apispec.sync.enabled=$(APIGEE_SYNC_ENABLED) \
    "-Ddevzone.apispec.sync.cron-expression=$(APIGEE_SYNC_CRON)" \
    -Ddevzone.apigee.resources.specs.all.url=$(APIGEE_SPECS_ALL_URL) \
    -Ddevzone.apigee.resources.specs.individual.url=$(APIGEE_SPECS_ONE_URL) \
	-Ddevzone.apigee.oauth.token.url=$(APIGEE_TOKEN_URL)

export AWS_ACCESS_KEY_ID=$(AWS_KEY)
export AWS_SECRET_ACCESS_KEY=$(AWS_SECRET)
export GOOGLE_CAPTCHA_SECRET=$(GOOGLE_RECAPTCHA_SECRET_KEY)
export HIPPO_MAVEN_PASSWORD
export HIPPO_MAVEN_USERNAME
export DEVZONE_APIGEE_OAUTH_USERNAME=$(APIGEE_USER)
export DEVZONE_APIGEE_OAUTH_PASSWORD=$(APIGEE_PASS)
export DEVZONE_APIGEE_OAUTH_BASICAUTHTOKEN=$(APIGEE_BASIC)
export DEVZONE_APIGEE_OAUTH_OTPKEY=$(APIGEE_OTPKEY)
