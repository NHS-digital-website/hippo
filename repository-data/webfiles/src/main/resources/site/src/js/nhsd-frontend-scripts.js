import './utils/public-path';

/**
 * Scripts to load just before `</body>`
 */
import {initCookieConsent} from "./relevance/relevance-cookie";
import "./nhsd-frontend/nhsd-frontend";
import {printingEvents} from "./events/printingEvents";
import "./print-publication";
import "./feed-page";

initCookieConsent();
printingEvents();

if (document.querySelector('[data-chartsource=highchart]')) {
    import(/* webpackChunkName: "highchart-setup" */ './highcharts/highchart-setup').then(module => {
        const charts = module.default;
        charts();
    })
}
