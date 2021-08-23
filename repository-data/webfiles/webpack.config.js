const isDev = process.env.NODE_ENV && process.env.NODE_ENV === "development";
const isProd = process.env.NODE_ENV && process.env.NODE_ENV === "production";

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
                    options: '$'
                }]
            },
            {
                test: /\.m?js$/,
                exclude: /node_modules\/(?!(sticky-sidebar)\/).*/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env']
                    }
                }
            }
        ],
    },
    externals: {
        nhsd: 'nhsd',
    },
}

const webpackDevConfig = {
    mode: 'development',
    devtool: 'source-map'
}

const webpackConfig = isDev ?
    ({ ...webpackDefaultConfig, ...webpackDevConfig}) :
    ({ ...webpackDefaultConfig});

module.exports = webpackConfig;
