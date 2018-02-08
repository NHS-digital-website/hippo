package uk.nhs.digital.ps.test.acceptance.pages.widgets;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;
import uk.nhs.digital.ps.test.acceptance.pages.XpathSelectors;

import java.time.Instant;

import static uk.nhs.digital.ps.test.acceptance.util.FormatHelper.formatInstant;

public class DateCmsWidget {

    private static final String CMS_DATE_PATTERN = "d/M/yyyy";

    private final PageHelper helper;
    private final String dateFieldCssClass;


    public DateCmsWidget(final PageHelper helper, final String dateFieldCssClass) {
        this.helper = helper;
        this.dateFieldCssClass = dateFieldCssClass;
    }

    private WebElement find() {
        return findDateFieldByCssClass(dateFieldCssClass);
    }

    public void populateWith(final Instant instant) {
        find().sendKeys(formatInstant(instant, CMS_DATE_PATTERN));
    }

    private WebElement findDateFieldByCssClass(final String dateFieldClass) {
        return helper.findElement(By.xpath(XpathSelectors.EDITOR_BODY
            + "//div[contains(@class, '" + dateFieldClass + "')]//input[@type='text' and @class='date']"
        ));
    }

    public void setToToday() {
        WebElement resetButton = find().findElement(By.xpath("//a[contains(@class, 'hippo-datepicker-reset')]"));
        resetButton.click();
    }

    public void clear() {
        find().clear();
    }

}
