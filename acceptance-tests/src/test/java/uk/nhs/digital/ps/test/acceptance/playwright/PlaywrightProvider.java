package uk.nhs.digital.ps.test.acceptance.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class PlaywrightProvider {
    private final Page page;

    public PlaywrightProvider() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.firefox().launch();

        BrowserContext context = browser.newContext();
        page = context.newPage();
    }

    public Page getPage() {
        return page;
    }
}
