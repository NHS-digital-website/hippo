import './utils/public-path';

/**
 * Scripts to load just before `</body>`
 */
import {initStickyNav} from "./sticky-nav/sticky-nav";
import {initChapterNav} from "./chapter-nav/chapter-nav";
import {tableSort} from "./table-sort/init-table-sort";
import {initCookieConsent} from "./relevance/relevance-cookie";
import {initCountup} from "./statistics/statistics-countup";
import {initSiteHeader} from "./header/site-header";
import {activateCodeBlocks} from "./highlighter/highlighter-copy-button";

initStickyNav();
initChapterNav();
initSiteHeader();
activateCodeBlocks();

const tables = document.querySelectorAll('table');
if (tables.length) {
    tableSort(tables);
}

initCookieConsent();
initCountup();

if (document.querySelector('[data-chartsource=highchart]')) {
    import(/* webpackChunkName: "highchart-setup" */ './highcharts/highchart-setup').then(module => {
        const charts = module.default;
        charts();
    })
}
