module.exports = {
    default: {
        parallel: 2,
        format: ['html:cucumber-report.html', 'summary:cucumber-summary.txt'],
        requireModule: ['ts-node/register'],
        require: [
            'src/cucumber/setup/**/*.ts',
            'src/cucumber/step_definitions/**/*.ts'
        ],
        publishQuiet: true
    }
}