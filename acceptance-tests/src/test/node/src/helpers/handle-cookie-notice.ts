import {CustomWorld} from "../setup/CustomWorld";

export default {
    accept: async function(world: CustomWorld) {
        const page = await world.browser.getPage();
        await page.locator('#CybotCookiebotDialogBodyLevelButtonLevelOptinAllowAll').click();
        await page.waitForSelector('#CybotCookiebotDialog', {state: 'hidden'});
    }
}
