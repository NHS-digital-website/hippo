//testing
import {After, Before, Status} from '@cucumber/cucumber';

Before({ tags: '@chrome' }, function () {
    this.browser = 'chrome';
});

Before({ tags: '@firefox' }, function () {
    this.browser = 'firefox';
});

After(async function(scenario) {
    if (scenario.result && scenario.result.status === Status.FAILED) {
        const screenshot = await this.screenshot(scenario.testCaseStartedId);
        this.attach(screenshot, 'image/jpeg');
    }
});