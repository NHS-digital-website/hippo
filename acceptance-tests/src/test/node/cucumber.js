module.exports = {
    default: {
        parallel: 2,
        format: ['html:cucumber-report.html'],
        requireModule: ['ts-node/register'],
        require: ['src/cucumber/**/*.ts'],
        publishQuiet: true
    }
}