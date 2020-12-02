import './utils/public-path';

/**
 * Scripts to load just before `</body>`
 */
import {
    initStickyNav,
    initStickyNavSubItems
} from "./sticky-nav/sticky-nav";
import {initChapterNav} from "./chapter-nav/chapter-nav";
import {tableSort} from "./table-sort/init-table-sort";
import {initCookieConsent} from "./relevance/relevance-cookie";
import {initCountup} from "./statistics/statistics-countup";
import {initSiteHeader} from "./header/site-header";
import {activateCodeBlocks} from "./highlighter/highlighter-copy-button";
import {tableSortDate} from "./table-sort/table-sort-date";

jQuery(() => {
    initStickyNav()
    initStickyNavSubItems()
})
initChapterNav();
initSiteHeader();
activateCodeBlocks();

const tables = document.querySelectorAll('table');
if (tables.length) {
    tableSortDate()
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

if (document.querySelector('[data-eforms="setup"]')) {
    import(/* webpackChunkName: "eform-setup" */ './eforms/eforms').then(module => {
        const eform = module.default;
        const {name, conditions, ajaxValidationUrl, ajaxSubmissionUrl} = window.eformsInfo;

        eform(name, conditions, ajaxValidationUrl, ajaxSubmissionUrl)
    })
}

if (document.querySelectorAll('[data-line-clamp="setup"]')) {
    import(/* webpackChunkName: "line-clamp-setup" */ './utils/line-clamp-polyfill').then(module => {
        const webkitLineClamp = module.default;
        if(window.webkitLineClamp.length > 0) {
            window.webkitLineClamp.forEach(el => {
                webkitLineClamp(el.element, el.lineCount, el.colour)
            })
        }
    })
}
