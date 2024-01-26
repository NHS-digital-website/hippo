import {Given, When, Then, DataTable} from '@cucumber/cucumber';
import {CustomWorld} from "../setup/world/CustomWorld";
import cookieNotice from "../helpers/handle-cookie-notice";
import {Viewport} from "../setup/world/BrowserDelegate";
import {expect} from 'chai';

const siteUrl = 'http://localhost:8080/site';

const pages: {
    name: string,
    url: string
}[] = [{
    name: 'General test document',
    url: '/website-acceptance-tests/general-test-document'
}, {
    name: 'Cyber Alert',
    url: '/cyber-alerts'
}, {
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

Then('I wait {int} seconds', {timeout: 60000}, async function (this: CustomWorld, timeout: number) {
    await this.browser.timeout(timeout * 1000);
});

When('I view page on {string}', async function (this: CustomWorld, device: Viewport) {
    await this.browser.switchViewport(device);
});

Then('I click on the link named {string}', async function (this: CustomWorld, target: string) {
    const page = await this.browser.getPage();
    await page.locator('.nhsd-a-menu-item__label').click();
});

Then('I should see text {string}', async function (this: CustomWorld, target: string) {
    const page = await this.browser.getPage();
    const targetElement = page.locator(`[data-text="${target}"]`);
    const filterText = await targetElement.innerText();
    expect(filterText).equal(target)
});

Then('I should see logo and click on logo', async function (this: CustomWorld) {
    const page = await this.browser.getPage();
    const targetElement = await page.locator(`.nhse-global-menu__logo`).getAttribute("href");
    expect(targetElement).that.is.oneOf(["/", "/site/"])
    const homePage = await page.locator(`.nhse-global-menu__logo`).click()
});

Then('I should see search icon', async function (this: CustomWorld) {
    const page = await this.browser.getPage();
    const targetElement = await page.locator(`#nhsd-global-header__search-button`).getAttribute("href");
    expect(targetElement).that.is.oneOf(["/search", "/site/search"])

});
When('I click see search icon', async function (this: CustomWorld) {
    const page = await this.browser.getPage();
    const homePage = await page.locator(`#nhsd-global-header__search-button`).click()

});

When('I hover on {string}', async function (this: CustomWorld, target: string) {
    const page = await this.browser.getPage();
    if (target == "logo") {
        const targetElement = await page.locator(`.nhse-global-menu__logo`).hover();
    }
    if (target == "search") {
        const targetElement = await page.locator(`#nhsd-global-header__search-button`).hover();
    }
    if (target == "menu") {
        const targetElement = await page.locator(`.nhsd-a-menu-item`).hover();
    }
});
Then('The page should look visually correct when I hover on {string}', async function (this: CustomWorld, target: string, items: DataTable) {
    for (const [item] of items.raw()) {
        const page = await this.browser.getPage()
        if (item == "logo") {
            const targetElement = await page.locator(`.nhse-global-menu__logo`).hover();
        }
        if (item == "menu") {
            if (this.browser.viewport == "mobile" && this.browser.jsEnabled) {
                const targetElement = await page.locator(`#nhsd-global-header__menu-button`).click();
            }
            const targetElement = await page.locator(`.nhsd-a-menu-item__label`).hover();
        }
        if (item == "search") {
            const targetElement = await page.locator(`#nhsd-global-header__search-button`).hover();
        }

        const screenshot = await page.screenshot({fullPage: true});

        const fileName = this.currentPage.toLowerCase().replace(/ /g, '-').replace(/[^a-z-]/g, '');
        const result = await this.matchSnapshot(screenshot, `${fileName}-${item}-${this.browser.viewport}-${this.browser.jsEnabled}`);
        expect(result).to.be.true;
    }
});

Given('I navigate to JS disabled {string} page', {timeout: 60000}, async function (this: CustomWorld, page: string) {
    const pageUrl = getPageUrl(page);
    if (!pageUrl) throw "Page not found";
    const res = await this.browser.openUrl(siteUrl + pageUrl, false);
    if (!res.ok()) throw "Navigation error";
    this.currentPage = page;
});

Then('I should see search box', async function (this: CustomWorld) {
    const page = await this.browser.getPage();
    const targetElement = page.locator(`#query`);
    const filterText = await targetElement.getAttribute("placeholder");
    if (!this.browser.jsEnabled) {
        expect(filterText).equal("Search Clinical Indicators")
    } else {
        expect(filterText).equal("What are you looking for today?")

    }
});

