package stidev.qa.tester.qatests;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import stidev.qa.tester.model.TestRunner;
import stidev.qa.tester.model.Tests;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class EdgeTests extends TestRunner {
    private WebDriver driver;
    private Tests test;

    public EdgeTests(WebDriver driver, Tests test) {
        this.driver = driver;
        this.test = test;
    }

    public void runTest() {
        try {
            switch (test) {
                case LOGIN -> runLoginTest("https://raper-dev-api.stifirestop.com");
                case SIGNOUT -> runSignoutTest("https://raper-dev-www.stifirestop.com");
                case IMAGES -> runImagesTest("https://raper-dev-www.stifirestop.com/about");
                case SITE_NAV -> runSiteNavTest("https://raper-dev-www.stifirestop.com");
            }
        } catch (NoSuchSessionException e) {
            System.err.println("WebDriver session not found. Re-initializing WebDriver.");
            restartTest();
        }
    }

    private void restartTest() {
        driver.quit();
        driver = new EdgeDriver();
        runTest();
    }

    private void runLoginTest(String url) {
        // Login test logic
    }

    private void runSignoutTest(String url) {
        // Signout test logic
    }

    private void runImagesTest(String url) {
        driver.get(url);
        driver.manage().window().maximize();
        scrollToBottom(driver);
        System.out.println("browser opened...");

        // Wait for the images to be present on the page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        //wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("img")));

        // Find all the image elements on the page
        List<WebElement> imageElements = driver.findElements(By.tagName("img"));

        System.out.println("Running image inspection...");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("image_inspection_results_Edge.txt"))) {
            // Iterate over each image element to inspect its attributes
            for (int i = 0; i < imageElements.size(); i++) {
                WebElement imageElement = imageElements.get(i);
                String imageURL = imageElement.getAttribute("src");
                String imageAltText = imageElement.getAttribute("alt");

                // Check if the image loaded properly by checking its naturalWidth and naturalHeight properties
                boolean isImageLoaded = false;
                try {
                    Object result = ((JavascriptExecutor) driver).executeScript(
                            "return arguments[0].complete && typeof arguments[0].naturalWidth !== 'undefined' && arguments[0].naturalWidth > 0 && arguments[0].naturalHeight > 0;",
                            imageElement);

                    if (result instanceof Boolean) {
                        isImageLoaded = (Boolean) result;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Log image information
                String logMessage = "Image " + (i + 1) + ", \nURL: " + imageURL;
                logMessage += imageAltText.isEmpty() ? ", \n" : ", \nAlt Text: " + imageAltText;
                logMessage += isImageLoaded ? ", \nLoaded: Yes" : ", \nLoaded: No";

                // Write the log message to the file
                writer.write(logMessage + "\n");
            }

            System.out.println("COMPLETED IMAGE INSPECTION. Results saved to image_inspection_results.txt");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error writing image inspection results to file: " + e.getMessage());
        } catch (NoSuchSessionException e) {
            restartTest();
        } finally {
            // Ensure the driver and browser are closed properly after the test
            driver.quit();
        }
    }

    private void runSiteNavTest(String url) {
        // Nav test logic
    }

    public static void scrollToBottom(WebDriver driver) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public static void scrollToElement(WebDriver driver, WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        } catch (JavascriptException e) {
            e.printStackTrace();
            System.err.println("Error occurred while scrolling to the element: " + e.getMessage());
        }
    }

}
