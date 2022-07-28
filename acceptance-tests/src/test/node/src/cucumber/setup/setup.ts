import { After, Status } from '@cucumber/cucumber';

After(async function(scenario) {
    if (scenario.result && scenario.result.status === Status.FAILED) {
        const screenshot = await this.screenshot(scenario.testCaseStartedId);
        this.attach(screenshot, 'image/jpeg');
    }
});