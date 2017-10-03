package uk.nhs.digital.ps.test.acceptance.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.nhs.digital.ps.test.acceptance.models.Publication;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

import static java.util.concurrent.TimeUnit.MILLISECONDS;


public class ContentPage extends AbstractPage {

    private PageHelper helper;

    public ContentPage(final WebDriverProvider webDriverProvider, final PageHelper helper) {
        super(webDriverProvider);
        this.helper = helper;
    }

    public boolean openContentTab() {
        WebElement contentTab = helper.findElement(By.className("tab2"));
        contentTab.click();

        // Refresh reference to element because click above recreates it
        WebDriverWait wait = new WebDriverWait(getWebDriver(), 5);
        return wait.pollingEvery(100, MILLISECONDS).until((WebDriver driver) -> {
            return isOpen(helper.findNewElement(contentTab, By.className("tab2")));
        });
    }

    public boolean newPublication(final String documentName) {
        WebElement menu = openPublicationsMenu(getWebDriver());
        return createPublication(menu, documentName);
    }

    public boolean isPublicationEditScreenOpen(){
        return helper.isElementPresent(By.className("hippo-editor-body"));
    }

    public void populatePublication(Publication publication) {
        WebElement editorBody = helper.findElement(By.className("hippo-editor-body"));
        WebElement titleField = editorBody.findElement(By.className("publication-title")).findElement(By.tagName("input"));
        titleField.sendKeys(publication.getPublicationTitle());

        WebElement summaryField = editorBody.findElement(By.className("publication-summary")).findElement(By.tagName("textarea"));
        summaryField.sendKeys(publication.getPublicationSummary());
    }

    public void savePublication() {
        findSaveAndClose().click();
    }

    public void publish(){
        findPublicationMenu().click();
        findPublish().click();
    }

    private boolean isOpen(WebElement contentTab) {
        return contentTab.getAttribute("class").contains("selected");
    }

    private WebElement openPublicationsMenu(WebDriver driver) {
        WebElement rootTitle = driver.findElement(By.cssSelector("a[class~='hippo-tree-node-link'][title='NHS Digital Publication System']"));
        WebElement rootExpander = rootTitle.findElement(By.xpath("preceding-sibling::a"));
        rootExpander.click();

        WebElement publicationsFolder = helper.findElement(By.cssSelector("a[class~='hippo-tree-node-link'][title='publications']") );
        publicationsFolder.click();

        // Refresh reference to element because click above recreates it
        publicationsFolder = helper.findNewElement(publicationsFolder, By.cssSelector("a[class~='hippo-tree-node-link'][title='publications']") );
        WebElement publicationsParent = helper.findChildElement(publicationsFolder, By.xpath(".."));

        WebElement menuIcon = publicationsParent.findElement(By.xpath("following-sibling::a"));
        helper.waitUntilVisible(menuIcon);
        menuIcon.click();

        WebElement menu = helper.findElement(By.cssSelector("ul[class~='hippo-toolbar-menu-item']"));
        return menu;
    }

    private boolean createPublication(WebElement menu, String title) {
        // Find and click "Add new document" option
        WebElement menuItem = menu.findElement(By.cssSelector("span[title='Add new document...']"));
        menuItem.click();

        // Wait for modal dialogue and find new document name field
        WebElement nameField = helper.findElement(By.name("name-url:name"));
        nameField.sendKeys(title);

        // Choose document type
        WebElement documentTypeField = getWebDriver().findElement(By.name("prototype"));
        Select dropdown = new Select(documentTypeField);
        dropdown.selectByVisibleText("publication");

        // Confirm
        findOk().click();

        boolean documentWasAdded = helper.isElementPresent(By.xpath("//span[@class='hippo-folder' or @class='hippo-document' and @title='" + title + "']"));
        return documentWasAdded;
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

    public void discardUnsavedPublication(String publicationName) {
        WebElement closeButton = helper.findElement(
            By.xpath("//a[@class='hippo-tabs-documents-tab hippo-perspective-editperspective' and @title='" + publicationName + "']/following-sibling::a"));
        closeButton.click();
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

    private WebElement findTakeOffline() {
        return helper.findElement(By.xpath("//span[text()='Take offline...']"));
    }

    private WebElement findOk() {
        return helper.findElement(By.xpath("//input[@value='OK' and @type='submit']"));
    }

    private WebElement findSaveAndClose() {
        return helper.findElement(By.xpath("//span[@title='Save & Close']"));
    }

    private WebElement findDocumentMenu(){
        return helper.findElement(By.xpath("//span[text()='Document']"));
    }

    private WebElement findDelete() {
        return helper.findElement(By.xpath("//span[text()='Delete...']"));
    }

    private WebElement findEdit() {
        return helper.findElement(By.xpath("//input[@name='name-url:url']/following-sibling::a"));
    }

    private WebElement findUrlName() {
        return helper.findElement(By.xpath("//input[@name='name-url:url']"));
    }

}
