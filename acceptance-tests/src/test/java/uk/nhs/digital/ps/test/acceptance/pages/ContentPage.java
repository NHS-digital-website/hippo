package uk.nhs.digital.ps.test.acceptance.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import uk.nhs.digital.ps.test.acceptance.models.*;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.*;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


public class ContentPage extends AbstractCmsPage {

    private static final String PUBLICATION = "publication";
    private static final String DATASET = "dataset";
    private static final String SERIES = "series";

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
        return createDocument(PUBLICATION, publication.getName());
    }

    public boolean newDataset(Dataset dataset) {
        return createDocument(DATASET, dataset.getName());
    }

    public boolean newSeries(PublicationSeries series) {
        return createDocument(SERIES, series.getName());
    }

    public boolean isDocumentEditScreenOpen() {
        return helper.findElement(
            By.xpath(XpathSelectors.EDITOR_BODY)
        ) != null;
    }

    public void populatePublication(Publication publication) {
        populateDocumentTitle(publication.getTitle());
        populateDocumentSummary(publication.getSummary());

        findPubliclyAccessibleRadioButton().select(publication.isPubliclyAccessible());

        populateDocumentNominalDate(publication.getNominalPublicationDate().asInstant());

        GeographicCoverage geographicCoverage = publication.getGeographicCoverage();
        if (geographicCoverage != null) {
            new Select(helper.findElement(
                By.xpath(XpathSelectors.EDITOR_BODY + "//span[text()='Geographic Coverage']/../following-sibling::div//select[@class='dropdown-plugin']")
            )).selectByVisibleText(
                geographicCoverage.getDisplayValue()
            );
        }

        InformationType informationType = publication.getInformationType();
        if (informationType != null) {
            getInformationTypeSection().addInformationTypeField();
            new Select(helper.findElement(
                By.xpath(XpathSelectors.EDITOR_BODY + "//span[text()='Information Type']/../following-sibling::div//select[@class='dropdown-plugin']")
            )).selectByVisibleText(
                informationType.getDisplayName()
            );
        }

        Granularity granularity = publication.getGranularity();
        if (granularity != null) {
            getGranularitySection().addGranularityField();
            getGranularitySection().populateGranularityField(granularity);
        }

        populateTaxonomy(publication);

        getAttachmentsWidget().uploadAttachments(publication.getAttachments());
    }

    public DateCmsWidget findNominalDateField() {
        return new DateCmsWidget(helper, "nominal-date");
    }

    private void populateTaxonomy(final Publication publication) {
        Taxonomy taxonomy = publication.getTaxonomy();
        if (taxonomy == null) {
            return;
        }

        helper.findElement(
            By.xpath(XpathSelectors.EDITOR_BODY + "//span[text()='Select taxonomy terms']/../../a")
        ).click();

        helper.findElement(
            By.xpath(XpathSelectors.TAXONOMY_PICKER + "//span[text()='" + taxonomy.getLevel1() + "']/..")
        ).click();
        helper.findElement(
            By.xpath(XpathSelectors.TAXONOMY_PICKER + "//span[text()='" + taxonomy.getLevel2() + "']/..")
        ).click();
        helper.findElement(
            By.xpath(XpathSelectors.TAXONOMY_PICKER + "//span[text()='" + taxonomy.getLevel3() + "']/..")
        ).click();
        helper.findElement(
            By.xpath(XpathSelectors.TAXONOMY_PICKER + "//a[normalize-space(text())='Add category']")
        ).click();

        clickButtonOnModalDialog("OK");
    }

    private PubliclyAccessibleCmsWidget findPubliclyAccessibleRadioButton() {
        return new PubliclyAccessibleCmsWidget(helper, XpathSelectors.EDITOR_BODY);
    }

    public void populateDocumentTitle(final String documentTitle) {
        findTitleElement().sendKeys(documentTitle);
    }

    public WebElement findTitleElement() {
        return helper.findElement(
            By.xpath(XpathSelectors.EDITOR_BODY + "//div[contains(@class, 'document-title')]//textarea")
        );
    }

    public void populateDocumentSummary(final String documentSummary) {
        findSummaryElement().sendKeys(documentSummary);
    }

    public void populateDocumentNominalDate(final Instant documentNominalDate) {
        findNominalDateField().populateWith(documentNominalDate);
    }

    public void populateDocumentNominalDate() {
        findNominalDateField().setToToday();
    }

    public WebElement findSummaryElement() {
        return helper.findElement(
            By.xpath(XpathSelectors.EDITOR_BODY + "//div[contains(@class, 'document-summary')]//textarea")
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

    public ResourceLinksCmsWidget getResourceLinksSection() {
        return new ResourceLinksCmsWidget(helper, getWebDriver());
    }

    public InformationTypeCmsWidget getInformationTypeSection() {
        return new InformationTypeCmsWidget(helper, getWebDriver());
    }

    public void saveDocument() {
        findSave().click();
    }

    public void saveAndCloseDocument() {
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

    private WebElement openDocumentMenu() {

        WebElement folderElement = navigateToFolder("Corporate Website", "Publication System");

        WebElement menuIcon = folderElement.findElement(By.xpath(".//a[contains(@class, 'hippo-tree-dropdown-icon-container')]"));
        helper.waitUntilVisible(menuIcon);
        menuIcon.click();

        WebElement menu = helper.findElement(By.cssSelector("ul[class~='hippo-toolbar-menu-item']"));
        return menu;
    }

    private boolean createDocument(String docType, String name) {

        WebElement menu = openDocumentMenu();

        // Find and click "Add new document" option
        WebElement menuItem = menu.findElement(By.cssSelector("span[title='Add new document...']"));
        menuItem.click();

        // Wait for modal dialogue and find new document name field
        WebElement nameField = helper.findElement(By.name("name-url:name"));
        nameField.sendKeys(name);

        // Choose document type
        WebElement documentTypeField = getWebDriver().findElement(By.name("prototype"));
        Select dropdown = new Select(documentTypeField);
        dropdown.selectByVisibleText(docType);

        // Confirm
        clickButtonOnModalDialog("OK");

        return isDocumentPresent(name);
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

    private boolean isDocumentPresent(String name) {
        return helper.waitForElementUntil(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath(XpathSelectors.NAVIGATION_CENTRE + "//span[contains(@class, 'document') and @title='" + name + "']")
            )
        ) != null;
    }

    public boolean isDocumentSaved(){
        // When a document is saved, a yellow status bar appears informing user document is either offline
        // or a previous version is live
        return helper.isElementPresent(By.className("hippo-toolbar-status"));
    }

    public void navigateToDocument(String documentName) {
        navigateToFolder("Corporate Website", "Publication System");
        clickDocument(documentName);
    }

    public void discardUnsavedChanges(String documentName) {
        WebElement closeButton = helper.findElement(
            By.xpath("//div[contains(@class, 'hippo-tabs-documents')]//a[contains(@class, 'hippo-tabs-documents-tab') and @title='" + documentName + "']/following-sibling::a[contains(@class, 'hippo-tabs-documents-close')]"));
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

    /**
     * Sometimes when send a click, it just highlights the button but doesn't click it..
     * The second time always seems to work so only try twice
     */
    private void clickButtonOnModalDialog(String buttonText) {
        try {
            clickButtonOnModalDialogOnce(buttonText);
        } catch (TimeoutException e) {
            clickButtonOnModalDialogOnce(buttonText);
        }
    }
    private void clickButtonOnModalDialogOnce(String buttonText) {
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

    public void populateDataset(Dataset dataset) {
        populateDocumentTitle(dataset.getTitle());
        populateDocumentSummary(dataset.getName());
        populateDocumentNominalDate(dataset.getNominalDate());
    }

    public void populateSeries(PublicationSeries series) {
        populateDocumentTitle(series.getTitle());
        populateDocumentSummary(series.getSummary());
    }
}
