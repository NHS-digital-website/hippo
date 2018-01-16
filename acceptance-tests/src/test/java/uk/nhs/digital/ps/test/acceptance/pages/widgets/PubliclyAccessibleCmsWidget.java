package uk.nhs.digital.ps.test.acceptance.pages.widgets;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;

public class PubliclyAccessibleCmsWidget {

    private final PageHelper helper;
    private final String editorBodyXpath;

    public PubliclyAccessibleCmsWidget(final PageHelper helper, final String editorBodyXpath) {
        this.helper = helper;
        this.editorBodyXpath = editorBodyXpath;
    }

    private WebElement findLiveOption() {
        return findRadioButtonByLabelText("FINALISED (Full content publicly accessible)");
    }

    private WebElement findUpcomingOption() {
        return findRadioButtonByLabelText("UPCOMING (Show in upcoming publications list only)");
    }

    public void select(final boolean publiclyAccessible) {
        WebElement option = publiclyAccessible ? findLiveOption() : findUpcomingOption();

        option.click();
    }

    private WebElement findRadioButtonByLabelText(final String labelText) {
        return helper.findElement(By.xpath(editorBodyXpath + "//input[@type='radio' and following-sibling::label[text() = '" + labelText + "']]"));
    }

}
