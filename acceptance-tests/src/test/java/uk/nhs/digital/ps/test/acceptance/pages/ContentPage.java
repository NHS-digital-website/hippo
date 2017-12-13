package uk.nhs.digital.ps.test.acceptance.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import uk.nhs.digital.ps.test.acceptance.models.Publication;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.*;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


public class ContentPage extends AbstractCmsPage {

    private PageHelper helper;

    public ContentPage(final WebDriverProvider webDriverProvider, final PageHelper helper) {
        super(webDriverProvider);
        this.helper = helper;
    }

    /* used as step assertion */
    public boolean openContentTab() {
        getWebDriver().findElement(
            By.xpath(XpathSelectors.TABBED_PANEL + "//li[contains(@class, 'tab2')]")).click();

        return helper.waitForElementUntil(
            ExpectedConditions.attributeContains(
                By.xpath(XpathSelectors.TABBED_PANEL + "//li[contains(@class, 'tab2')]"), "class", "selected"));
    }

    public boolean newPublication(final Publication publication) {
        WebElement menu = openPublicationsMenu(getWebDriver());
        return createPublication(menu, publication);
    }

    public boolean isPublicationEditScreenOpen() {
        return helper.findElement(
            By.xpath(XpathSelectors.EDITOR_BODY)
        ) != null;
    }

    public void populatePublication(Publication publication) {
        populatePublicationTitle(publication.getTitle());
        populatePublicationSummary(publication.getSummary());

        findPubliclyAccessibleRadioButton().select(publication.isPubliclyAccessible());

        findNominalDateField().populateWith(publication.getNominalPublicationDate().asInstant());

        new Select(helper.findElement(
            By.xpath(XpathSelectors.EDITOR_BODY + "//span[text()='Geographic Coverage']/../following-sibling::div//select[@class='dropdown-plugin']")
        )).selectByVisibleText(
            publication.getGeographicCoverage().getDisplayValue()
        );

        getInformationTypeSection().addInformationTypeField();
        new Select(helper.findElement(
            By.xpath(XpathSelectors.EDITOR_BODY + "//span[text()='Information Type']/../following-sibling::div//select[@class='dropdown-plugin']")
        )).selectByVisibleText(
            publication.getInformationType().getDisplayName()
        );

        getGranularitySection().addGranularityField();
        getGranularitySection().populateGranularityField(publication.getGranularity());

        populateTaxonomy(publication);

        getAttachmentsWidget().uploadAttachments(publication.getAttachments());
    }

    private DateCmsWidget findNominalDateField() {
        return new DateCmsWidget(helper, "nominal-date");
    }

    private void populateTaxonomy(final Publication publication) {
        helper.findElement(
            By.xpath(XpathSelectors.EDITOR_BODY + "//span[text()='Select taxonomy terms']/../../a")
        ).click();

        helper.findElement(
            By.xpath(XpathSelectors.TAXONOMY_PICKER + "//span[text()='" + publication.getTaxonomy().getLevel1() + "']/..")
        ).click();
        helper.findElement(
            By.xpath(XpathSelectors.TAXONOMY_PICKER + "//span[text()='" + publication.getTaxonomy().getLevel2() + "']/..")
        ).click();
        helper.findElement(
            By.xpath(XpathSelectors.TAXONOMY_PICKER + "//span[text()='" + publication.getTaxonomy().getLevel3() + "']/..")
        ).click();
        helper.findElement(
            By.xpath(XpathSelectors.TAXONOMY_PICKER + "//a[normalize-space(text())='Add category']")
        ).click();

        clickButtonOnModalDialog("OK");
    }

    private PubliclyAccessibleCmsWidget findPubliclyAccessibleRadioButton() {
        return new PubliclyAccessibleCmsWidget(helper, XpathSelectors.EDITOR_BODY);
    }

    public void populatePublicationTitle(final String publicationTitle) {
        helper.findElement(
            By.xpath(XpathSelectors.EDITOR_BODY + "//div[contains(@class, 'publication-title')]//input")
        ).sendKeys(
            publicationTitle
        );
    }

    public void populatePublicationSummary(final String publicationSummary) {
        helper.findElement(
            By.xpath(XpathSelectors.EDITOR_BODY + "//div[contains(@class, 'publication-summary')]//textarea")
        ).sendKeys(
            publicationSummary
        );
    }

    public AttachmentsCmsWidget getAttachmentsWidget() {
        return new AttachmentsCmsWidget(helper, getWebDriver());
    }

    public GranularityCmsWidget getGranularitySection() {
        return new GranularityCmsWidget(helper, getWebDriver());
    }

    public RelatedLinksCmsWidget getRelatedLinksSection() {
        return new RelatedLinksCmsWidget(helper, getWebDriver());
    }

    public InformationTypeCmsWidget getInformationTypeSection() {
        return new InformationTypeCmsWidget(helper, getWebDriver());
    }

    public void savePublication() {
        findSave().click();
    }

    public void saveAndClosePublication() {
        findSaveAndClose().click();
    }

    public void publish(){
        findPublicationMenu().click();
        findPublish().click();

        waitUntilPublished();
    }

    public void unpublishDocument(final String documentName) {
        navigateToDocument(documentName);

        findPublicationMenu().click();
        findTakeOffline().click();

        clickButtonOnModalDialog("OK");
    }

    private void waitUntilPublished() {

        // Saving and closing a document leaves one of the following disclaimers. Hitting 'Publish' makes them
        // disappear. We treat this disappearance as an indicator that the publishing has completed.
        Stream.of(
            "Offline",                   // created after closing previously unpublished document
            "A previous version is live" // created after closing previously published document
        ).forEach(title ->
            // Using 'findElements' as this is the method recommended for asserting absence of elements
            // (see JavaDoc for org.openqa.selenium.WebDriver.findElement).
            helper.waitUntilTrue(() -> getWebDriver().findElements(
                By.xpath(XpathSelectors.EDITOR_BODY + "//span[@title='" + title + "']")
            ).isEmpty()
        ));
    }

    private WebElement openPublicationsMenu(WebDriver driver) {

        WebElement folderElement = navigateToFolder("Corporate Website", "Publication System");

        WebElement menuIcon = folderElement.findElement(By.xpath(".//a[contains(@class, 'hippo-tree-dropdown-icon-container')]"));
        helper.waitUntilVisible(menuIcon);
        menuIcon.click();

        WebElement menu = helper.findElement(By.cssSelector("ul[class~='hippo-toolbar-menu-item']"));
        return menu;
    }

    private boolean createPublication(final WebElement menu, final Publication publication) {
        // Find and click "Add new document" option
        WebElement menuItem = menu.findElement(By.cssSelector("span[title='Add new document...']"));
        menuItem.click();

        // Wait for modal dialogue and find new document name field
        WebElement nameField = helper.findElement(By.name("name-url:name"));
        nameField.sendKeys(publication.getPublicationUrlName());

        // Choose document type
        WebElement documentTypeField = getWebDriver().findElement(By.name("prototype"));
        Select dropdown = new Select(documentTypeField);
        dropdown.selectByVisibleText("publication");

        // Confirm
        clickButtonOnModalDialog("OK");

        return isDocumentPresent(publication);
    }

    private WebElement navigateToFolder(String... folders) {
        String xpathSelector = XpathSelectors.NAVIGATION_LEFT + "//div[@class='wicket-tree']/div[1]";

        for (String folder : folders) {
            xpathSelector += "/following-sibling::div//a[contains(@class, 'hippo-tree-node-link') and @title='" + folder + "']";

            getWebDriver().findElement(By.xpath(xpathSelector)).click();
            helper.waitForElementUntil(
                ExpectedConditions.attributeContains(
                    By.xpath(xpathSelector), "class", "hippo-tree-node-expanded"));

            // select root again
            xpathSelector += "/ancestor::*[contains(@class, 'row')][1]";
        }

        return helper.findElement(By.xpath(xpathSelector));
    }

    private boolean isDocumentPresent(final Publication publication) {
        return helper.waitForElementUntil(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath(XpathSelectors.NAVIGATION_CENTRE + "//span[contains(@class, 'document') and @title='" + publication.getName() + "']")
            )
        ) != null;
    }

    public boolean isPublicationSaved(){
        // When a document is saved, a yellow status bar appears informing user document is either offline
        // or a previous version is live
        return helper.isElementPresent(By.className("hippo-toolbar-status"));
    }

    public void navigateToDocument(String documentName) {
        navigateToFolder("Corporate Website", "Publication System");
        clickDocument(documentName);
    }

    public void discardUnsavedChanges(String publicationName) {
        WebElement closeButton = helper.findElement(
            By.xpath("//div[contains(@class, 'hippo-tabs-documents')]//a[contains(@class, 'hippo-tabs-documents-tab') and @title='" + publicationName + "']/following-sibling::a[contains(@class, 'hippo-tabs-documents-close')]"));
        closeButton.click();
        clickButtonOnModalDialog("Discard");
    }

    private void clickDocument(String name){
        helper.findElement(By.xpath("//span[@class='hippo-document' and @title='" + name + "']")).click();
    }

    private WebElement findPublicationMenu(){
        return helper.findElement(
            By.xpath(XpathSelectors.EDITOR_BODY + "//span[text()='Publication']"));
    }

    private WebElement findPublish() {
        return helper.findElement(
            By.xpath(XpathSelectors.EDITOR_BODY + "//span[text()='Publish']"));
    }

    private WebElement findTakeOffline() {
        return helper.findElement(
            By.xpath(XpathSelectors.EDITOR_BODY + "//span[text()='Take offline...']"));
    }

    private void clickButtonOnModalDialog(String buttonText) {
        helper.findElement(
            By.xpath("//div[contains(@class, 'wicket-modal')]//input[@type='submit' and @value='"+ buttonText +"']"))
            .click();

        helper.waitForElementUntil(ExpectedConditions.invisibilityOfElementLocated(
            By.xpath("//div[contains(@class, 'wicket-modal')]")));

    }

    private WebElement findSaveAndClose() {
        return helper.findElement(
            By.xpath(XpathSelectors.EDITOR_BODY + "//span[@title='Save & Close']"));
    }

    private WebElement findSave() {
        return helper.findElement(
            By.xpath(XpathSelectors.EDITOR_BODY + "//span[@title='Save']"));
    }

    public List<String> getErrorMessages() {
        List<WebElement> errorElements = helper.findElements(By.xpath("//span[contains(@class, 'feedbackPanelERROR')]"));
        return errorElements.stream().map(WebElement::getText).collect(toList());
    }

    public String getFirstErrorMessage() {
        return getErrorMessages().get(0);
    }
}
