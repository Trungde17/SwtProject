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

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class HomestaySearchTest {
    private WebDriver driver;
    private HomestaySearchPage homestaySearchPage;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        driver.get("http://localhost:8081/healingland/index.jsp"); // Đường dẫn tới trang index.jsp của bạn
        homestaySearchPage = new HomestaySearchPage(driver);
    }

    // Search correct location, fill blank date check out, date correct check in and correct number of guest
    @Test
    public void testSearchWithBlankCheckOutDate() {
        // Select location
        homestaySearchPage.selectDistrict("Hải Châu");

        // Enter check-in date
        homestaySearchPage.enterCheckInDate("2024-07-20");

        // Leave check-out date blank
        homestaySearchPage.leaveCheckOutDateBlank();

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

    // Search correct location, choose check-in date < now, correct check-out date, and correct number of guests
    @Test
    public void testCheckInDateCannotBeInPast() throws InterruptedException {
        // Định dạng ngày tháng theo yêu cầu của trường nhập liệu
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        // Lấy ngày hôm nay và ngày hôm qua
        String today = LocalDate.now().format(formatter);
        String pastDate = LocalDate.now().minusDays(1).format(formatter);

        // Tìm trường nhập liệu ngày check-in và đặt ngày trong quá khứ
        WebElement checkInInput = driver.findElement(By.id("checkIn")); // Thay đổi 'checkIn' theo id thực tế của trường nhập liệu
        checkInInput.sendKeys(pastDate);

        // Đợi 10 giây để kiểm tra giá trị của trường nhập liệu ngày check-in
        Thread.sleep(5000);
        String actualCheckInDate = checkInInput.getAttribute("value");

        assertTrue(actualCheckInDate.compareTo(today) >= 0, "Check-in date should be today's date or a future date");
    }

    @Test
    public void testCheckOutDateCannotBeInPast() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        String today = LocalDate.now().format(formatter);
        String pastDate = LocalDate.now().minusDays(1).format(formatter);

        // Enter a past date for check-out
        homestaySearchPage.enterCheckOutDate(pastDate);

        // Get the value of check-out date input and check it
        String actualCheckOutDate = driver.findElement(By.id("checkOut")).getAttribute("value");
        assertTrue(actualCheckOutDate.compareTo(today) >= 0, "Check-out date should be today's date or a future date");
    }

    @Test
    public void testCheckOutDateCannotBeEarlierThanCheckInDate() throws InterruptedException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        String futureDate = LocalDate.now().plusDays(5).format(formatter);
        String earlierDate = LocalDate.now().plusDays(3).format(formatter);

        // Enter a valid check-in date
        homestaySearchPage.enterCheckInDate(futureDate);

        // Enter a check-out date earlier than check-in date
        homestaySearchPage.enterCheckOutDate(earlierDate);

        // Check for the alert and verify its message
        boolean alertPresent = false;
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            alert.accept();
            alertPresent = true;
            assertTrue(alertText.contains("Check-out date cannot be earlier than check-in date. Please reselect the dates."), "Alert message is incorrect");
        } catch (Exception e) {
            alertPresent = false;
        }
        assertTrue(alertPresent, "Alert was not present when check-out date was earlier than check-in date");

        Thread.sleep(5000);
        // Verify that the check-out date is not set to an earlier date than the check-in date
        String actualCheckOutDate = driver.findElement(By.id("checkOut")).getAttribute("value");
        assertFalse(actualCheckOutDate.equals(earlierDate), "Check-out date should not be earlier than check-in date");
    }

    @Test
    public void testCheckInDateCannotBeLaterThanCheckOutDate() throws InterruptedException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        String validCheckOutDate = LocalDate.now().plusDays(5).format(formatter);
        String invalidCheckInDate = LocalDate.now().plusDays(6).format(formatter);

        // Enter a valid check-out date
        homestaySearchPage.enterCheckOutDate(validCheckOutDate);

        // Enter a check-in date later than the check-out date
        homestaySearchPage.enterCheckInDate(invalidCheckInDate);

        // Check for the alert and verify its message
        boolean alertPresent = false;
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            alert.accept();
            alertPresent = true;
            assertTrue(alertText.contains("Check-in date cannot be later than check-out date. Please reselect the dates."), "Alert message is incorrect");
        } catch (Exception e) {
            alertPresent = false;
        }
        assertTrue(alertPresent, "Alert was not present when check-in date was later than check-out date");

        Thread.sleep(5000);
        // Verify that the check-in date is not set to a later date than the check-out date
        String actualCheckInDate = driver.findElement(By.id("checkIn")).getAttribute("value");
        assertFalse(actualCheckInDate.equals(invalidCheckInDate), "Check-in date should not be later than check-out date");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
