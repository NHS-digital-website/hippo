import {Given, When} from '@cucumber/cucumber';
import {CustomWorld} from "../setup/world/CustomWorld";
import cookieNotice from "../helpers/handle-cookie-notice";
import {Viewport} from "../setup/world/BrowserDelegate";

const siteUrl = 'http://localhost:8080/site';

const pages: [{
    name: string,
    url: string
}] = [{
    name: 'General test document',
    url: '/website-acceptance-tests/general-test-document'
}];

function getPageUrl(pageName: string) {
    const page = pages.find(page => page.name === pageName);
    return page ? page.url : null;
}

function getMacroTestPageUrl(macro: string) {
    return "/automated-test-pages/macros?macro=" + macro;
}

Given('I navigate to the {string} page', {timeout: 20000}, async function (this: CustomWorld, page: string) {
    const pageUrl = getPageUrl(page);
    if (!pageUrl) throw "Page not found";
    const res = await this.browser.openUrl(siteUrl + pageUrl);
    if (!res.ok()) throw "Navigation error";

    await cookieNotice.accept(this);
    this.currentPage = page;
});

Given('I navigate to the {string} macro test page', {timeout: 20000}, async function (this: CustomWorld, macro: string) {
    const pageUrl = getMacroTestPageUrl(macro);
    const res = await this.browser.openUrl(siteUrl + pageUrl);
    if (!res.ok()) throw "Navigation error";
    this.currentPage = 'Macro test page';
});

When('I view page on {string}', async function(this: CustomWorld, device: Viewport) {
    await this.browser.switchViewport(device);
});

