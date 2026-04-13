package Selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class JoakimSeleniumTest {
    private WebDriverWait wait;
    public WebElement waitForVisible(By locator) {

        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

    }

    @Test
    public void tricentisOpenTest() {
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
        driver.findElement(By.id("modeHep")).click();
    }

    @Test
    public void AddCompetitor() {
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
        driver.findElement(By.id("modeHep")).click();
        WebElement Add = driver.findElement(By.id("name"));
        Add.sendKeys("Anna");

    }

    @Test
    public void EnterResultHighJump() {
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
        driver.findElement(By.id("modeHep")).click();
        WebElement Add = driver.findElement(By.id("name"));
        Add.sendKeys("Anna");
        driver.findElement(By.id("add")).click();
        driver.findElement(new By.ByXPath("/html/body/div[3]/div[1]/select/option[2]")).click();
        driver.findElement(By.id("raw")).sendKeys("100");
        driver.findElement(By.id("save")).click();



    }
    @Test
    public void EnterResultShotPut() {
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
        driver.findElement(By.id("modeHep")).click();
        WebElement Add = driver.findElement(By.id("name"));
        Add.sendKeys("Josef");
        driver.findElement(By.id("add")).click();
        driver.findElement(new By.ByCssSelector("#event > option:nth-child(3)")).click();
        driver.findElement(By.id("raw")).sendKeys("100");
        driver.findElement(By.id("save")).click();



    }
}

