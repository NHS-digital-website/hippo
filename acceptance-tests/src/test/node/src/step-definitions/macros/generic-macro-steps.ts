import {DataTable, Then} from '@cucumber/cucumber';
import {CustomWorld} from "../../setup/world/CustomWorld";
import macroSelectors, {MacroSelectors, isVariant} from "../../helpers/macro-selectors";
import { expect } from 'chai';

Then('I should see {string} macros with following heading text:', async function (this: CustomWorld, macro: keyof MacroSelectors, variantHeadings: DataTable) {
    const page = await this.browser.getPage();

    for (const [variant, expectedHeadingText] of variantHeadings.raw()) {
        if (!isVariant(macro, variant)) {
            throw `Variant "${variant}" not found`;
        }

        const macroElement = page.locator(macroSelectors[macro][variant]);
        const headings = macroElement.locator('[class^=nhsd-t-heading-]');
        const [firstHeading] = await headings.allInnerTexts();
        expect(firstHeading).to.equal(expectedHeadingText);
    }
});
