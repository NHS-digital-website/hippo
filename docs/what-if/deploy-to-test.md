# What If I want to deploy to Test?

## How to Deploy specific branch to Test Environment

1. Get Rundek access first

2. Then create public key in `~/.ssh`

3. Now go to [https://idm.onehippo.com/pubkeys](https://idm.onehippo.com/pubkeys) & add public key

4. Create a file `~/nhsd.rundec.env`

5. Then Add following…

		export RD_URL=https://deploys.onehippo.com/api/21
		export RD_USER=
		export RD_PASSWORD=
		export RD_AUTH_PROMPT=false
		export RD_COLOR=0

6. Checkout the branch you want to deploy

		cd ci-cd

		make clean build ondemand.upload ondemand.deploy ENV=tst RD_CONF=~/nhsd.rundec.env

7. Now see the status here:

		https://deploys.onehippo.com/project/nhs/activity
