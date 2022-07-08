module.exports = {
    default: {
        format: ['html:cucumber-report.html', 'summary:cucumber-summary.txt', '@cucumber/pretty-formatter'],
        requireModule: ['ts-node/register'],
        require: [
            'src/setup/**/*.ts',
            'src/step-definitions/**/*.ts'
        ],
        publish: true,
    }
}
