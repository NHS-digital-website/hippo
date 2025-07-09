# Frontend Roadmap

## Create an integrated frontend build process
Using a modern deterministic build process, we can make use of powerful static asset tooling. Intsead of bundling JavaScript inline, we can serve up separate files, reducing file load as a user navigates throughout the site. CSS can be transpiled and minified. Rarely used JS can be dynamically loaded as required.

## Use Manifest files to bypass Bloomreach's cache breaking
Bloomreach currently serves static files from within a cache-busting folder, i.e. `/site/webfiles/1591372284699/dist/nhsd-priority-scripts.bundle.js` and `/site/webfiles/1591372284699/dist/nhsuk.css`. The long number will regenerate every time the site is built. That means that a deploy will break all cached files, even if the contents have not changed.

It is possible to bypass this, and to fetch webfiles directly. https://xmdocumentation.bloomreach.com/library/concepts/web-files/web-files-stable-urls.html discusses how to access the files directly. In this case, the file hash could be attached to the filename directly, such as `nhsd-priority-scripts.205199ab45963f6a62ec.bundle.js`. To pass that hash to the Freemaker template, an intermediate `manifest.json` file could be created, with the following specification:

```json
{
  "nhsd-priority-scripts.bundle.js": "nhsd-priority-scripts.7e2c49a622975ebd9b7e.bundle.js",
  "nhsd-scripts.bundle.js": "nhsd-scripts.205199ab45963f6a62ec.bundle.js",
  "nhsuk.css": "nhsuk.7e2c49a622975ebd9b7e.css"
}
```

This should be updated during the build process.

## Activate Linting

Linting frameworks have already been installed - eslint for linsting JS files, and Stylelint for linting SCSS files. These can be run via `npx lint-js` and `npx lint-scss` respectively. Ideally these should be run as part of the normal build process, catching errors and style violations before they are committed. However, two things should happen before that:
 1. Code style rules should be agreed upon. Some suggested linting configurations are installed.
 2. the existing codebase should be adapted to adhere to these style rules.
Additionally, these linting tools should be run on commit. To enable that, `lint-staged` and `husky` have been installed.

## Create JavaScript tests

There are many JavaScript testing libraries. Popular options include Jest or Jasmine for unit tests.

## Start Using TypeScript

As the website as a whole is written in a statically typed language, it would be a straightforward assumption that using a statically typed superset of JavaScript may be a positive move forward. Compilation-time type safety would bring many benefits, and should be a straightforward upgrade. The TypeScript compiler supports plain JavaScript, so a smooth upgrade path would involve updating individual JavaScript files at a time.
