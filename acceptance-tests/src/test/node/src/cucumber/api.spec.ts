import {expect} from '@playwright/test';
import {Given, setWorldConstructor, Then} from '@cucumber/cucumber';
import getPage from './pages';
import CustomWorld from "./world";

const site = 'http://test:8080/site'

setWorldConstructor(CustomWorld);

Given('I navigate to {string} page', async function (page: string) {
    const url = getPage(page);
    await this.openUrl(site + url);
});

Then('I should see api page titled {string}', async function (pageTitle: string) {
    const actualTitle = await this.page.title();
    await expect(actualTitle).toEqual(pageTitle);
})
Then('I can see document title with content {string}', async function (docTitle: string) {
    const actualDocTitle = await this.page.innerText("h1");
    await expect(actualDocTitle).toEqual(docTitle);
})
Then('I should see page with text {string}', async function (pageText: string) {
    const apiLocator = this.page.locator('id=api-description__api-status-and-roadmap');
    await expect(apiLocator).toContainText(pageText);
})
;