import * as playwright from 'playwright';
import { World, setWorldConstructor, IWorldOptions } from '@cucumber/cucumber';

class CustomWorld extends World {
    public page?: playwright.Page;
    public browser: String = 'chrome';

    constructor(options: IWorldOptions) {
        super(options);
    }

    async screenshot(name = "screenshot") {
        if (!this.page) return null;
        return await this.page.screenshot({ fullPage: true, type: "jpeg" });
    }

    async launchBrowser() {
        if (this.browser == 'firefox') {
            return await playwright.firefox.launch();
        }
        return await playwright.chromium.launch();
    }

    async openUrl(url: string) {
        const browser = await this.launchBrowser();
        const context = await browser.newContext();
        this.page = await context.newPage();
        return await this.page.goto(url);
    }
}

setWorldConstructor(CustomWorld);
