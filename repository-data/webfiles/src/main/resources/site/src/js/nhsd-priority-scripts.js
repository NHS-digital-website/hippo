import './utils/public-path';

/**
 * Scripts to load in the `<head>`
 */
import jQuery from "jquery";

import { initBreadcrumbs } from './breadcrumbs/breadcrumbs';
import { initAria } from './details-element/aria';
import { initPolyfill } from './details-element/details-element-polyfill/dist/details-element-polyfill';

import "./cookiebot/cookiebot-load";
import "./utils/js-enabled";
import "./utils/vanilla-js-utils";

window.jquery = jQuery;
window.$ = jQuery;

initBreadcrumbs();
initAria();
initPolyfill();
