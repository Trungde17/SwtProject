package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomestaySearchPage {
    private WebDriver driver;

    // Locators
    private By districtSelect = By.id("district");
    private By checkInInput = By.id("checkIn");
    private By checkOutInput = By.id("checkOut");
    private By guestSelect = By.id("guest");
    private By searchButton = By.cssSelector("button[type='submit']");

    // Constructor
    public HomestaySearchPage(WebDriver driver) {
        this.driver = driver;
    }

    // Page actions
    public void selectDistrict(String district) {
        WebElement districtElement = driver.findElement(districtSelect);
        districtElement.sendKeys(district);
    }

    public void enterCheckInDate(String checkInDate) {
        WebElement checkInElement = driver.findElement(checkInInput);
        checkInElement.sendKeys(checkInDate);
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
