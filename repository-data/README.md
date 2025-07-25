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

This module also provides [updater scripts](https://xmdocumentation.bloomreach.com/library/concepts/update/updater-scripts.html). To run any of them, access http://localhost:8080/cms/systemupdater with the application running locally.
ApiSpecificationRenderer is one such script, helping to shorten development cycle when making changes to the logic of rendering API specifications, where it allows to quickly see their effect without having to restart the application.

The *webfiles* module is intended to contribute webfiles (only) to the repository data. Like the
application module, it is intended to be included on every environment.

If your application requires so, you can create more repository-data submodules to be deployed to the
environments of your liking, similar to above described default setup.

NOTE: We have added another module called local which contains the bare minimum config required to run
and test locally without including development data. This is useful for testing locally
on a 'production like' environment.
