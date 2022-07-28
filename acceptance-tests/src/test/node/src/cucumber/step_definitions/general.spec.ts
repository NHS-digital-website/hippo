import { expect } from '@playwright/test';
import {Given, Then} from '@cucumber/cucumber';
import getPage from './pages';

const site = 'http://localhost:8080/site';

Given('I navigate to the {string} page', {timeout: 3 * 5000}, async function (page: string) {
    const url = getPage(page);
    return await this.openUrl(site + url);
});

Then('I should see page titled {string}', async function (pageTitle: string) {
    const actualTitle = await this.page.title();
    await expect(actualTitle).toEqual(pageTitle);
});

