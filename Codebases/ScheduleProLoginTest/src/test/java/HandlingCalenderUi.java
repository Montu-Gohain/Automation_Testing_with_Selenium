import net.bytebuddy.asm.Advice;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;

public class HandlingCalenderUi {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();

        try{
            Properties prop = new Properties();
            File file  = new File("testdata.properties");
            FileInputStream fis = new FileInputStream(file);
            prop.load(fis);

            String TEST_URL = prop.getProperty("dropdownTestingUrl");

            // Step 0 : Open the browser and hit the URL

            driver.get(TEST_URL);

            // Step 1 : Is to select Round Trip from the radio button.
            driver.findElement(By.xpath("//*[@id=\"ctl00_mainContent_rbtnl_Trip_1\"]")).click();

            // Step 2 : Select Departure place
            driver.findElement(By.id("ctl00_mainContent_ddl_originStation1_CTXT")).click();
            driver.findElement(By.xpath("//*[@id=\"dropdownGroup1\"]/div/ul[1]/li[6]/a")).click();

            Thread.sleep(2000);

            // Step 3 : Select Arrival place
            driver.findElement(By.xpath("(//*[@id=\"dropdownGroup1\"]/div/ul[1]/li[7]/a)[2]")).click();


            // Step 4 : Selecting Departure date

            Thread.sleep(3000);
            // Let's select today's date as the departure date.
//            driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/div[1]/table/tbody/tr[3]/td[3]/a")).click(); // Working fix
            driver.findElement(By.className("ui-state-highlight")).click(); // Correct way to select the current date as the departure date

            // Step 5 : To select one way again and check of the arrival date calender section is disabled or not.

            Thread.sleep(3000); // Adding 3 seconds of delay.
            driver.findElement(By.id("ctl00_mainContent_rbtnl_Trip_0")).click();
            System.out.println("Selecting one way again.");
            Thread.sleep(2000);
            // Now let's check if the second calender option is disabled or not.
            Assert.assertFalse(driver.findElement(By.id("ctl00_mainContent_view_date2")).isEnabled());
            System.out.println("The second calender is disabled.");
            // Close the browser after completion of all the tests
            Thread.sleep(3000);
            driver.quit();
        }
        catch (Exception E){
            E.printStackTrace();
        }
    }
}
