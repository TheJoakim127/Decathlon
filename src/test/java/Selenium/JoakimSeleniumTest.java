package Selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;




public class JoakimSeleniumTest {

    @Test
    public void tricentisOpenTest() {
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
        driver.findElement(By.id("modeHep")).click();
        driver.findElement(By.id("name")).sendKeys("Anna");
        driver.findElement(By.id("add")).click();
        WebElement dropdown = driver.findElement(By.id("event"));
        Select select = new Select(dropdown);
        select.selectByValue("hepHighJump");
        driver.findElement(By.id("raw")).sendKeys("250");
        driver.findElement(By.id("save")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By totalLocator = By.cssSelector("[data-testid='standingsTable'] tr td:last-child");
        wait.until(ExpectedConditions.textToBe(totalLocator, "1948"));
        String total = driver.findElement(totalLocator).getText();
        String name = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-testid='standingsTable'] tr td:nth-child(2)"))).getText();
        assertEquals("1948", total);
        assertEquals("Anna", name);
        //driver.quit();
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