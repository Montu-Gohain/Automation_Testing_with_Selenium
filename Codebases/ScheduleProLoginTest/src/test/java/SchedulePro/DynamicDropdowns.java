package SchedulePro;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;

public class DynamicDropdowns {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        try{
            // Access values from testdata.properties file
            Properties prop = new Properties();
            File file = new File("testdata.properties");
            FileInputStream fis = new FileInputStream(file);
            prop.load(fis);

            String TEST_URL = prop.getProperty("dropdownTestingUrl");

            // Step 0 : Open the webpage.
            driver.get(TEST_URL);

            // Step 1 : Click on the FROM option
            driver.findElement(By.id("ctl00_mainContent_ddl_originStation1_CTXT")).click();

            // Step 2 : Select Bengaluru from the options
            driver.findElement(By.xpath("//a[@value='BLR']")).click();
            // Step 4 : Select Chennai from the options.
//            driver.findElement(By.xpath("(//a[@value='MAA'])[2]")).click();
            driver.findElement(By.xpath("/html/body/form/div[4]/div[2]/div/div[5]/div[2]/div[2]/div[2]/div[3]/div/div[3]/div/div[2]/div[2]/div/table/tbody/tr[2]/td[2]/div[3]/div[1]/div/ul[1]/li[7]/a")).click();

            // Close browser after tests completion.
            Thread.sleep(4000);
            driver.quit();
        }
        catch (Exception E){
            E.printStackTrace();
        }
    }
}
