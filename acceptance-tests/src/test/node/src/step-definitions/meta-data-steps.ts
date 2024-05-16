import {Then} from '@cucumber/cucumber';
import {expect} from 'chai';
import {CustomWorld} from "../setup/world/CustomWorld";

Then('I should see page titled {string}', async function (this: CustomWorld, expectedTitle: string) {
    const page = await this.browser.getPage();
    const actTitle = await page.title();
    expect(expectedTitle).to.equal(actTitle);
});
