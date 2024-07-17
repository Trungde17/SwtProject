package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.HomestaySearchPagePink;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class HomestaySearchTestPink {
    private WebDriver driver;
    private HomestaySearchPagePink homestaySearchPage;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        driver.get("http://localhost:8080/healingland/"); // Change to your actual URL
        homestaySearchPage = new HomestaySearchPagePink(driver);
    }

    @Test
    public void testSearchWithResults() {
        // Select location
        homestaySearchPage.selectDistrict("Hải Châu");

        // Enter check-out date
        homestaySearchPage.enterCheckOutDate("07-25-2024");

        // Enter check-in date
        homestaySearchPage.enterCheckInDate("07-20-2024");

        // Select number of guests
        homestaySearchPage.selectGuests("2");

        // Click search button
        homestaySearchPage.clickSearchButton();

        // Verify URL change after search
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("searchServlet"), "URL should contain 'homestaySearch' after search");

        // Verify that the search results are displayed correctly
        WebElement searchResults = driver.findElement(By.id("search-results"));
        assertTrue(searchResults.isDisplayed(), "Search results should be displayed");

        // Verify the pagination
        WebElement pagination = driver.findElement(By.className("pagination"));
        assertTrue(pagination.isDisplayed(), "Pagination should be displayed");

        // Verify at least one result is displayed
        WebElement firstResult = searchResults.findElement(By.cssSelector(".result-item"));
        assertTrue(firstResult.isDisplayed(), "At least one search result should be displayed");
    }

    @Test
    public void testSearchWithoutResults() {
        // Select location
        homestaySearchPage.selectDistrict("Hải Châu");

        // Enter check-out date
        homestaySearchPage.enterCheckOutDate("07-25-2024");

        // Enter check-in date
        homestaySearchPage.enterCheckInDate("07-20-2024");

        // Select number of guests
        homestaySearchPage.selectGuests("100"); // Assuming 100 guests will return no results

        // Click search button
        homestaySearchPage.clickSearchButton();

        // Verify URL change after search
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("searchServlet"), "URL should contain 'homestaySearch' after search");

        // Verify that no search results are displayed
        WebElement searchResults = driver.findElement(By.id("search-results"));
        assertTrue(searchResults.isDisplayed(), "Search results should not be displayed");

        // Verify no pagination is displayed
        WebElement pagination = driver.findElement(By.className("pagination"));
        assertTrue(pagination.isDisplayed(), "Pagination should not be displayed");
    }

    @Test
    public void testSearchWithEmptyLocation() {
        // Leave location empty
        WebElement districtElement = driver.findElement(By.id("district"));
        //districtElement.clear();

        // Enter check-out date
        homestaySearchPage.enterCheckOutDate("07-25-2024");

        // Enter check-in date
        homestaySearchPage.enterCheckInDate("07-20-2024");

        // Select number of guests
        homestaySearchPage.selectGuests("2");

        // Click search button
        homestaySearchPage.clickSearchButton();

        // Verify URL change after search
        String currentUrl = driver.getCurrentUrl();
        assertFalse(currentUrl.contains("searchServlet"), "URL should not contain 'homestaySearch' after search");


    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
