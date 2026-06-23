import {DataTable, Then, When} from '@cucumber/cucumber';
import {CustomWorld} from "../../setup/world/CustomWorld";
import {expect} from 'chai';


Then('I should see {string} results when search text is {string}:', { timeout: 30000 }, async function (this: CustomWorld, resultsNum: string, searchText: string, recordResult: DataTable) {
    for (const [count, searchText] of recordResult.raw()) {
        const page = await this.browser.getPage();
        await page.locator('[data-test-text=searchBox]').fill(searchText);

        await page.locator('[data-test-text=button]').click();
        await page.waitForLoadState('networkidle');
        const [oneResults] = await page.locator('.nhsd-t-heading-s').allInnerTexts();
        expect(oneResults).to.equal(count + ' result');
    }
});

Then('I should see {string} results when click on filter {string}:', { timeout: 30000 }, async function (this: CustomWorld, resultsNum: string, searchText: string, recordResult: DataTable) {
    for (const [count, filterValue, filterType] of recordResult.raw()) {
        const page = await this.browser.getPage();
        const expectedFilterText = filterValue + ' (' + count + ')';
        const filterMenu = page.locator('[data-active-menu="' + filterType + '"]');

        expect(await filterMenu.count()).to.equal(1);
        const filterText = (await filterMenu.locator('.nhsd-a-checkbox__label').allTextContents())
            .map((text: string) => text.trim());
        expect(filterText).to.include(expectedFilterText);
    }
});
