package uk.nhs.digital.ps.test.acceptance.pages.widgets;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;
import uk.nhs.digital.ps.test.acceptance.pages.XpathSelectors;

public class RelatedLinksCmsWidget {

    private final PageHelper helper;

    public RelatedLinksCmsWidget(final PageHelper helper, final WebDriver webDriver) {
        this.helper = helper;
    }

    public void addRelatedLinkField() {
        helper.executeWhenStable(() -> findAddButton().click());
    }

    private WebElement findRootElement() {
        return helper.findElement(
            By.xpath(XpathSelectors.EDITOR_BODY + "//div[@class='hippo-editor-field' and h3/span[text() = 'Related Links']]"));
    }

    private WebElement findAddButton() {
        return helper.findChildElement(findRootElement(), By.linkText("Add"));
    }
}
