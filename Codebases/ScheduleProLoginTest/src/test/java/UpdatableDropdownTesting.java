import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;

public class UpdatableDropdownTesting {
    public static void main(String[] args) {
        // Creating the webdriver to open the perform the tests in the browser.
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();

        try{
            Properties prop = new Properties();
            File file = new File("testdata.properties");
            FileInputStream fis = new FileInputStream(file);
            prop.load(fis);

            String TEST_URL = prop.getProperty("dropdownTestingUrl");

            // Step 0 : Open the website.
            driver.get(TEST_URL);


            // Let's open the passengers list dropdown menu
            driver.findElement(By.id("divpaxinfo")).click();
            Thread.sleep(2000);


            // Increase the passengers number to five, i. e. we need to click the add option four times.
            int clickCounts = 4;
            while(clickCounts > 0){
                    driver.findElement(By.id("hrefIncAdt")).click();
                    Thread.sleep(400);
                clickCounts--;
            }
            // After increasing the passengers number let's print the current state of the dropdown menu
            System.out.println(driver.findElement(By.id("divpaxinfo")).getText());

            Thread.sleep(2000);
            // Click on the Done button.
            driver.findElement(By.id("btnclosepaxoption")).click();


            // Step Last: Close the driver after completion of tests.
            Thread.sleep(3000);
            driver.quit();
        }
        catch (Exception E){
            E.printStackTrace();
        }
    }
}
