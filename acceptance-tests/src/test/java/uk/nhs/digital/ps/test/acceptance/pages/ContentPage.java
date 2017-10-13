package uk.nhs.digital.ps.test.acceptance.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import uk.nhs.digital.ps.test.acceptance.models.Publication;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


public class ContentPage extends AbstractCmsPage {

    private final static String XPATH_CONTENT_TAB = "//div[contains(@class, 'tabbed-panel-layout-left')]//li[contains(@class, 'tab2')]";
    private final static String XPATH_EDITOR_BODY = "//div[contains(@class, 'hippo-editor-body')]";
    private final static String XPATH_TAXONOMY_PICKER =  "//div[contains(@class, 'wicket-modal') and contains(@aria-labelledby, 'Taxonomy picker')]";

    private PageHelper helper;

    public ContentPage(final WebDriverProvider webDriverProvider, final PageHelper helper) {
        super(webDriverProvider);
        this.helper = helper;
    }

    /* used as step assertion */
    public boolean openContentTab() {
        getWebDriver().findElement(By.xpath(XPATH_CONTENT_TAB)).click();

        return helper.waitForElementUntil(
            ExpectedConditions.attributeContains(
                By.xpath(XPATH_CONTENT_TAB),
                "class",
                "selected"
            )
        );
    }

    public boolean newPublication(final Publication publication) {
        WebElement menu = openPublicationsMenu(getWebDriver());
        return createPublication(menu, publication);
    }

    public boolean isPublicationEditScreenOpen() {
        return helper.findElement(
            By.xpath(XPATH_EDITOR_BODY)
        ) != null;
    }

    public void populatePublication(Publication publication) {
        populatePublicationTitle(publication.getTitle());

        helper.findElement(
            By.xpath(XPATH_EDITOR_BODY + "//div[contains(@class, 'publication-summary')]//textarea")
        ).sendKeys(
            publication.getSummary()
        );

        new Select(helper.findElement(
            By.xpath(XPATH_EDITOR_BODY + "//span[text()='Geographic Coverage']/../following-sibling::div//select[@class='dropdown-plugin']")
        )).selectByVisibleText(
            publication.getGeographicCoverage().getDisplayValue()
        );

        new Select(helper.findElement(
            By.xpath(XPATH_EDITOR_BODY + "//span[text()='Information Type']/../following-sibling::div//select[@class='dropdown-plugin']")
        )).selectByVisibleText(
            publication.getInformationType().getDisplayName()
        );

        new Select(helper.findElement(
            By.xpath(XPATH_EDITOR_BODY + "//span[text()='Granularity']/../following-sibling::div//select[@class='dropdown-plugin']")
        )).selectByVisibleText(
            publication.getGranularity().getDisplayValue()
        );

        helper.findElement(
            By.xpath(XPATH_EDITOR_BODY + "//span[text()='Select taxonomy terms']/../..")
        ).click();
        helper.findElement(
            By.xpath(XPATH_TAXONOMY_PICKER + "//span[text()='" + publication.getTaxonomy().getLevel1() + "']/..")
        ).click();
        helper.findElement(
            By.xpath(XPATH_TAXONOMY_PICKER + "//span[text()='" + publication.getTaxonomy().getLevel2() + "']/..")
        ).click();
        helper.findElement(
            By.xpath(XPATH_TAXONOMY_PICKER + "//span[text()='" + publication.getTaxonomy().getLevel3() + "']/..")
        ).click();
        helper.findElement(By.cssSelector("a[class~='category-add']")).click();

        findOk().click();

        getAttachmentsSection().uploadAttachments(publication.getAttachments());
    }

    public void populatePublicationTitle(final String publicationTitle) {
        helper.findElement(
            By.xpath(XPATH_EDITOR_BODY + "//div[contains(@class, 'publication-title')]//input")
        ).sendKeys(
            publicationTitle
        );
    }

    public AttachmentsSection getAttachmentsSection() {
        return new AttachmentsSection(helper, getWebDriver());
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
    }

    private WebElement openPublicationsMenu(WebDriver driver) {

        expandMenuByTitle("NHS Digital Publication System");
        expandMenuByTitle("publications");

        // Refresh reference to element because click above recreates it
        WebElement publicationsFolder = helper.findElement(By.cssSelector("a[class~='hippo-tree-node-link'][title='publications']"));
        WebElement publicationsParent = helper.findChildElement(publicationsFolder, By.xpath(".."));

        WebElement menuIcon = publicationsParent.findElement(By.xpath("following-sibling::a"));
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
        findOk().click();

        return isDocumentPresent(publication);
    }

    private WebElement expandMenuByTitle(String title) {
        getWebDriver().findElement(
            By.xpath("//a[contains(@class, 'hippo-tree-node-link') and @title='" + title + "']")
        ).click();

        return helper.waitForElementUntil(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[contains(@class, 'hippo-tree-node-link') and contains(@class, 'hippo-tree-node-expanded') and @title='" + title + "']")
            )
        );
    }

    private WebElement clickDropdownIconByTitle(String title) {
        WebElement icon = getWebDriver().findElement(
            By.xpath("//a[contains(@class, 'hippo-tree-node-link') and @title='" + title + "']/../../a[@class='hippo-tree-dropdown-icon-container']")
        );
        icon.click();

        return helper.waitForElementUntil(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath("//ul[contains(@class, 'hippo-toolbar-menu-item')]")
            )
        );
    }

    private boolean isDocumentPresent(final Publication publication) {
        return helper.waitForElementUntil(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath("//span[contains(@class, 'document') and @title='" + publication.getName() + "']")
            )
        ) != null;
    }

    public boolean isPublicationSaved(){
        // When a document is saved, a yellow status bar appears informing user document is either offline
        // or a previous version is live
        return helper.isElementPresent(By.className("hippo-toolbar-status"));
    }

    public void navigateToDocument(String documentName) {
        clickFolder("NHS Digital Publication System");
        clickFolder("publications");
        clickDocument(documentName);
    }

    public void discardUnsavedChanges(String publicationName) {
        WebElement closeButton = helper.findElement(
            By.xpath("//a[@class='hippo-tabs-documents-tab hippo-perspective-editperspective' and @title='" + publicationName + "']/following-sibling::a"));
        closeButton.click();
        WebElement confirmDiscard = helper.findElement(By.xpath("//input[@type='submit' and @value='Discard']"));
        confirmDiscard.click();
    }

    private void clickFolder(String name){
        helper.findElement(By.xpath("//span[@class='hippo-folder' and @title='" + name + "']")).click();
    }

    private void clickDocument(String name){
        helper.findElement(By.xpath("//span[@class='hippo-document' and @title='" + name + "']")).click();
    }

    private WebElement findPublicationMenu(){
        return helper.findElement(By.xpath("//span[text()='Publication']"));
    }

    private WebElement findPublish() {
        return helper.findElement(By.xpath("//span[text()='Publish']"));
    }

    private WebElement findOk() {
        return helper.findElement(By.xpath("//input[@value='OK' and @type='submit']"));
    }

    private WebElement findSaveAndClose() {
        return helper.findElement(By.xpath("//span[@title='Save & Close']"));
    }

    private WebElement findSave() {
        return helper.findElement(By.xpath("//span[@title='Save']"));
    }

    public List<String> getErrorMessages() {
        List<WebElement> errorElements = helper.findElements(By.xpath("//span[contains(@class, 'feedbackPanelERROR')]"));
        return errorElements.stream().map(WebElement::getText).collect(toList());
    }

    public String getFirstErrorMessage() {
        return getErrorMessages().get(0);
    }
}
