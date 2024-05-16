/* eslint-disable no-console */

// Include gulp
const del = require('del');
const browsersync = require('browser-sync').create();
const postcss = require('gulp-postcss');
const sass = require('gulp-dart-sass');
const stylelint = require('gulp-stylelint');
const named = require('vinyl-named');
const webpack = require('webpack-stream');

const autoprefixer = require('autoprefixer');
const cssnano = require('cssnano');

const {
    task, src, dest, series, watch, parallel,
} = require('gulp');

const isDev = process.env.NODE_ENV && process.env.NODE_ENV === 'development';

const devRoot = 'localhost:8080';
const webfileRoot = 'src/main/resources/site';
const paths = {
    js: `${webfileRoot}/src/js`,
    scss: `${webfileRoot}/src/scss`,
    dist: `${webfileRoot}/dist`,
};

task('clean', () => del([paths.dist]));

task('lint-scss', () => src(`${paths.scss}/**/*.scss`).pipe(
    stylelint({
        reporters: [{ formatter: 'string', console: true }],
    }),
));

const postCssPlugins = [autoprefixer];

if (!isDev) {
    postCssPlugins.push(cssnano);
}

const cssPaths = [
    `./${paths.scss}/*.scss`,
    `./${paths.scss}/rebrand/*.scss`,
    `./${paths.scss}/ndrs/*.scss`,
];

task('build-css', async () => src(cssPaths, { sourcemaps: isDev })
    .pipe(sass().on('error', sass.logError))
    .pipe(postcss(postCssPlugins))
    .pipe(dest(`./${paths.dist}`, { sourcemaps: isDev ? '.' : false }))
    .pipe(browsersync.stream()));

// Concatenate && Minify JS
task('build-js', async () => {
    // eslint-disable-next-line global-require
    const webpackConfig = require('./webpack.config');
    webpackConfig.output.path = `/${paths.js}`;

    const jsPaths = [
        `${paths.js}/nhsd-priority-scripts.js`,
        `${paths.js}/nhsd-frontend-scripts.js`,
        `${paths.js}/nhsd-scripts.js`,
        `${paths.js}/ndrs-priority-scripts.js`,
        `${paths.js}/ndrs-frontend-scripts.js`,
        `${paths.js}/nhsd-intranet-priority-scripts.js`,
        `${paths.js}/nhsd-intranet-scripts.js`,
    ];

    return src(jsPaths).pipe(named())
        .pipe(
            webpack({
                config: webpackConfig,
            }).on('error', function webpackError(err) {
                console.log(err.message);
                this.emit('end'); // Recover from errors
            }),
        )
        .pipe(dest(paths.dist));
});

task('scss', series('lint-scss', 'build-css'));
task('scripts', series('build-js'));

task('build', series('clean', parallel('build-css', 'build-js')));

// create a task that ensures the `js` task is complete before
// reloading browsers
task('script-watch', series('build-js', (done) => {
    browsersync.reload();
    done();
}));

// Watch Files For Changes
task('watch', (done) => {
    browsersync.init({
        proxy: `http://${devRoot}/site`,
        rewriteRules: [
            // Allow Bloomreach to reload on FTL change
            {
                match: /return "ws:\/\/" \+ document.location.host \+ CONTEXT_PATH \+ AUTO_RELOAD_PATH;/g,
                fn() {
                    return `return "ws://${devRoot}" + CONTEXT_PATH + AUTO_RELOAD_PATH;`;
                },
            },
        ],
    });

    watch([`${paths.scss}/**/*.scss`, `./${paths.scss}/rebrand/*.scss`, `./${paths.scss}/ndrs/*.scss`], series('build-css'));
    watch(`${paths.js}/**/*.js`, series('script-watch'));
    done();
});

task('default', series('build', 'watch'));
