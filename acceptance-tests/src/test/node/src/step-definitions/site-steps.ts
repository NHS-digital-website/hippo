import {Given, When, Then} from '@cucumber/cucumber';
import {CustomWorld} from "../setup/world/CustomWorld";
import cookieNotice from "../helpers/handle-cookie-notice";
import {Viewport} from "../setup/world/BrowserDelegate";
import { expect } from 'chai';

const siteUrl = 'http://localhost:8080/site';

const pages: {
    name: string,
    url: string
}[] = [{
    name: 'General test document',
    url: '/website-acceptance-tests/general-test-document'
},{
    name: 'Cyber Alert',
    url: '/cyber-alerts'
},{
    name: 'Service test document',
    url: '/website-acceptance-tests/service-test-document'
}];

function getPageUrl(pageName: string) {
    const page = pages.find(page => page.name === pageName);
    return page ? page.url : null;
}

function getMacroTestPageUrl(macro: string) {
    return "/automated-test-pages/macros?macro=" + macro.replace(/ /, '-');
}

Given('I navigate to the {string} page', {timeout: 60000}, async function (this: CustomWorld, page: string) {
    const pageUrl = getPageUrl(page);
    if (!pageUrl) throw "Page not found";
    const res = await this.browser.openUrl(siteUrl + pageUrl);
    if (!res.ok()) throw "Navigation error";

    await cookieNotice.accept(this);
    this.currentPage = page;
});

Given('I navigate to the {string} macro test page', {timeout: 60000}, async function (this: CustomWorld, macro: string) {
    const pageUrl = getMacroTestPageUrl(macro);
    const res = await this.browser.openUrl(siteUrl + pageUrl);
    if (!res.ok()) throw "Navigation error";
    this.currentPage = 'Macro test page';
});

Given('I click target {string} element', async function (this: CustomWorld, target: string) {
    const page = await this.browser.getPage();
    const targetElement = page.locator(`[data-test-target="${target}"]`);
    expect(await targetElement.count()).to.be.greaterThan(0);
    await targetElement.first().scrollIntoViewIfNeeded();
    await this.browser.timeout(1000);
    await targetElement.first().click();
});

Then('I wait {int} seconds', {timeout: 60000}, async function(this: CustomWorld, timeout: number) {
    await this.browser.timeout(timeout * 1000);
});

When('I view page on {string}', async function(this: CustomWorld, device: Viewport) {
    await this.browser.switchViewport(device);
});

let newPagePromise: Promise<any> = null;
When('I wait for a new page to open', async function(this: CustomWorld) {
    const browerContext = await this.browser.getContext();
    newPagePromise = browerContext.waitForEvent('page');
});

Then('a new page should open with url, {string}', async function(this: CustomWorld, expectedUrl: string) {
    const newPage = await newPagePromise;
    await newPage.waitForLoadState();
    const pageUrl = newPage.url();
    expect(pageUrl.endsWith(expectedUrl)).to.be.true;
});
