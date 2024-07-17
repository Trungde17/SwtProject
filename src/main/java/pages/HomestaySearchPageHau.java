package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomestaySearchPageHau {
    private WebDriver driver;

    // Locators
    private By districtSelect = By.id("district");
    private By checkInInput = By.id("checkIn");
    private By checkOutInput = By.id("checkOut");
    private By guestSelect = By.id("guest");
    private By searchButton = By.cssSelector("button[type='submit']");

    // Constructor
    public HomestaySearchPageHau(WebDriver driver) {
        this.driver = driver;
    }

    // Page actions
    public void selectDistrict(String district) {
        WebElement districtElement = driver.findElement(districtSelect);
        districtElement.sendKeys(district);
    }

    public void leaveDistrictBlank() {
        WebElement districtElement = driver.findElement(districtSelect);
        districtElement.click(); // Open the dropdown
        districtElement.findElement(By.cssSelector("option[value='']")).click(); // Select the blank option if it exists
    }

    public void enterCheckInDate(String checkInDate) {
        WebElement checkInElement = driver.findElement(checkInInput);
        checkInElement.sendKeys(checkInDate);
    }

    public void leaveCheckInDateBlank() {
        WebElement checkInElement = driver.findElement(checkInInput);
        checkInElement.clear();
    }

    public void enterCheckOutDate(String checkOutDate) {
        WebElement checkOutElement = driver.findElement(checkOutInput);
        checkOutElement.sendKeys(checkOutDate);
    }

    public void leaveCheckOutDateBlank() {
        WebElement checkOutElement = driver.findElement(checkOutInput);
        checkOutElement.clear();
    }

    public void selectGuests(String guests) {
        WebElement guestElement = driver.findElement(guestSelect);
        guestElement.sendKeys(guests);
    }

    public void clickSearchButton() {
        WebElement searchButtonElement = driver.findElement(searchButton);
        searchButtonElement.click();
    }
}
