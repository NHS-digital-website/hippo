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

## Clean, build and start local hippo (no autoexport)
serve:
	mvn clean verify
	mvn -P cargo.run,without-autoexport

test:
	$(MAKE) -C ci-cd/ $@
test.%:
	$(MAKE) -C ci-cd/ $@

# install hooks and local git config
.git/.local-hooks-installed:
	@bash .git-local/install
