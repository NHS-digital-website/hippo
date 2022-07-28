import * as playwright from 'playwright';
import { World, setWorldConstructor, IWorldOptions } from '@cucumber/cucumber';

class CustomWorld extends World{
    public page?: playwright.Page;

    constructor(options: IWorldOptions) {
        super(options)
    }

    async screenshot(name = "screenshot") {
        if (!this.page) return null;
        return await this.page.screenshot({ fullPage: true, type: "jpeg" });
    }

    async openUrl(url: string) {
        const browser = await playwright.chromium.launch({
            headless: true,
        });
        const context = await browser.newContext();
        this.page = await context.newPage();
        await this.page.goto(url);
    }
}

setWorldConstructor(CustomWorld);
