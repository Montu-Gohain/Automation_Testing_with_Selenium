import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import java.time.Duration;

public class HappyFlowRahul {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        String name = "rahul";
        String password = "rahulshettyacademy";
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        // Open the browser and reach the webpage
        driver.get("https://rahulshettyacademy.com/locatorspractice/");
        // Fill the input fields
        driver.findElement(By.xpath("/html/body/div/div/div/div/div/div/div[2]/form/input[1]")).sendKeys(name);
        driver.findElement(By.xpath("/html/body/div/div/div/div/div/div/div[2]/form/input[2]")).sendKeys(password);
        // Click on the Sign In Button
        driver.findElement(By.xpath("/html/body/div/div/div/div/div/div/div[2]/form/button")).click();
       try{
           Thread.sleep(3000);
           String homeText = driver.findElement(By.xpath("/html/body/div/div/div/div/p")).getText();
           // Expected text
           String expectedText = "You are successfully logged in.";
           // Assert if the actual text matches the expected text
           Assert.assertEquals(homeText, expectedText);
           System.out.println("Text assertion passed!");
           Thread.sleep(5000);
           driver.quit();
       }
       catch (InterruptedException E){
           E.printStackTrace();
           driver.quit();
       }

    }
}
