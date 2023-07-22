package stidev.qa.tester;

import stidev.qa.tester.controller.TesterController;
import stidev.qa.tester.model.Tester;
import stidev.qa.tester.view.TesterView;

public class Main {
    public static void main(String[] args) {

        Tester tester = new Tester();

        TesterView view = new TesterView();

        TesterController controller = new TesterController(tester, view);

        view.setVisible(true);

        System.out.println("Generated view.");

    }
        /*
        //Setup drivers
        System.setProperty("webdriver.chrome.driver", "/Users/mattraper/Desktop/Selenium Test Projects/chromedriver_mac64/chromedriver");=
        WebDriver driver = new ChromeDriver();

        //Open Webpage, maximize window
        driver.get("https://raper-dev-www.stifirestop.com");
        driver.manage().window().maximize();

        //Perform testing actions
    */
}