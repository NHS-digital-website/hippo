import {test, expect} from '@playwright/test';
import {injectAxe, checkA11y} from 'axe-playwright'
import {navigateToPage} from './navigation';


test.describe('General page', () => {
    test.beforeEach(async ({page}) => {
        await navigateToPage('./website-acceptance-tests/general-test-document', page);
        await injectAxe(page);
    });

    test('Page should be titled "General AcceptanceTestDocument - NHS Digital"', async ({page}) => {
        const actualTitle = await page.title();
        expect(actualTitle).toEqual("General AcceptanceTestDocument - NHS Digital");
    });

    test('Page should look visually correct', async ({page}) => {
        await expect(page).toHaveScreenshot();
    });

    test('Page should pass accessibility checks', async ({page}) => {
        await checkA11y(page);
    });
});
