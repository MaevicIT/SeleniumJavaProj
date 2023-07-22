package stidev.qa.tester.model;
import org.openqa.selenium.WebDriver;
import stidev.qa.tester.qatests.ChromeTests;
import stidev.qa.tester.qatests.EdgeTests;
import stidev.qa.tester.qatests.SafariTests;
import stidev.qa.tester.qatests.FirefoxTests;


public class TestRunner extends Tester {
    private Browsers browser;
    private Tests test;
    private WebDriver driver;

    public void runTest(Browsers browser, WebDriver driver, Tests test){
        try {
            switch (browser) {
                case CHROME:
                    System.setProperty("webdriver.chrome.driver", "/Users/mattraper/Desktop/Selenium Test Projects/chromedriver_mac64/chromedriver");
                    this.driver = driver;
                    ChromeTests chtest = new ChromeTests(driver, test);
                    chtest.runTest();
                    break;
                case SAFARI:
                    this.driver = driver;
                    SafariTests saftest = new SafariTests(driver, test);
                    saftest.runTest();
                    break;
                case FIREFOX:
                    System.setProperty("webdriver.firefox.driver", "/Users/mattraper/Desktop/Selenium Test Projects/geckodriver_mac64/geckodriver");
                    this.driver = driver;
                    FirefoxTests fftest = new FirefoxTests(driver, test);
                    fftest.runTest();
                    break;
                case EDGE:
                    System.setProperty("webdriver.edge.driver", "/Users/mattraper/Desktop/Selenium Test Projects/edgedriver_mac64/msedgedriver");
                    this.driver = driver;
                    EdgeTests edtest = new EdgeTests(driver, test);
                    edtest.runTest();
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
