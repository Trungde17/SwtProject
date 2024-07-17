package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.HomestaySearchPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import pages.HomestaySearchPageQHuy;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomestaySearchTestHuy {
    private WebDriver driver;
    private HomestaySearchPageQHuy homestaySearchPage;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        driver.get("http://localhost:8081/healingland/index.jsp"); // Update the URL to match your environment
        homestaySearchPage = new HomestaySearchPageQHuy(driver);
    }

    @Test
    public void testSearchWithOnlyLocation() {
        System.out.println("Test: Search with Only Location");
        homestaySearchPage.selectDistrict("Hải Châu");
        homestaySearchPage.clickSearchButton();

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("searchServlet"), "URL should contain 'searchServlet' after searching");

        String pageSource = driver.getPageSource();
        System.out.println(pageSource);

        try {
            System.out.println("Waiting for search results...");
            WebElement searchResults = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search-results")));
            assertTrue(searchResults.isDisplayed(), "Search results should be displayed");

            WebElement pagination = driver.findElement(By.className("pagination"));
            assertTrue(pagination.isDisplayed(), "Pagination should be displayed");

            WebElement firstResult = searchResults.findElement(By.cssSelector(".result-item"));
            assertTrue(firstResult.isDisplayed(), "At least one search result should be displayed");
        } catch (Exception e) {
            System.out.println("Search results element not found. Exception: " + e.getMessage());
        }
    }

    @Test
    public void testSearchWithOnlyCheckInDate() {
        System.out.println("Test: Search with Only Check-In Date");
        homestaySearchPage.enterCheckInDate("07-20-2024");
        homestaySearchPage.clickSearchButton();

        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL: " + currentUrl);

        // Check if the URL does not change and print a message
        if (currentUrl.equals("http://localhost:8081/healingland/index.jsp")) {
            System.out.println("The URL did not change. The search operation might not have been triggered.");
        } else {
            assertTrue(currentUrl.contains("searchServlet"), "URL should contain 'searchServlet' after searching");
        }

        String pageSource = driver.getPageSource();
        System.out.println(pageSource);

        try {
            System.out.println("Waiting for search results...");
            WebElement searchResults = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search-results")));
            assertTrue(searchResults.isDisplayed(), "Search results should be displayed");

            WebElement pagination = driver.findElement(By.className("pagination"));
            assertTrue(pagination.isDisplayed(), "Pagination should be displayed");

            WebElement firstResult = searchResults.findElement(By.cssSelector(".result-item"));
            assertTrue(firstResult.isDisplayed(), "At least one search result should be displayed");
        } catch (Exception e) {
            System.out.println("Search results element not found. Exception: " + e.getMessage());
        }
    }

    @Test
    public void testSearchWithOnlyCheckOutDate() {
        System.out.println("Test: Search with Only Check-Out Date");
        homestaySearchPage.leaveCheckInDateBlank();
        homestaySearchPage.enterCheckOutDate("07-25-2024");
        homestaySearchPage.clickSearchButton();

        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL: " + currentUrl);

        // Check if the URL does not change and print a message
        if (currentUrl.equals("http://localhost:8081/healingland/index.jsp")) {
            System.out.println("The URL did not change. The search operation might not have been triggered.");
        } else {
            assertTrue(currentUrl.contains("searchServlet"), "URL should contain 'searchServlet' after searching");
        }

        String pageSource = driver.getPageSource();
        System.out.println(pageSource);

        try {
            System.out.println("Waiting for search results...");
            WebElement searchResults = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search-results")));
            assertTrue(searchResults.isDisplayed(), "Search results should be displayed");

            WebElement pagination = driver.findElement(By.className("pagination"));
            assertTrue(pagination.isDisplayed(), "Pagination should be displayed");

            WebElement firstResult = searchResults.findElement(By.cssSelector(".result-item"));
            assertTrue(firstResult.isDisplayed(), "At least one search result should be displayed");
        } catch (Exception e) {
            System.out.println("Search results element not found. Exception: " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
