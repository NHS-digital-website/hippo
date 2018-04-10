package uk.nhs.digital.ps.test.acceptance.pages.widgets;

import static org.slf4j.LoggerFactory.getLogger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import uk.nhs.digital.ps.test.acceptance.models.Attachment;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;
import uk.nhs.digital.ps.test.acceptance.pages.XpathSelectors;

import java.util.List;

public class AttachmentsCmsWidget {

    private static final Logger log = getLogger(AttachmentsCmsWidget.class);

    private final PageHelper helper;
    private WebDriver webDriver;

    public AttachmentsCmsWidget(final PageHelper helper, final WebDriver webDriver) {
        this.helper = helper;
        this.webDriver = webDriver;
    }

    public void addUploadField() {
        helper.executeWhenStable(() -> findAddButton().click());
    }

    public void uploadAttachments(final List<Attachment> attachments) {

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
            for (int i = 0; i < attachments.size(); i++) {
                addUploadField();
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
            return helper.findOptionalChildElement(findRootElement(), By.linkText(attachment.getFullName())) != null;
        });
    }

    private List<WebElement> findFileUploadElements() {
        return findRootElement().findElements(By.cssSelector("input[type=file]"));
    }

    private WebElement findAddButton() {
        return helper.findChildElement(findRootElement(), By.linkText("Add"));
    }

    private WebElement findRootElement() {
        // find 'parent' div that _contains_ an 'h3/span' child elements, where the <span> has text 'Attachments'
        // this is so that further searches can be performed in context of the root element
        return getWebDriver().findElement(
            By.xpath(XpathSelectors.EDITOR_BODY + "//div[@class='hippo-editor-field' and h3/span[text() = 'Attachments']]"));
    }

    private WebDriver getWebDriver() {
        return webDriver;
    }
}
