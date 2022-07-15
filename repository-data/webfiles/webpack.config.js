// const ESLintPlugin = require('eslint-webpack-plugin');

const isDev = process.env.NODE_ENV && process.env.NODE_ENV === 'development';

const webpackDefaultConfig = {
    mode: 'production',
    output: {
        filename: '[name].bundle.js',
        chunkFilename: '[name].bundle.js',
    },
    module: {
        rules: [
            {
                test: require.resolve('jquery'),
                use: [{
                    loader: 'expose-loader',
                    options: {
                        exposes: ['$', 'jQuery'],
                    },
                }],
            },
            {
                test: /\.m?js$/,
                exclude: /node_modules\/(?!(sticky-sidebar)\/).*/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env'],
                    },
                },
            },
        ],
    },
    optimization: {
        chunkIds: 'named',
        splitChunks: {
            chunks() {
                return false;
            },
        },
    },
    externals: {
        nhsd: 'nhsd',
    },
    // Webpack eslint plugin to be enabled once all js lint errors have been fixed
    // plugins: [new ESLintPlugin()],
};

const webpackDevConfig = {
    mode: 'development',
    devtool: 'source-map',
};

const webpackConfig = isDev
    ? ({ ...webpackDefaultConfig, ...webpackDevConfig })
    : ({ ...webpackDefaultConfig });

module.exports = webpackConfig;
