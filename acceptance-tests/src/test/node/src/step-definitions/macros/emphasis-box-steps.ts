import {When, Then} from '@cucumber/cucumber';
import {CustomWorld} from "../../setup/world/CustomWorld";
import { expect } from 'chai';
import { Locator } from 'playwright';

let emphasisBox: Locator = null;

When('I see an emphasis box with heading {string}', async function (this: CustomWorld, heading: string) {
    const page = await this.browser.getPage();
    emphasisBox = page.locator('.nhsd-m-emphasis-box', {
        has: page.locator(`[class^="nhsd-t-heading-"]:has-text("${heading}")`)
    });

    expect(await emphasisBox.count()).to.equal(1);
});

Then('emphasis box should contain text {string}', async function (this: CustomWorld, content: string) {
    expect(emphasisBox).to.not.be.null;

    const textLocator = emphasisBox.locator(`:text("${content}")`);
    console.log(await textLocator.allInnerTexts());
    expect(await textLocator.count()).to.equal(1);
});

Then('emphasis box should have a(n?) {string} border colour', async function (this: CustomWorld, colour: string) {
    const borderBox = emphasisBox.locator(`.nhsd-a-box--border-${colour}`);
    expect(await borderBox.count()).to.equal(1);
});