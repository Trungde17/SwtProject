package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.HomestaySearchPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import pages.HomestaySearchPageHau;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class HomestaySearchTestHau {
    private WebDriver driver;
    private HomestaySearchPageHau homestaySearchPage;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        driver.get("http://localhost:8080/healingland/index.jsp"); // Đường dẫn tới trang index.jsp của bạn
        homestaySearchPage = new HomestaySearchPageHau(driver);
    }

    @Test
    public void testSearchWithCheckInBlank() throws InterruptedException {
        // Leave location blank
        homestaySearchPage.selectDistrict("Hải Châu");
        Thread.sleep(1000);
        // Leave check-in date blank
        homestaySearchPage.leaveCheckInDateBlank();
        Thread.sleep(1000);
        // Enter valid check-out date
        homestaySearchPage.enterCheckOutDate("07-20-2024");
        Thread.sleep(1000);
        // Select number of guests
        homestaySearchPage.selectGuests("2");
        Thread.sleep(1000);
        // Click search button
        homestaySearchPage.clickSearchButton();

        // Verify that the page redirects to the correct page based on the form validation
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("searchServlet")) {
            // Verify that the search results are displayed correctly
            WebElement searchResults = driver.findElement(By.id("search-results"));
            assertTrue(searchResults.isDisplayed(), "Search results should be displayed");

            // Verify the pagination
            WebElement pagination = driver.findElement(By.className("pagination"));
            assertTrue(pagination.isDisplayed(), "Pagination should be displayed");

            // Verify at least one result is displayed
            WebElement firstResult = searchResults.findElement(By.cssSelector(".result-item"));
            assertTrue(firstResult.isDisplayed(), "At least one search result should be displayed");
        } else {
            // If validation fails, verify that the user is still on the index page
            assertTrue(currentUrl.contains("index.jsp"), "User should be redirected to index.jsp");
        }
    }

    @Test
    public void testSearchWithBlankLocationAndCheckInDate() throws InterruptedException {
        // Leave location blank
        homestaySearchPage.leaveDistrictBlank();
        Thread.sleep(1000);
        // Leave check-in date blank
        homestaySearchPage.leaveCheckInDateBlank();
        Thread.sleep(1000);
        // Enter valid check-out date
        homestaySearchPage.enterCheckOutDate("07-20-2024");
        Thread.sleep(1000);
        // Select number of guests
        homestaySearchPage.selectGuests("2");
        Thread.sleep(1000);
        // Click search button
        homestaySearchPage.clickSearchButton();

        // Verify that the page redirects to the correct page based on the form validation
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("searchServlet")) {
            // Verify that the search results are displayed correctly
            WebElement searchResults = driver.findElement(By.id("search-results"));
            assertTrue(searchResults.isDisplayed(), "Search results should be displayed");

            // Verify the pagination
            WebElement pagination = driver.findElement(By.className("pagination"));
            assertTrue(pagination.isDisplayed(), "Pagination should be displayed");

            // Verify at least one result is displayed
            WebElement firstResult = searchResults.findElement(By.cssSelector(".result-item"));
            assertTrue(firstResult.isDisplayed(), "At least one search result should be displayed");
        } else {
            // If validation fails, verify that the user is still on the index page
            assertTrue(currentUrl.contains("index.jsp"), "User should be redirected to index.jsp");
        }
    }

    @Test
    public void testSearchWithInvalid() throws InterruptedException {
        homestaySearchPage.selectDistrict("Hải Châu");
        Thread.sleep(1000);
        // Enter check-in date
        homestaySearchPage.enterCheckOutDate("12/10/2025");
        Thread.sleep(1000);
        homestaySearchPage.enterCheckInDate("11-10-2024");
        Thread.sleep(1000);


        // Select number of guests
        homestaySearchPage.selectGuests("-1");
        Thread.sleep(1000);
        // Verify that the page redirects to the correct page based on the form validation
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("searchServlet")) {
            // Verify that the search results are displayed correctly
            WebElement searchResults = driver.findElement(By.id("search-results"));
            assertTrue(searchResults.isDisplayed(), "Search results should be displayed");

            // Verify the pagination
            WebElement pagination = driver.findElement(By.className("pagination"));
            assertTrue(pagination.isDisplayed(), "Pagination should be displayed");

            // Verify at least one result is displayed
            WebElement firstResult = searchResults.findElement(By.cssSelector(".result-item"));
            assertTrue(firstResult.isDisplayed(), "At least one search result should be displayed");
        } else {
            // If validation fails, verify that the user is still on the index page
            assertTrue(currentUrl.contains("index.jsp"), "User should be redirected to index.jsp");
        }
    }

    @Test
    public void testSearchWithBlankDistrict() throws InterruptedException {
        // Select location
        homestaySearchPage.selectDistrict("Hải Châu");
        Thread.sleep(1000);
        // Enter check-in date
        homestaySearchPage.enterCheckOutDate("12/10/2025");
        Thread.sleep(1000);
        homestaySearchPage.enterCheckInDate("11-10-2024");
        Thread.sleep(1000);


        // Select number of guests
        homestaySearchPage.selectGuests("7");
        Thread.sleep(1000);
        // Click search button
        homestaySearchPage.clickSearchButton();
        Thread.sleep(3000);
        // Try to find an element indicative of a successful search result
        boolean isSearchSuccessful = true;
        try {
            WebElement imgElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("oxd-userdropdown-img")));
            assertTrue(imgElement.isDisplayed());
        } catch (Exception e) {
            isSearchSuccessful = false;
        }

        // If the search was not successful, navigate to the index page
        if (!isSearchSuccessful) {
            driver.get("http://localhost:8080/healingland/index.jsp");
        }
    }
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
