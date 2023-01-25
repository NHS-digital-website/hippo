import {Then, When} from '@cucumber/cucumber';
import {CustomWorld} from "../../setup/world/CustomWorld";
import {expect} from 'chai';


When('I click the button to open/close the {string} dropdown variant', { timeout: 30000 }, async function (this: CustomWorld, variant: string) {
    const page = await this.browser.getPage();
    const variantLocator = page.locator(`[data-variant="${variant}"]`);
    expect(await variantLocator.count()).to.be.greaterThan(0);

    // Find button
    const dropdownButtonLocator = variantLocator.locator('.nhsd-o-dropdown__input .nhsd-a-button');
    expect(await dropdownButtonLocator.count()).to.be.greaterThan(0);
    await dropdownButtonLocator.click();
    await this.browser.timeout(200);
});

When('I search {string} in the search box for the {string} dropdown variant', { timeout: 30000 }, async function (this: CustomWorld, searchTerm: string, variant: string) {
    const page = await this.browser.getPage();
    const variantLocator = page.locator(`[data-variant="${variant}"]`);
    expect(await variantLocator.count()).to.be.greaterThan(0);

    // Find search input
    const dropdownInputLocator = variantLocator.locator('.nhsd-o-dropdown__input .nhsd-t-form-input');
    expect(await dropdownInputLocator.count()).to.be.greaterThan(0);

    // Enter text
    await dropdownInputLocator.fill(searchTerm);
    await this.browser.timeout(200);
});

Then('{string} should be the selected dropdown value', async function (this: CustomWorld, expectedValue: string) {
    const page = await this.browser.getPage();
    const selectedValue = await page.evaluate('window.dropdownValue');
    expect(selectedValue).to.equal(expectedValue);
});

Then('the {string} dropdown should be {open}', async function (this: CustomWorld, variant: string, open: boolean) {
    const page = await this.browser.getPage();
    const dropdownComponentLocator = page.locator(`[data-variant="${variant}"] .nhsd-o-dropdown`);
    expect(await dropdownComponentLocator.count()).to.be.greaterThan(0);

    const isVisable = await dropdownComponentLocator.locator('.nhsd-o-dropdown__dropdown').isVisible();
    expect(isVisable).to.equal(open);
});

Then('I click off to close the dropdown', { timeout: 30000 }, async function (this: CustomWorld) {
    const page = await this.browser.getPage();
    const bodyLocator = page.locator(`body`);
    await bodyLocator.click();
    await page.waitForTimeout(500);
});
