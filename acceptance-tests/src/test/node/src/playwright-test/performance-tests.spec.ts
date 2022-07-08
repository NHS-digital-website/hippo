import { test, expect } from '@playwright/test';
import lighthouse from 'lighthouse';

test.use({
    launchOptions: {
        args: ['--remote-debugging-port=9222'],
    }
});

test.describe('Performance tests', () => {
    test.skip(({ browserName }) => browserName !== "chromium");

    test('General page should pass performance checks', async ({ page, browserName, baseURL }) => {
        const report = await lighthouse(baseURL + 'website-acceptance-tests/general-test-document');
        const performanceScore = report.lhr.categories.performance.score;
        console.log(`Performance score: ${performanceScore}`);
        expect(performanceScore).toBeGreaterThan(0.4);
    });
});
