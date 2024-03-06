package SchedulePro;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;

public class e2e {
    public static void main(String[] args) {
        // First end to end testing for the Flight Booking site.
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        try{
            // Get the URL from testdata.properties
            Properties prop = new Properties();
            File file = new File("testdata.properties");
            FileInputStream fis  = new FileInputStream(file);
            prop.load(fis);

            String TEST_URL = prop.getProperty("dropdownTestingUrl");

            // Todo -> Step 0 : Opening the browser and hitting the URL.
            driver.get(TEST_URL);


            // Todo -> Step 1 : Select the departure city
            driver.findElement(By.id("ctl00_mainContent_ddl_originStation1_CTXT")).click();
            driver.findElement(By.xpath("//*[@id=\"dropdownGroup1\"]/div/ul[1]/li[6]/a")).click();

            Thread.sleep(3000);
            // Wait for 2 seconds.
            // Todo -> Step 2 : Select the arrival city
            driver.findElement(By.xpath("(//*[@id=\"dropdownGroup1\"]/div/ul[1]/li[7]/a)[2]")).click();

            // Todo -> Step 3 : Select the depart date
//            driver.findElement(By.id("ctl00$mainContent$view_date1")).click();
            Thread.sleep(4000);
            driver.findElement(By.className("ui-state-highlight")).click();
            // Selecting Depart date as today.

            // Todo -> Step 4 : Add 4 adult passengers
            driver.findElement(By.id("divpaxinfo")).click();
            Thread.sleep(3000);

            // Let's add 5 adults as the number of passengers.

            int passenger_count = 4;
            while(passenger_count > 0){
                driver.findElement(By.id("hrefIncAdt")).click();
                passenger_count--;
            }

            Thread.sleep(2000);
            driver.findElement(By.id("btnclosepaxoption")).click();

            Thread.sleep(3000);

            // Todo -> Step 5 : Click on the checkbox Senior Citizen.
            driver.findElement(By.id("ctl00_mainContent_chk_SeniorCitizenDiscount")).click();

            // Todo : Final step : Click on the search button
           Thread.sleep(2000);
           driver.findElement(By.id("ctl00_mainContent_btn_FindFlights")).click();


           // Algo verify that , One way is selected and Return Date selection is disabled.

            // Todo : Addition/extra -> Verify that the return date option is disabled.

            if(driver.findElement(By.id("Div1")).getAttribute("style").contains("0.5")){
                System.out.println("Return date is disabled.");
                Assert.assertTrue(true);
            }
            else {
                System.out.println("Return date is still enabled");
                Assert.fail();
            }

            System.out.println("All tests have been passed.");
            // Close the browser after completion of all the tests.
            Thread.sleep(5000);
            driver.quit();
        }
        catch (Exception E){
            E.printStackTrace();
        }
    }
}
