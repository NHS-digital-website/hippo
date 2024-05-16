import {After, ITestCaseHookParameter, Status} from '@cucumber/cucumber';
import {CustomWorld} from "../world/CustomWorld";

After(async function(this: CustomWorld, scenario: ITestCaseHookParameter) {
    if (scenario.result && scenario.result.status === Status.FAILED) {
        const screenshot = await this.browser.screenshot();
        if (screenshot) this.attach(screenshot, 'image/png');
    }

    await this.browser.destroy();
});
