package uk.nhs.digital.ps.test.acceptance.pages.site;

import org.openqa.selenium.By;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

public class ServicePage extends AbstractSitePage {

    public ServicePage(WebDriverProvider webDriverProvider, String siteUrl) {
        super(webDriverProvider, siteUrl);
    }

    public String getPageTitle() {
        return getWebDriver().findElement(By.xpath("html/body/article/div/div[1]/h1")).getText();
    }

    public String findPageElement(String elementName) {
        if (elementName.toLowerCase().equals("summary")) {
            return getWebDriver().findElement(By.xpath(".//*[@id='section-summary']/p")).getText();
        } else if (elementName.toLowerCase().equals("top tasks")) {
            return getWebDriver().findElement(By.xpath("html/body/article/div/div[2]/div[2]/section[2]/div/p[1]")).getText();
        } else if (elementName.toLowerCase().equals("introduction")) {
            return getWebDriver().findElement(By.xpath("html/body/article/div/div[2]/div[2]/div/p")).getText();
        } else if (elementName.toLowerCase().equals("body section 1")) {
            return getWebDriver().findElement(By.xpath(".//*[@id='section-1']/h2")).getText();
        } else if (elementName.toLowerCase().equals("body section 2")) {
            return getWebDriver().findElement(By.xpath(".//*[@id='section-2']/h2")).getText();
        } else if (elementName.toLowerCase().equals("further information")) {
            return getWebDriver().findElement(By.xpath(".//*[@id='section-child-pages']/ol/li/article/h2/a")).getText();
        } else if (elementName.toLowerCase().equals("page contents")) {
            return getWebDriver().findElement(By.xpath("html/body/article/div/div[2]/div[1]/div/nav/ol/li[1]/a")).getText();
        } else {
            return "No element found!";
        }
    }
}
