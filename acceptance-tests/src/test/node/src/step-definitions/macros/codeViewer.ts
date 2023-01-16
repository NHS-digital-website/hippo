import {DataTable, Then, When} from '@cucumber/cucumber';
import {CustomWorld} from "../../setup/world/CustomWorld";
import {expect} from 'chai';

When('I click {string} tab of {string} code viewer variant', { timeout: 30000 }, async function (this: CustomWorld, tab: string, variant: string) {
    const page = await this.browser.getPage();
    const tabLink = page.locator(`[data-variant=${variant}] .nhsd-a-tab`).locator(`text=${tab}`);
    expect(await tabLink.count()).to.equal(1);
    await tabLink.click();
});

When('I click open example link of {string} code viewer variant', { timeout: 30000 }, async function (this: CustomWorld, variant: string) {
    const page = await this.browser.getPage();
    const openExampleLink = page.locator(`[data-variant=${variant}] .nhsd-o-code-viewer__header .nhsd-a-link:visible`);
    expect(await openExampleLink.count()).to.equal(1);
    await openExampleLink.click();
});

When('I click the copy button of the {string} code viewer variant', { timeout: 30000 }, async function (this: CustomWorld, variant: string) {
    const context = await this.browser.getContext();
    await context.grantPermissions(['clipboard-write']);
    const page = await this.browser.getPage();
    const copyButton = page.locator(`[data-variant=${variant}] .nhsd-a-button:visible`).locator(`text=Copy`);
    expect(await copyButton.count()).to.equal(1);
    await copyButton.click();
});

Then ('my clipboard should contain:', { timeout: 30000 }, async function (this: CustomWorld, copiedCodeTable: DataTable) {
    const context = await this.browser.getContext();
    await context.grantPermissions(['clipboard-read']);
    const page = await this.browser.getPage();
    const clipboard = await page.evaluate(async () => await navigator.clipboard.readText());
    for (const [testCode] of copiedCodeTable.raw()) {
        expect(clipboard).to.contain(testCode);
    }
});

Then('I should see {string} code viewer variant with code:', { timeout: 30000 }, async function (this: CustomWorld, variant: string, testCodeTable: DataTable) {
    const page = await this.browser.getPage();
    const visibleCodeElement = page.locator(`[data-variant=${variant}] .nhsd-o-code-viewer__code-content:visible`);
    expect(await visibleCodeElement.count()).to.equal(1);
    const visibleCode = await visibleCodeElement.innerText();
    for (const [testCode] of testCodeTable.raw()) {
        expect(visibleCode.includes(testCode)).to.be.true;
    }
});
