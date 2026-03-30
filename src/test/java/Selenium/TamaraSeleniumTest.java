package Selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TamaraSeleniumTest {

    @Test
    public void tricentisOpenTest() {
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
    }
}
