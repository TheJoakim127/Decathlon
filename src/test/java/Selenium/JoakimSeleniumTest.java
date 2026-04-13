package Selenium;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.HashMap;
import java.util.Map;

public class JoakimSeleniumTest {
    @Test
    public void testoppenlokalhost() {
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/");

        driver.findElement(By.id("modeHep")).click();
        driver.findElement(By.id("name")).sendKeys("Cici");
        driver.findElement(By.id("add")).click();

        Select select = new Select(driver.findElement(By.id("event")));
        select.selectByVisibleText("100m Hurdles (s)");

        driver.findElement(By.id("raw")).sendKeys("9");
        driver.findElement(By.id("save")).click();
    }


    //auto test all event
    @Test
    public void testAllHeptathlonEvents() {
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("http://localhost:8080/");

            // Väljer Heptathlon
            driver.findElement(By.id("modeHep")).click();

            // Skriver in namn
            driver.findElement(By.id("name")).sendKeys("Cici");

            // Lägger till en deltagare
            driver.findElement(By.id("add")).click();

            // Testdata för olika grenar
            Map<String, String> testData = new HashMap<>();
            testData.put("100m Hurdles (s)", "9");
            testData.put("High Jump (cm)", "150");
            testData.put("Shot Put (m)", "10");
            testData.put("200m (s)", "25");
            testData.put("Long Jump (cm)", "500");
            testData.put("Javelin Throw (m)", "30");
            testData.put("800m (s)", "70");

            // Testar varje gren med ett värde
            for (String event : testData.keySet()) {
                Select eventSelect = new Select(driver.findElement(By.id("event")));
                eventSelect.selectByVisibleText(event);

                WebElement rawInput = driver.findElement(By.id("raw"));
                rawInput.clear();
                rawInput.sendKeys(testData.get(event));

                driver.findElement(By.id("save")).click();

                System.out.println("Testade: " + event + " -> " + testData.get(event));
            }

        } finally {
            // Stänger webbläsaren
            // driver.quit();
        }
    }

    /*WebDriver driver;
    //explicit wait: Väntar tills ett element syns
    private WebElement waitForElement(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }*/
}
