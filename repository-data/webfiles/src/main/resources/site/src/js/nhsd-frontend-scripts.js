import './utils/public-path';
import cookies from './utils/cookies';
import initGlobalHeaderEscapeClose from './header/global-header-escape-close';
import initGlobalHeaderFocusOrder from './header/global-header-focus-order';

/**
 * Scripts to load just before `</body>`
 */
import { initCookieConsent } from './relevance/relevance-cookie';
import initHeroTitles from './hero/hero-title-expand';
import { printingEvents } from './events/printingEvents';
import './print-publication';
import './feed-page';
import './show-hide-articles';

initCookieConsent();
printingEvents();
initGlobalHeaderEscapeClose();
initGlobalHeaderFocusOrder();

if (document.querySelector('[data-chartsource=highchart]')) {
    import(/* webpackChunkName: "highchart-setup" */ './highcharts/highchart-setup').then((module) => {
        const charts = module.default;
        charts();
    });
}

if (document.querySelector('[data-chartsource=nhsd-data-viz]')) {
    import(/* webpackChunkName: "data-viz-setup'" */ './data-viz-setup').then((module) => module.default());
}

// Download org prompt
if (document.querySelector('[data-org-prompt]')) {
    cookies.onCookieConsent('statistics').then(() => import(/* webpackChunkName: "org-prompt" */ './dialogs/org-prompt'));
}

if (document.querySelector('[data-eforms="setup"]')) {
    import(/* webpackChunkName: "eform-setup" */ './eforms/eforms').then((module) => {
        const eform = module.default;
        const {
            name,
            conditions,
            ajaxValidationUrl,
            ajaxSubmissionUrl,
        } = window.eformsInfo;

        eform(name, conditions, ajaxValidationUrl, ajaxSubmissionUrl);
    });
}

document.addEventListener('DOMContentLoaded', () => {
    initHeroTitles();
});
