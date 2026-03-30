package SeleniumAlek;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class AlekSeleniumTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Load Page Test
    @Test
    public void pageLoadsTest() {
        String title = driver.getTitle();
        assertNotNull(title);
    }

    // Add competitor Test
    @Test
    public void addCompetitorTest() {

        WebElement nameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='competitorNameInput']")
                )
        );

        nameInput.sendKeys("Alek");

        driver.findElement(
                By.cssSelector("[data-testid='addCompetitorBtn']")
        ).click();

        // Verify competitor added
        assertTrue(driver.getPageSource().contains("Alek"));
    }

    // Add Result Test
    @Test
    public void addResultTest() {

        // Add competitor first
        WebElement nameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='competitorNameInput']")
                )
        );
        nameInput.sendKeys("Alek");

        driver.findElement(
                By.cssSelector("[data-testid='addCompetitorBtn']")
        ).click();

        // Enter name in result section
        WebElement resultName = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("name2")
                )
        );
        resultName.sendKeys("Alek");

        // Select event (100m)
        WebElement eventDropdown = driver.findElement(
                By.cssSelector("[data-testid='eventSelect']")
        );
        eventDropdown.sendKeys("100m (s)");

        // Enter result value
        WebElement resultInput = driver.findElement(
                By.cssSelector("[data-testid='rawInput']")
        );
        resultInput.sendKeys("11");

        // Save score
        driver.findElement(
                By.cssSelector("[data-testid='saveScoreBtn']")
        ).click();

        // 6. Verify result appears in standings
       assertTrue(driver.getPageSource().contains("11"));
    }

}