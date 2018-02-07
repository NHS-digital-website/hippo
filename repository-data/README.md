Intent of repository data submodules
====================================

The repository-data module contains all definitions to be bootstrapped into the repository.
It is sub-divided into three sub-modules:

  1) application
  2) development
  3) webfiles

Repository data vital to the application (i.e. which should be bootstrapped into any environment)
should go into the *application* module. In fresh projects, the auto-export mechanism is configured
such that all exported repository-data is added to the application submodule.

Repository data intended for development environments only (i.e. local or CI environments) should
go into the *development* module. By default, this module is available to the bootstrapping mechanism
when deploying the application locally (-Pcargo.run), but not included when building a distribution
(-Pdist). In order to include the development module in a distribution, build it with
-Pdist-with-development-data. You can add repository data into the development module by moving YAML
sources, or by configuring auto-export to export certain repository paths to the development module.

The *webfiles* module is intended to contribute webfiles (only) to the repository data. Like the
application module, it is intended to be included on every environment.

If your application requires so, you can create more repository-data submodules to be deployed to the
environments of your liking, similar to above described default setup.

**Migration Module**

There is also now a module for migration. This is used when running an EXIM import on the migrated 
content. 

To export just to the migration module:

  1) Go to `/repository-data/application/src/main/resources/hcm-config/configuration/modules/autoexport-module.yaml`
  and change the autoexport module to `migration`
  2) Run `mvn verify` (builds the migration module with all the others)
  3) Run `-Pcargo.run,with-migration-data`
