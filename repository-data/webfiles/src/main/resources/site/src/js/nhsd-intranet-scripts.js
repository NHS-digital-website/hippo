import './utils/public-path';

if (document.querySelector('[data-chartsource=highchart]')) {
    console.log('here');
    import(/* webpackChunkName: "highchart-setup" */ './highcharts/highchart-setup').then(module => {
        const charts = module.default;
        charts();
    })
}
