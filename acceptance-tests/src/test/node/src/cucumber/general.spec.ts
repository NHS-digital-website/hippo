import { expect } from '@playwright/test';
import {Given, setWorldConstructor, Then} from '@cucumber/cucumber';
import getPage from './pages';
import CustomWorld from "./world";

const site = 'http://test:8080/site'

setWorldConstructor(CustomWorld);

Given('I navigate to the {string} page', async function (page: string) {
    const url = getPage(page);
    await this.openUrl(site + url);
});

Then('I should see page titled {string}', async function (pageTitle: string) {
    const actualTitle = await this.page.title();
    await expect(actualTitle).toEqual(pageTitle);
});

