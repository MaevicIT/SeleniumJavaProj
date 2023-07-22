package stidev.qa.tester.model;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;


public class Tester {

    private Browsers browser;
    private Tests test;
    private WebDriver driver;

    public void setBrowser(Browsers browser) {
        this.browser = browser;
    }

    public String getBrowserDesc() {
        return switch (browser) {
            case CHROME -> "Chrome";
            case SAFARI -> "Safari";
            case EDGE -> "Edge";
            case FIREFOX -> "FireFox";
        };
    }

    public void setTest(Tests test) {
        this.test = test;
    }

    public String getTestDesc() {
        return switch (test) {
            case LOGIN -> "Login";
            case SIGNOUT -> "Sign out";
            case IMAGES -> "Images";
            case SITE_NAV -> "Site Nav";
        };
    }

    public void tester() {
        TestRunner run = new TestRunner();
        switch (browser) {
            case CHROME -> driver = new ChromeDriver();
            case SAFARI -> driver = new SafariDriver();
            case FIREFOX -> driver = new FirefoxDriver();
            case EDGE -> driver = new EdgeDriver();

            // Handle other browsers here if necessary
        }
        System.out.println("Running " + this.browser + ", With driver - " + driver);
        run.runTest(browser, driver, test);
    }
}

