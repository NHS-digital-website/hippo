import './utils/public-path';
import cookies from './utils/cookies';

/**
 * Scripts to load just before `</body>`
 */
import {initCookieConsent} from "./relevance/relevance-cookie";
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

// Download org prompt
if (document.querySelector('[data-org-prompt]')) {
    cookies.onCookieConsent('statistics').then(() => import(/* webpackChunkName: "org-prompt" */ './dialogs/org-prompt'));
}
