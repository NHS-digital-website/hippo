import './utils/public-path';
import searchPage from './intranet/search-page';

if (document.querySelector('[data-chartsource=highchart]')) {
    import(/* webpackChunkName: "highchart-setup" */ './highcharts/highchart-setup').then(module => {
        const charts = module.default;
        charts();
    });
}

window.onload = function() {
    searchPage();
}
