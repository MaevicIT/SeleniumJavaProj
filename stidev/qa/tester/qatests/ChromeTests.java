package stidev.qa.tester.qatests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import stidev.qa.tester.model.TestRunner;
import stidev.qa.tester.model.Tests;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Set;

public class ChromeTests extends TestRunner {

    private WebDriver driver;
    private Tests test;

    public ChromeTests(WebDriver driver, Tests test) {
        this.driver = driver;
        this.test = test;
    }

    public void runTest() {
        try {
            switch (test) {
                case LOGIN -> runLoginTest("https://raper-dev-www.stifirestop.com");
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
        driver = new ChromeDriver();
        runTest();
    }

    private void runLoginTest(String url) {
        // Login test logic
        driver.get(url);
        System.out.println("browser opened");

        WebElement login = driver.findElement(By.name("login"));

        login.click();

    }

    private void runSignoutTest(String url) {
        // Signout test logic
        // ...
    }

    private void runImagesTest(String url) {
        driver.get(url);
        //driver.manage().window().maximize();
        System.out.println("browser opened...");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        System.out.println("Page Load Complete");

        scrollToBottom(driver);
        System.out.println("At Bottom of Page");

        // Wait for the images to be present on the page


        // Find all the image elements on the page
        List<WebElement> imageElements = driver.findElements(By.tagName("img"));
        // wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("img")));

        System.out.println("Running image inspection...");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("image_inspection_results_Chrome.txt"))) {
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
        driver.get(url);
        //driver.manage().window().maximize();

        System.out.println("browser opened...");
        scrollToBottom(driver);

        // Wait for the navigation bar menu and footer to be present
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("a")));
        //wait.until(ExpectedConditions.presenceOfElementLocated(By.className("")));

        // Find all the navigation links in the Nav bar menu
        List<WebElement> navLinks = driver.findElements(By.tagName("a"));
        List<WebElement> navigationLinks = driver.findElements(By.cssSelector("a[href]:not([href=''])"));

        int c = 0;
        for(WebElement w : navLinks){
            String tx = w.getText();
            String txURL = w.getAttribute("href");
            System.out.println(w);
            System.out.println(tx);
            System.out.println(txURL);
            c++;
        }

        System.out.println("# of Total links: " + c );

        //System Status output to see what the app is doing.
        System.out.println("Adjusted number of links (calc): " + c);
        System.out.println("Adjusted number of links (act): " + navigationLinks.size());
        for (WebElement x : navigationLinks) {
            System.out.println("Links: " + x.getText() + " - " + x.getAttribute("href"));
        }

        //System status milestone to monitor process progress
        System.out.println("Running link check...");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("link_check_results_Chrome.txt"))) {
            // Iterate over each link to verify destination
            for (int i = 0; i < navigationLinks.size(); i++) {
                WebElement link = navigationLinks.get(i);
                String linkText = link.getText();
                System.out.println(linkText);
                String linkURL = link.getAttribute("href");
                System.out.println(linkURL);
                // Filter out external links based on the domain of the link URL
                if (isExternalLink(linkURL)) {
                    System.out.println("Ignoring external link: " + linkURL);
                    continue;
                }

                // Attempt to click the link and get current URL
                try {
                    scrollToElement(driver, link);
                    link.click();
                } catch (StaleElementReferenceException e) {
                    System.err.println("Stale element encountered. Re-finding the element.");
                    navigationLinks = driver.findElements(By.linkText(linkText));
                    link = navigationLinks.get(i);
                    scrollToElement(driver, link);
                    link.click();
                } catch (ElementNotInteractableException e){
                    System.err.println("This Element can't be interacted with: " + linkText);
                    link = navigationLinks.get(i+1);
                    scrollToElement(driver, link);
                    link.click();
                    continue;
                }

                // Check if the link opens a new window or tab
                String currentWindowHandle = driver.getWindowHandle();
                Set<String> windowHandles = driver.getWindowHandles();
                for (String handle : windowHandles) {
                    if (!handle.equals(currentWindowHandle)) {
                        try {
                            driver.switchTo().window(handle);
                            // Handle any operations on the new window/tab, if necessary
                        } catch (NoSuchSessionException ex) {
                            System.err.println("WebDriver session not found. Re-initializing WebDriver.");
                            driver.quit();
                            driver = new ChromeDriver();
                            driver.get(url);
                            driver.manage().window().maximize();
                            navigationLinks = driver.findElements(By.tagName("a"));
                            link = navigationLinks.get(i);
                            scrollToElement(driver, link);
                            link.click();
                            currentWindowHandle = driver.getWindowHandle();
                            windowHandles = driver.getWindowHandles();
                            driver.switchTo().window(handle);
                        }
                        break;
                    }
                }

                String currentURL = driver.getCurrentUrl();

                // Check current URL and expected URL
                String logMessage;
                if (currentURL.equals(linkURL)) {
                    logMessage = "Link: " + linkText + " - Destination is correct";
                } else {
                    logMessage = "Link: " + linkText + " - Destination is NOT correct\n"
                            + "Expected: " + linkURL + "\n"
                            + "Actual: " + currentURL;
                }

                // Write the log message to the file
                writer.write(logMessage + "\n");

                // Close the new window/tab and switch back to the original window
                driver.close();
                driver.switchTo().window(currentWindowHandle);
            }

            System.out.println("COMPLETED LINK CHECK. Results saved to link_check_results.txt");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error writing link check results to file: " + e.getMessage());
        } catch (NoSuchSessionException e) {
            restartTest();
        } finally {
            // Ensure the driver and browser are closed properly after the test
            driver.quit();
        }
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

    private boolean isExternalLink(String linkURL) {
        // Implement logic to check if the linkURL belongs to an external domain
        // For example, check if the linkURL starts with "http://" or "https://" and does not belong to the current website's domain.
        // Return true if it's an external link, false otherwise.
        return linkURL.startsWith("http://") || linkURL.startsWith("https://") && !linkURL.contains("stifirestop.com");
    }
}

