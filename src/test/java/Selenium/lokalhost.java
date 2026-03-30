package Selenium;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class lokalhost {
    @Test
    public void testoppenlokalhost() {
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/");

        driver.findElement(By.id("modeHep")).click();
    }
}
