package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomestaySearchPageQHuy {
    private WebDriver driver;

    // Locators
    private By districtSelect = By.id("district");
    private By checkInInput = By.id("checkIn");
    private By checkOutInput = By.id("checkOut");
    private By guestSelect = By.id("guest");
    private By searchButton = By.cssSelector("button[type='submit']");

    // Constructor
    public HomestaySearchPageQHuy(WebDriver driver) {
        this.driver = driver;
    }

    // Page actions
    public void selectDistrict(String district) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement districtElement = wait.until(ExpectedConditions.elementToBeClickable(districtSelect));
        System.out.println("Selecting district: " + district);
        districtElement.sendKeys(district);
    }

    public void enterCheckInDate(String checkInDate) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement checkInElement = wait.until(ExpectedConditions.elementToBeClickable(checkInInput));
        System.out.println("Entering check-in date: " + checkInDate);
        checkInElement.clear(); // Ensure the field is cleared
        checkInElement.sendKeys(checkInDate);
    }

    public void leaveCheckInDateBlank() {
        WebElement checkInElement = driver.findElement(checkInInput);
        checkInElement.clear();
    }

    public void enterCheckOutDate(String checkOutDate) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement checkOutElement = wait.until(ExpectedConditions.elementToBeClickable(checkOutInput));
        System.out.println("Entering check-out date: " + checkOutDate);
        checkOutElement.clear(); // Ensure the field is cleared
        checkOutElement.sendKeys(checkOutDate);
    }

    public void leaveCheckOutDateBlank() {
        WebElement checkOutElement = driver.findElement(checkOutInput);
        checkOutElement.clear();
    }

    public void selectGuests(String guests) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement guestElement = wait.until(ExpectedConditions.visibilityOfElementLocated(guestSelect));
        wait.until(ExpectedConditions.elementToBeClickable(guestElement));

        System.out.println("Guest element state - Displayed: " + guestElement.isDisplayed() + ", Enabled: " + guestElement.isEnabled());
        if (guestElement.isDisplayed() && guestElement.isEnabled()) {
            System.out.println("Clearing and selecting guests: " + guests);
            guestElement.clear(); // Ensure the field is cleared
            guestElement.sendKeys(guests);
        } else {
            System.out.println("Guest element is not interactable, using JavaScript.");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].value='" + guests + "';", guestElement);
        }
    }

    public void clickSearchButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement searchButtonElement = wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        System.out.println("Clicking search button.");
        searchButtonElement.click();
    }
}
