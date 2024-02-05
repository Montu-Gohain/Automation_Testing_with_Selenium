import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

//
//These are the locators available in Selenium
//
//System given : 
//	Id
//	name
//	className
//	TagName
//	LinkText
//	Partial Link Text
//Created manually : 
// Xpath
// CSS Selector


public class Locators {
	
	public static void main(String[] args) {
	   WebDriver driver = new ChromeDriver();
	   
	   //We need to use implicit wait, its like setTimeout() in JS.
	   
	   driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
	   
	   driver.get("https://rahulshettyacademy.com/locatorspractice/");
	   
	   // Let's find input element for email using ID locator
	   driver.findElement(By.id("inputUsername")).sendKeys("rahul");
	   
	   
	   driver.findElement(By.name("inputPassword")).sendKeys("hello123");
	   
	   // Let's check the checkboxes
	   
	   driver.findElement(By.id("chkboxOne")).click();
	   driver.findElement(By.id("chkboxTwo")).click();
	   
	   // Let's target the submit button
	   driver.findElement(By.className("signInBtn")).click();
	   
	   

	   
       // After Getting the error let's access that error element by using cssSelector.
	   String error_element = driver.findElement(By.cssSelector("p.error")).getText();
	   System.out.println(error_element);
	}
}
