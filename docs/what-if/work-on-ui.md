# What If I want to work on the UI

## Quickstart

Ensure that Node/NPM is installed locally

```bash
make frontend
```

> The Maven build now downloads its own copy of Node into `repository-data/webfiles/.node/<version>`. If you previously had `repository-data/webfiles/node` on disk (from an older build) remove that directory before running `npm install` or the Maven frontend tasks, otherwise npm may fail with `Class extends value undefined is not a constructor or null` when it loads `fs-minipass`.

## How it works

The following build process happens via `frontend-maven-plugin` during the Maven
 build, so no user intervention is required to make compiled static assets.

### Technologies

- Node: server-side JavaScript, used to power the task runner and tooling.
- NPM: Node Package Manager.
- Browserslist - browser target configuration, set in `package.json`
- Gulp: Task runner. Configured in `gulpfile.js`
- Dart-sass: Modern Sass compiler
- PostCSS: CSS transformation library
- Babel: JS transpiler
- Webpack: JS bundler
- Browsersync: local proxy server to handle live reload and easy device testing.

Source file location:

`repository-data/webfiles/src/main/resources/site/src`

Compiled file location:

`repository-data/webfiles/src/main/resources/site/dist`

### Commands

Lint all CSS & JS files (Not all passing at time of writing)

```bash
npm run lint
```

Build JS files

```bash
npm run build-js
```
Build CSS files

```bash
npm run build-css
```

Build CSS & JS files

```bash
npm run build
```

Initialise Browsersync, build all files, and start watching for changes

```bash
npm start
```

### CSS

The following SCSS files are each compiled to a matching CSS files:
- `nhsuk.scss`
- `nhsuk-print.scss`
- `nhsuk-print-pdf-document.scss`

Each one is compiled to CSS, and then run through the following PostCSS plugins:
- Autoprefixer - this handles browser prefixes and adds/removes them according to
the supported browserslist string in `package.json`.
- CSSNano - This minifier shrinks the CSS file as much as possible without
changing the overall meaning.

### JS

The following JS files are each compiled to a matching JS file:
- `nhsd-priority-scripts.js`
- `nhsd-scripts.js`

The above files and their dependencies are transpiled via Babel, according to the supported browsers specified in `package.json`. Each one is bundled to a single file via Webpack, using the config file `webpack.config.js`.

`nhsd-priority-scripts.js` is enqueued in the document head, and should be
restricted to JS that must run before the body is parsed.
`nhsd-scripts.js` is enqueued in the document head. Most JS should be placed in here.
