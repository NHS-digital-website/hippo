import {CustomWorld} from "../setup/world/CustomWorld";

export default {
    accept: async function(world: CustomWorld) {
        const page = await world.browser.getPage();
        try {
            const cookieBotAllowButton = await page.waitForSelector('#CybotCookiebotDialogBodyLevelButtonLevelOptinAllowAll', { timeout: 20000 });
            await cookieBotAllowButton.click();
            await page.waitForSelector('#CybotCookiebotDialog', {state: 'hidden'});
        } catch (e) {
            console.log('Cookie bot notice didn\'t load');
        }
    }
}
