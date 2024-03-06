package SchedulePro;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;

public class AlertsAndConfirmation {
    public static void main(String[] args) {
        // Let's test out alerts and confirm messages using selenium
        WebDriver driver = new ChromeDriver();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();

        try{
            Properties prop = new Properties();
            File file = new File("testdata.properties");
            FileInputStream fis = new FileInputStream(file);
            prop.load(fis);

            String TEST_URL = prop.getProperty("alertspage");

            driver.get(TEST_URL);

            String username = "Montu Gohain";
            // Todo : Enter the name in the name input field
            driver.findElement(By.cssSelector("[id='name']")).sendKeys(username);
            // Click on the alert button
            driver.findElement(By.id("alertbtn")).click();
            Thread.sleep(3000);
            // Click ok in the alert window.
            System.out.println(driver.switchTo().alert().getText());
            // Let's finally close this alert window.
            driver.switchTo().alert().accept();

            //--------- Now let's test out Confirm button ------------------------------

            Thread.sleep(3000);
            driver.findElement(By.id("confirmbtn")).click();
            Thread.sleep(2000);
//            driver.findElement(By.cssSelector("[id='name']")).sendKeys(username);

            // Let's click on the ok button on the confirm window.
            driver.switchTo().alert().dismiss(); // To click on the cancel button that appers in the confirm window.
            // In case of yes or ok button we just use alert().accept() and for not accepting the window we use alert().dismiss()

            // Close the browser after completion of tests.
            System.out.println("Test Completed");
            Thread.sleep(5000);
            driver.quit();
        }
        catch (Exception E){
            E.printStackTrace();
        }

    }
}
