import * as playwright from 'playwright';
import { World, IWorldOptions } from '@cucumber/cucumber';

export default class CustomWorld extends World{
    public page: playwright.Page | undefined;

    constructor(options: IWorldOptions) {
        super(options)
    }

    async openUrl(url: string) {
        const browser = await playwright.chromium.launch({
            headless: false,
        });
        const context = await browser.newContext();
        this.page = await context.newPage();
        await this.page.goto(url);
    }
}
