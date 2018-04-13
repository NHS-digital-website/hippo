package uk.nhs.digital.ps.test.acceptance.data;

import static java.util.Collections.synchronizedList;
import static java.util.Collections.unmodifiableCollection;

import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Collection;

public class WebDriversRepo {

    private Collection<WebDriver> webDrivers = synchronizedList(new ArrayList<>());

    public void addWebDriver(final WebDriver webDriver) {
        this.webDrivers.add(webDriver);
    }

    public Collection<WebDriver> getAll() {
        return unmodifiableCollection(webDrivers);
    }

    public int size() {
        return webDrivers.size();
    }
}
