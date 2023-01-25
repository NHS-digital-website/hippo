import {DataTable, Then} from '@cucumber/cucumber';
import { expect } from 'chai';
import {CustomWorld} from "../setup/world/CustomWorld";

Then('the page should look visually correct', async function(this: CustomWorld) {
    const page = await this.browser.getPage()
    const screenshot = await page.screenshot({
        fullPage: true,
        animations: "disabled"
    });

    const fileName = this.currentPage.toLowerCase().replace(/ /g, '-').replace(/[^a-z-]/g, '');

    const result = await this.matchSnapshot(screenshot, `${fileName}-${this.browser.viewport}`);
    expect(result).to.be.true;
});

Then('I should see the following {string} variants look visually correct:', async function(this: CustomWorld, macro: string, variants: DataTable) {
    for (const [variant] of variants.raw()) {
        const page = await this.browser.getPage();

        // Grey out iframe content to prevent flakey tests
        const stylesheet = await page.addStyleTag({
            content:
                `iframe { filter: contrast(0); }`
        });

        const macroElement = page.locator(`[data-variant='${variant}']`);
        expect(await macroElement.count()).greaterThan(0);

        await page.mouse.move(0, 0);
        const imageBuff = await macroElement.screenshot({
            scale: "css",
            animations: "disabled"
        });

        const result = await this.matchSnapshot(imageBuff, `${macro}-${variant.replace(' ', '-')}-macro-${this.browser.viewport}`);
        expect(result).to.be.true;

        await stylesheet.dispose();
    }
});
