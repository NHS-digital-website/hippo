package uk.nhs.digital.ps.test.acceptance.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class PubliclyAccessibleRatioButton {

    private final PageHelper helper;

    public PubliclyAccessibleRatioButton(final PageHelper helper) {
        this.helper = helper;
    }

    private WebElement findLiveOption() {
        return findRadioButtonByLabelText("LIVE (Full content publicly accessible)");
    }

    private WebElement findUpcomingOption() {
        return findRadioButtonByLabelText("UPCOMING (Show in upcoming publications list only)");
    }

    public void select(final boolean publiclyAccessible) {
        WebElement option = publiclyAccessible ? findLiveOption() : findUpcomingOption();

        option.click();
    }

    private WebElement findRadioButtonByLabelText(final String labelText) {
        return helper.findElement(By.xpath("//input[@type='radio' and following-sibling::label[text() = '" + labelText + "']]"));
    }

}
