import {DataTable, Then} from '@cucumber/cucumber';
import { expect } from 'chai';
import macroSelectors, {MacroSelectors, isVariant} from "../helpers/macro-selectors";
import {CustomWorld} from "../setup/world/CustomWorld";

Then('the page should look visually correct', async function(this: CustomWorld) {
    const page = await this.browser.getPage()
    const screenshot = await page.screenshot({fullPage: true});

    const fileName = this.currentPage.toLowerCase().replace(/ /g, '-').replace(/[^a-z-]/g, '');

    const result = await this.matchSnapshot(screenshot, `${fileName}-${this.browser.viewport}`);
    expect(result).to.be.true;
});

Then('I should see the following {string} macros look visually correct:', async function(this: CustomWorld, macro: keyof MacroSelectors, variants: DataTable) {
    for (const [variant] of variants.raw()) {
        if (!isVariant(macro, variant)) throw `Variant "${variant}" not found`;

        const page = await this.browser.getPage();
        const macroElement = page.locator(macroSelectors[macro][variant]);

        const imageBuff = await macroElement.screenshot({
            scale: "css"
        });

        const result = await this.matchSnapshot(imageBuff, `${macro}-${variant}-macro-${this.browser.viewport}`);
        expect(result).to.be.true;
    }
});
