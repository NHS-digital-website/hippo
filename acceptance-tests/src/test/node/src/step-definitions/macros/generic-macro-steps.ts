import {DataTable, Then} from '@cucumber/cucumber';
import {CustomWorld} from "../../setup/world/CustomWorld";
import { expect } from 'chai';

Then('I should see {string} variants with the following {string} text:', async function (this: CustomWorld, macro: string, textType: string, variantText: DataTable) {
    const page = await this.browser.getPage();

    for (const [variant, expectedText] of variantText.raw()) {
        const macroElement = page.locator(`[data-variant='${variant}']`);
        expect(await macroElement.count()).greaterThan(0);
        const elements = macroElement.locator(`[data-test-text="${textType}"]`);
        const [firstElement] = await elements.allInnerTexts();

        if (expectedText.endsWith('...')) {
            const startingText = expectedText.slice(0, -3);
            expect(firstElement.startsWith(startingText)).to.be.true;
        } else {
            expect(firstElement).to.equal(expectedText);
        }
    }
});

Then('I should see {string} variants with an image with alt text:', async function (this: CustomWorld, macro: string, variantAltText: DataTable) {
    const page = await this.browser.getPage();

    for (const [variant, altText] of variantAltText.raw()) {
        const macroElement = page.locator(`[data-variant='${variant}']`);
        expect(await macroElement.count()).greaterThan(0);
        const imageWithAltText = macroElement.locator(`img[alt="${altText}"]`);
        expect(await imageWithAltText.count()).greaterThan(0);
    }
});
