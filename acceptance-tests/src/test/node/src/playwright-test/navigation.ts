import { Page } from 'playwright';

export async function acceptCookies(page: Page) {
    await page.locator('#CybotCookiebotDialogBodyLevelButtonLevelOptinAllowAll').click();
    await page.waitForSelector('#CybotCookiebotDialog', { state:'hidden' });
}

export async function navigateToPage(url: string, page: Page) {
    await page.goto(url);
    //await acceptCookies(page);
}
