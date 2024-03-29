import * as playwright from 'playwright';

const viewports = {
    mobile: {
        width: 380,
        height: 800,
    },
    tablet: {
        width: 800,
        height: 1200,
    },
    desktop: {
        width: 1400,
        height: 1050,
    }
};
export type Viewport = keyof typeof viewports;

export default class BrowserDelegate {
    public jsEnabled?: boolean
    public viewport: Viewport = 'desktop';

    private browser?: playwright.Browser;
    private context?: playwright.BrowserContext;
    private page?: playwright.Page;

    async init(browser?: 'chrome' | 'firefox') {
        await this.destroy();

        if (browser == 'firefox') {
            this.browser = await playwright.firefox.launch();
        } else {
            this.browser = await playwright.chromium.launch();
        }
        return this.browser;
    }

    async destroy() {
        if (this.browser) await this.browser.close();
    }

    async newContext() {
        let browser = this.browser;
        if (!browser) browser = await this.init();

        this.context = await browser.newContext({
            viewport: viewports[this.viewport],
            javaScriptEnabled: this.jsEnabled
        });
        return this.context;
    }

    async screenshot() {
        const page = await this.getPage();
        return await page.screenshot({fullPage: true, type: "jpeg"});
    }

    async newPage() {
        let context = this.context;
        if (!context) context = await this.newContext();

        this.page = await context.newPage();
        return this.page;
    }

    async getPage() {
        if (this.page) return this.page;
        return await this.newPage();
    }

    async openUrl(url: string, jsenabled: boolean = true) {
        this.jsEnabled = jsenabled;
        const page = await this.getPage();
        const res = await page.goto(url);
        await page.waitForLoadState('networkidle');
        return res;
    }

    async switchViewport(viewport: Viewport) {
        const page = await this.getPage();
        await page.setViewportSize(viewports[viewport]);
        this.viewport = viewport;
    }

    async timeout(timeout: number) {
        await this.page.waitForTimeout(timeout);
    }

    async loadScript(url: string) {
        await this.page.addScriptTag({url})
    }
}
