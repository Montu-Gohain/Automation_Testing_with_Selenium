import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;

public class IMS_E2E {
    public static void main(String[] args) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1500,968");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
//        driver.manage().window().maximize();
        try{
            Properties prop = new Properties();
            File file  = new File("testdata.properties");
            FileInputStream fis = new FileInputStream(file);
            prop.load(fis);

            String TEST_URL = prop.getProperty("scheduleProUrl");
            String useremail  = prop.getProperty("userEmail");
            String userpassword = prop.getProperty("userPass");

            // 0 : Opening the browser and logging in using credentials
            Test_0_open_URL(driver, TEST_URL);

            // Todo : Explicitly waiting because sometimes the websites takes few seconds / minutes to load.
            WebDriverWait exp_wait = new WebDriverWait(driver,Duration.ofSeconds(25));
            exp_wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"right-block\"]/div[2]/form/h5")));
            // 1 : Todo : User Log in
            Test_1_user_login(driver, useremail, userpassword);

            exp_wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div[2]/div/div[1]/div/div/ul/li[5]/div/span[1]")));
            // 2 : Todo : Verify Current URL and click on Appointment Summary option on Reports section.
            Test2_navigateTo_AppointmentSummary(driver);

            exp_wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div[2]/div/div[2]/div/div[1]/span")));
            // 3 : Todo : checking if we've reached the reports page.
            Test3_navigateToReportsPage(driver);

            // 4 : Todo : Extracting data points from the report page.
            int total_appointments = Test4_ExtractingDataFromReportPage(driver);
            // Waiting till the text Appointment Report Summary / warehouse is visible on the page.
            exp_wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div[2]/div/div[2]/div/div[1]/div[1]/span")));

            // 5 : Todo : Check whether the table has all of the appointments data, which should be equal to number of total appointments.
            Test5_CheckingNoOfRowsInTable(driver, total_appointments);


            Thread.sleep(6000);
            // Todo : Logout from IMS
            driver.findElement(By.xpath("/html/body/div/div[2]/div/div[1]/div/ul/li[2]/i")).click();
            Thread.sleep(3000);
            driver.findElement(By.xpath("/html/body/div/div[2]/div/div[1]/div/ul/li[2]/div[2]/ul/li[6]")).click();
            System.out.println("Logout option clicked , confirm message should appear");

            Thread.sleep(4000);
            driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div[3]/div/button[2]")).click();
            System.out.println("Logout Confirm Button Clicked");

            // After all of these close the browser.
            System.out.println("All tests passed");

        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Test Failed,Something Went Wrong.");
        }
    }
    public  static void Test_0_open_URL(WebDriver driver, String url){
        // Step 0 : Open the browser and hit the test url
        driver.get(url);

    }
    public static void Test_1_user_login(WebDriver driver, String username, String password){
        try{
            driver.findElement(By.xpath("//input[@placeholder='Enter Email']\n")).sendKeys(username);
            driver.findElement(By.xpath("//input[@placeholder='Enter Password']\n")).sendKeys(password);
            Thread.sleep(2000);
            driver.findElement(By.xpath("//*[@id=\"right-block\"]/div[2]/form/div[3]/button")).click();
            System.out.println("Logging in.");
        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Test Failed,Something Went Wrong.");

        }
    }
    public static void Test2_navigateTo_AppointmentSummary(WebDriver driver){
       try{
           // Checking if we've reached the proper url or not.
           String expected_url = "https://scheduleproqa.freightsmith.net/bookappointments";
           Assert.assertEquals(expected_url, driver.getCurrentUrl());
           System.out.println("Verification successful for the current URL");


           // Click on the Reports option on the sidebar.
           driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[1]/div/div/ul/li[5]/div/span[1]")).click();
           Thread.sleep(2000);

           // Click on Appointment Summary option
           driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[1]/div/div/ul/li[5]/div[2]/ul/a[2]/li")).click();
           System.out.println("Appointment Summary Option clicked");

       }
       catch (Exception E){
           E.printStackTrace();
           System.out.println("Test Failed,Something Went Wrong.");

       }
    }
    public static void Test3_navigateToReportsPage(WebDriver driver){
            System.out.println("We've reached the reports page.");
            String expectedUrl = "https://scheduleproqa.freightsmith.net/report";
            Assert.assertEquals(expectedUrl, driver.getCurrentUrl());
            System.out.println("Verification Successful for the Report page URL.");
    }
    public static int Test4_ExtractingDataFromReportPage(WebDriver driver){
        int noOfTotalAppointments=0;
        try{
            Thread.sleep(4000);
            String totalAppointments = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[2]/div/div[2]/div[1]/table/tbody/tr[3]/td[2]")).getText();

            noOfTotalAppointments = Integer.parseInt(totalAppointments);
            System.out.println("Number of total appointments : " + noOfTotalAppointments);
            // Todo : Let's click on the circular graph
          WebElement circularGraph = driver.findElement(By.xpath("(//*[name()='svg']//*[name()='path'])[1]"));
            if(circularGraph.isDisplayed()){
                circularGraph.click();
            }
        }
        catch(Exception E){
            E.printStackTrace();
            System.out.println("Test Failed,Something Went Wrong.");

        }
        return noOfTotalAppointments;

    }
    public static void Test5_CheckingNoOfRowsInTable(WebDriver driver, int totalAppointments){
        try{
            Thread.sleep(9000);
            WebElement tbodyElement = driver.findElement(By.className("SS-drilldown-body"));

            // Find all <tr> elements that are children of the <tbody> element
            // and count them
            int rowCount = tbodyElement.findElements(By.tagName("tr")).size();

            // Output the count
            System.out.println("Number of table rows : " + rowCount + " and total appointments we have : " + totalAppointments);

            Assert.assertEquals(rowCount, totalAppointments);

            Thread.sleep(9000);

            // Todo : Click on the back arrow and check we are getting redirected to the /reports page.
            driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[2]/div/div[1]/div[1]/span/i")).click();

            Thread.sleep(5000);
            String expectedUrl = "https://scheduleproqa.freightsmith.net/report";
            Assert.assertEquals(expectedUrl, driver.getCurrentUrl());
            System.out.println("Back button is working as expected.");

        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Test Failed,Something Went Wrong.");

        }
    }
}
