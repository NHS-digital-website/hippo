import * as dataviz from 'nhsd-dataviz';

function optionsTranslator(options) {
    let icon;
    if (options.icon?.type) {
        icon = options.icon.type.replace(' ', '-').toLowerCase();
    }

    let colourOption;
    if (options.colour?.colourOption) {
        colourOption = options.colour.colourOption.replace(' ', '-').toLowerCase();
    }

    return {
        vizType: options.chart?.type || 'pie',
        title: options.title?.text,
        introText: options.xlxsValues?.introText,
        icon,
        data: {
            ...options.xlxsValues,
            ratio: {
                numerator: options.xlxsValues?.numerator,
                denominator: options.xlxsValues?.denominator,
            },
        },
        palette: colourOption,
    };
}

export default function initDataViz() {
    const datavizScripts = Array.from(document.querySelectorAll('[data-chartsource=nhsd-data-viz]'));

    datavizScripts.forEach((dvScript) => {
        const chartId = dvScript.dataset.sectionid;
        const data = window.datavizData[chartId];

        dataviz.chart(`#chart-${chartId}`, optionsTranslator(data));
    });
}
