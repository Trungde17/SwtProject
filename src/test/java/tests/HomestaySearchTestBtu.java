package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.HomestaySearchPageBTu;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class HomestaySearchTestBtu {
    private WebDriver driver;
    private HomestaySearchPageBTu homestaySearchPageBTu;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        driver.get("http://localhost:8080/healingland/index.jsp"); // Đường dẫn tới trang index.jsp của bạn
        homestaySearchPageBTu = new HomestaySearchPageBTu(driver);
    }

    // test search blank district and check out
    @Test
    public void testSearchWithBlankCheckOutAndDistrict() throws InterruptedException {
        // Select location
        homestaySearchPageBTu.selectDistrict("");
        Thread.sleep(1000);
        // Enter check-in date

        homestaySearchPageBTu.enterCheckInDate("11-10-2024");
        Thread.sleep(1000);
        // Leave check-out date blank
        homestaySearchPageBTu.enterCheckOutDate("");
        Thread.sleep(1000);
        // Select number of guests
        homestaySearchPageBTu.selectGuests("2");
        Thread.sleep(1000);
        // Click search button
        homestaySearchPageBTu.clickSearchButton();
        Thread.sleep(1000);
        // Verify URL change after search
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
            assertTrue(currentUrl.contains("http://localhost:8080/healingland/index.jsp"), "User should be redirected to index.jsp");
        }}
    //test check in and check out can not be in past
    @Test
    public void testCheckInDateCannotBeInPast() throws InterruptedException {
        // Định dạng ngày tháng theo yêu cầu của trường nhập liệu
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        // Lấy ngày hôm nay và ngày hôm qua
        String today = LocalDate.now().format(formatter);
        String pastDate = LocalDate.now().minusDays(2).format(formatter);
        String pastDate1 = LocalDate.now().minusDays(1).format(formatter);

        WebElement checkOutInput = driver.findElement(By.id("checkOut")); // Thay đổi 'checkIn' theo id thực tế của trường nhập liệu
        checkOutInput.sendKeys(pastDate1);
        // Tìm trường nhập liệu ngày check-in và đặt ngày trong quá khứ
        WebElement checkInInput = driver.findElement(By.id("checkIn")); // Thay đổi 'checkIn' theo id thực tế của trường nhập liệu
        checkInInput.sendKeys(pastDate);

        // Đợi 10 giây để kiểm tra giá trị của trường nhập liệu ngày check-in
        Thread.sleep(5000);
        String actualCheckInDate = checkInInput.getAttribute("value");
        String actualCheckOutDate = checkOutInput.getAttribute("value");

        System.out.println("Check-in and check-out date should be today's date or a future date");

        assertTrue(actualCheckOutDate.compareTo(today) >= 0, "Check-out date should be today's date or a future date");
        assertTrue(actualCheckInDate.compareTo(today) >= 0, "Check-in date should be today's date or a future date");
    }

    // test blank district and invalid number of guest
    @Test
    public void testSearchWithBlankDistrict() throws InterruptedException {
        // Select location
        homestaySearchPageBTu.selectDistrict("");
        Thread.sleep(1000);
        // Enter check-in date
        homestaySearchPageBTu.enterCheckOutDate("12/10/2025");
        Thread.sleep(1000);
        homestaySearchPageBTu.enterCheckInDate("11/10/2024");
        Thread.sleep(1000);


        // Select number of guests
        homestaySearchPageBTu.selectGuests("7");
        Thread.sleep(1000);
        // Click search button
        homestaySearchPageBTu.clickSearchButton();
        Thread.sleep(2000);
        // Try to find an element indicative of a successful search result
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
            assertTrue(currentUrl.contains("http://localhost:8080/healingland/index.jsp"), "User should be redirected to index.jsp");
        }}
    // test check in and check out fill blank
    @Test
    public void testSearchWithCheckInCheckOutFillBlank() throws InterruptedException {
        // Select location
        homestaySearchPageBTu.selectDistrict("Hải Châu");
        Thread.sleep(1000);
        // Enter check-in date
        homestaySearchPageBTu.enterCheckInDate("");
        Thread.sleep(1000);
        //enter check out date
        homestaySearchPageBTu.enterCheckOutDate("");
        Thread.sleep(1000);
        // Select number of guests
        homestaySearchPageBTu.selectGuests("4");
        Thread.sleep(1000);
        // Click search button
        homestaySearchPageBTu.clickSearchButton();
        Thread.sleep(2000);
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
            assertTrue(currentUrl.contains("http://localhost:8080/healingland/index.jsp"), "User should be redirected to index.jsp");
        }}



    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
