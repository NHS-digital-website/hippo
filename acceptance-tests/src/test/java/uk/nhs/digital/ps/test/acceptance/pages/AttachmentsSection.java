package uk.nhs.digital.ps.test.acceptance.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import uk.nhs.digital.ps.test.acceptance.models.Attachment;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class AttachmentsSection {

    private final static Logger log = getLogger(AttachmentsSection.class);

    private final PageHelper helper;
    private WebDriver webDriver;

    AttachmentsSection(final PageHelper helper, final WebDriver webDriver) {
        this.helper = helper;
        this.webDriver = webDriver;
    }

    void uploadAttachments(final List<Attachment> attachments) {

        // Uploading a file via Selenium requires that the file is present on the disk.
        // Upload itself is triggered by the full path to the file being 'typed' into the input element.

        if (attachments != null) {
            // Create enough file upload fields for all attachments.
            //
            // HERE BE DRAGONS: it would provide for a better encapsulation if we were to iterate
            // through attachments and, for each attachment first add one upload field and upload
            // attachment and then progress to the next attachment. It has been tried, but, unfortunately,
            // this approach proved to be very unreliable and so, in the interest of time,
            // the approach of creating new fields in one go first was implemented as more robust.
            //
            // Starting at i == 1 to skip the first, pre-existing field.
            for (int i = 1; i < attachments.size(); i++) {
                helper.executeWhenStable(() -> findAddButton().click());
                final int expectedUploadFieldsCount = i + 1;
                helper.waitUntilTrue(() -> findFileUploadElements().size() == expectedUploadFieldsCount);
            }

            // Upload attachments one by one, using the newly created fields.
            for (int i = 0; i < attachments.size(); i++) {
                final Attachment attachment = attachments.get(i);

                uploadAttachment(i, attachment);
                waitForUploadToFinish(attachment);
            }
        }
    }

    public void uploadAttachment(final Attachment attachment) {
        uploadAttachment(0, attachment);
    }

    private void uploadAttachment(final int uploadFileFieldIndex, final Attachment attachment) {

        log.debug("Uploading attachment[{}] {} from {}", uploadFileFieldIndex, attachment.getFullName(),
            attachment.getPath().toAbsolutePath());

        helper.executeWhenStable(() -> {
            final List<WebElement> fileUploadElements = findFileUploadElements();
            final WebElement nextFileUploadElement = fileUploadElements.get(uploadFileFieldIndex);

            nextFileUploadElement.sendKeys(attachment.getPath().toAbsolutePath().toString());
        });
    }

    private void waitForUploadToFinish(final Attachment attachment) {
        // Uploads are handled asynchronously so we should detect whether it has completed and only
        // proceed further if it has.
        helper.waitUntilTrue(() -> {
            log.debug("Found attachment link: {}", attachment.getFullName());
            return findRootElement().findElement(By.linkText(attachment.getFullName())) != null;
        });
    }

    private List<WebElement> findFileUploadElements() {
        return findRootElement().findElements(By.cssSelector("input[type=file]"));
    }

    private WebElement findAddButton() {
        return helper.findElement(() -> findRootElement().findElement(By.linkText("Add")));
    }

    private WebElement findRootElement() {
        return getWebDriver().findElement(By.xpath(
            // find 'parent' div that _contains_ an 'h3/span' child elements, where the <span> has text 'Attachments'
            // this is so that further searches can be performed in context of the
            "//div[@class='hippo-editor-field' and h3[@class='hippo-editor-field-title']/span[text() = 'Attachments']]"
        ));
    }

    private WebDriver getWebDriver() {
        return webDriver;
    }
}
