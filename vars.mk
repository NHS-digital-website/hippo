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

#-Dsplunk.token=$(SPLUNK_TOKEN) \
#	-Dsplunk.url=$(SPLUNK_URL) \
#	-Dsplunk.hec.name=$(SPLUNK_HEC) \

MVN_VARS = -Ddynamic.bean.generation=false \
	-Dexternalstorage.aws.bucket=$(S3_BUCKET) \
	-Dexternalstorage.aws.region=$(S3_REGION) \
	-Dspring.profiles.active=local

export AWS_ACCESS_KEY_ID=$(AWS_KEY)
export AWS_SECRET_ACCESS_KEY=$(AWS_SECRET)
export GOOGLE_CAPTCHA_SECRET=$(GOOGLE_RECAPTCHA_SECRET_KEY)
export HIPPO_MAVEN_PASSWORD
export HIPPO_MAVEN_USERNAME
