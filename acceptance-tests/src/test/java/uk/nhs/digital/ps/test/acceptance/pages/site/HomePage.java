package uk.nhs.digital.ps.test.acceptance.pages.site;

import org.openqa.selenium.By;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

public class HomePage extends AbstractSitePage {

    public HomePage(WebDriverProvider webDriverProvider, String siteUrl) {
        super(webDriverProvider, siteUrl);
    }

    public String getPageTitle() {
        return getWebDriver().getTitle();
    }

    public String findPageElement(String elementName) {
        if (elementName.toLowerCase().equals("top label")) {
            return getWebDriver().findElement(By.xpath("html/body/header/div/div/section/div/p")).getText();
        } else if (elementName.toLowerCase().equals("search")) {
            return getWebDriver().findElement(By.xpath("html/body/header/div/div/section/div/form/div[2]/button")).getText();
        } else if (elementName.toLowerCase().equals("data and information")) {
            return getWebDriver().findElement(By.xpath("html/body/main/div/div/div[1]/div[1]/h2")).getText();
        } else if (elementName.toLowerCase().equals("systems and services")) {
            return getWebDriver().findElement(By.xpath("html/body/main/div/div/div[2]/div[1]/h2")).getText();
        } else if (elementName.toLowerCase().equals("find data and information")) {
            return getWebDriver().findElement(By.xpath("html/body/main/div/div/div[1]/div[2]/div/div[1]/div/div/div[1]/div/h3")).getText();
        } else if (elementName.toLowerCase().equals("indicator library")) {
            return getWebDriver().findElement(By.xpath("html/body/main/div/div/div[1]/div[2]/div/div[1]/div/div/div[2]/div/h3")).getText();
        } else if (elementName.toLowerCase().equals("data and information summary")) {
            return getWebDriver().findElement(By.xpath("html/body/main/div/div/div[1]/div[2]/div/div[1]/div/div/div[1]/div/p")).getText();
        } else if (elementName.toLowerCase().equals("see all data and publications")) {
            return getWebDriver().findElement(By.xpath("html/body/main/div/div/div[1]/div[2]/div/div[1]/div/div/div[1]/div/a")).getText();
        } else if (elementName.toLowerCase().equals("view indicator library")) {
            return getWebDriver().findElement(By.xpath("html/body/main/div/div/div[1]/div[2]/div/div[1]/div/div/div[2]/div/a")).getText();
        } else if (elementName.toLowerCase().equals("latest publications")) {
            return getWebDriver().findElement(By.xpath("html/body/main/div/div/div[1]/div[2]/div/div[2]/div/div[1]/div/h3")).getText();
        } else if (elementName.toLowerCase().equals("systems and services")) {
            return getWebDriver().findElement(By.xpath("html/body/main/div/div/div[3]/div[1]/h2")).getText();
        } else if (elementName.toLowerCase().equals("systems and services summary")) {
            return getWebDriver().findElement(By.xpath("html/body/main/div/div/div[2]/div[1]/p")).getText();
        } else if (elementName.toLowerCase().equals("most popular services")) {
            return getWebDriver().findElement(By.xpath("html/body/main/div/div/div[2]/div[2]/div/div/div[1]/div[1]/div/h3")).getText();
        } else if (elementName.toLowerCase().equals("view all services")) {
            return getWebDriver().findElement(By.xpath("html/body/main/div/div/div[2]/div[2]/div/div/div[2]/div/a")).getText();
        } else if (elementName.toLowerCase().equals("latest news")) {
            return getWebDriver().findElement(By.xpath("html/body/main/div/div/div[3]/div[2]/div/div[2]/div/div/h3")).getText();
        } else if (elementName.toLowerCase().equals("view all news")) {
            return getWebDriver().findElement(By.xpath("html/body/main/div/div/div[3]/div[2]/div/div[2]/div/div/a")).getText();
        } else if (elementName.toLowerCase().equals("upcoming events")) {
            return getWebDriver().findElement(By.xpath("html/body/main/div/div/div[3]/div[2]/div/div[3]/div/div/h3")).getText();
        } else if (elementName.toLowerCase().equals("view all events")) {
            return getWebDriver().findElement(By.xpath("html/body/main/div/div/div[3]/div[2]/div/div[3]/div/div/a")).getText();
        } else if (elementName.toLowerCase().equals("how we look after your information")) {
            return getWebDriver().findElement(By.xpath("html/body/main/div/div/div[4]/div/div/div/div/div[1]/div/h3")).getText();
        } else if (elementName.toLowerCase().equals("find out how we look after your information")) {
            return getWebDriver().findElement(By.xpath("html/body/main/div/div/div[4]/div/div/div/div/div[2]/div[1]/div/a")).getText();
        } else if (elementName.toLowerCase().equals("about us")) {
            return getWebDriver().findElement(By.xpath("html/body/main/div/div/div[3]/div[1]/h2")).getText();
        } else {
            return "No element found! or please check whether expected sections are with uppercases";
        }
    }
}
