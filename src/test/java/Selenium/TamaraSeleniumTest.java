package Selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TamaraSeleniumTest {

    @Test
    public void tricentisOpenTest() {
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
        driver.findElement(By.id("modeHep")).click();
    }
    @Test
    public void AddCompetitor () {
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
        WebElement Add = driver.findElement(By.id("name"));
        Add.sendKeys("Anna");


    }


}
