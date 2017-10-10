package uk.nhs.digital.ps.test.acceptance.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ConsumableAttachmentElement {

    private final WebElement rootElement;

    public ConsumableAttachmentElement(final WebElement rootElement) {
        this.rootElement = rootElement;
    }

    public String getFullName() {
        return getHyperlink().getText();
    }

    public String getSizeText() {
        return rootElement.findElement(By.className("fileSize")).getText();
    }

    public void clickHyperlink() {
        getHyperlink().click();
    }

    private WebElement getHyperlink() {
        return rootElement.findElement(By.tagName("a"));
    }
}
