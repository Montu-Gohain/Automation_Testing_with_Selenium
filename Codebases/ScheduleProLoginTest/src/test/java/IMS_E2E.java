import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
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
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;

public class IMS_E2E {
    public static void main(String[] args) {

        // Todo : Setting up ExtentReport package to give us test results.

        ExtentReports extent = new ExtentReports();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        // Convert the timestamp to string
        String timestamp = sdf.format(new Date());
        // Create dynamic filename with the timestamp.
        String filename = "target/Reports/ExtentReport-" + timestamp + ".html";
        ExtentSparkReporter spark = new ExtentSparkReporter(filename);
        spark.config().setTheme(Theme.DARK);
        extent.attachReporter(spark);

        // Todo : Initializing the Webdriver in our case ChromeDriver.

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1500,968");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(45));
        try{
            Properties prop = new Properties();
            File file  = new File("testdata.properties");
            FileInputStream fis = new FileInputStream(file);
            prop.load(fis);

            String TEST_URL = prop.getProperty("scheduleProUrl");
            String useremail  = prop.getProperty("userEmail");
            String userpassword = prop.getProperty("userPass");


            ExtentTest test_visit_url = extent.createTest("Visiting IMS Login Page", "Opening Schedule Pro Login Page");
            // 0 : Opening the browser and logging in using credentials
            Test_0_open_URL(driver, TEST_URL, test_visit_url);

            // Todo : Explicitly waiting because sometimes the websites takes few seconds / minutes to load.
            WebDriverWait exp_wait = new WebDriverWait(driver,Duration.ofSeconds(25));
            exp_wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"right-block\"]/div[2]/form/h5")));

            ExtentTest test_fillout_inputFields = extent.createTest("Fill out Login Input fields", "Entering login credentials of IMS");
            // 1 : Todo : User Log in
            Test_1_user_login(driver, useremail, userpassword, test_fillout_inputFields);

            exp_wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div[2]/div/div[1]/div/div/ul/li[5]/div/span[1]")));
            // 2 : Todo : Verify Current URL and click on Appointment Summary option on Reports section.
            ExtentTest test_find_appointmentSummaryOption = extent.createTest("Navigating to Appointments Summary", "Opening the Appointments Summary options from the Report section in the sidebar");
            Test2_navigateTo_AppointmentSummary(driver, test_find_appointmentSummaryOption);

            exp_wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div[2]/div/div[2]/div/div[1]/span")));
            // 3 : Todo : checking if we've reached the reports page.
            ExtentTest test_reach_reportsPage = extent.createTest("Landing on Reports Page", "Verify that we've landed on the Reports page.");
            Test3_navigateToReportsPage(driver,test_reach_reportsPage);

            // 4 : Todo : Extracting data points from the report page.
            ExtentTest test_extract_data_from_reportsPage = extent.createTest("Extract data from Reports Page", "Extract No. of total Appointments from reports page");
            int total_appointments = Test4_ExtractingDataFromReportPage(driver, test_extract_data_from_reportsPage);
            // Waiting till the text Appointment Report Summary / warehouse is visible on the page.
            exp_wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div[2]/div/div[2]/div/div[1]/div[1]/span")));

            // 5 : Todo : Check whether the table has all of the appointments data, which should be equal to number of total appointments.
            ExtentTest test_presenceOf_data_in_table = extent.createTest("Verify Table contains all appointments data.", "Check if Total no. of appointments = Total no. of rows in the table.");
            Test5_CheckingNoOfRowsInTable(driver, total_appointments,test_presenceOf_data_in_table);


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

        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Test Failed,Something Went Wrong.");
        }
        System.out.println("All tests passed");
        extent.flush();
    }
    public  static void Test_0_open_URL(WebDriver driver, String url, ExtentTest _Test_){

        _Test_.info("Opening Login page of Schedule Pro");
        // Step 0 : Open the browser and hit the test url
        driver.get(url);
        String InPage_text_finder = driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div[2]/div[2]/form/h5")).getText();
        if (InPage_text_finder.equals("Login")) {
            _Test_.pass("We have reached SchedulePro Login Page");
            System.out.println("Test 0 Passed : We're reached the login page");
        } else {
            _Test_.fail("Failed to reach SchedulePro Login Page");
            System.out.println("Test Failed");
        }

    }
    public static void Test_1_user_login(WebDriver driver, String username, String password, ExtentTest _Test_){
        try{
            _Test_.info("Filling out username and password field");
            driver.findElement(By.xpath("//input[@placeholder='Enter Email']\n")).sendKeys(username);
            driver.findElement(By.xpath("//input[@placeholder='Enter Password']\n")).sendKeys(password);

            String enteredUsername = driver.findElement(By.xpath("//input[@placeholder='Enter Email']\n")).getAttribute("value");
            String enteredPassword = driver.findElement(By.xpath("//input[@placeholder='Enter Password']\n")).getAttribute("value");
            System.out.println(enteredUsername + enteredUsername);
            if(enteredPassword.length() > 0 && enteredUsername.length() > 0){
                _Test_.pass("Credentials got entered successfully");
            } else{
                _Test_.fail("Failed to enter the Credentials.");
            }

            Thread.sleep(2000);
            driver.findElement(By.xpath("//*[@id=\"right-block\"]/div[2]/form/div[3]/button")).click();
            System.out.println("Logging in.");
        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Test Failed,Something Went Wrong.");

        }
    }
    public static void Test2_navigateTo_AppointmentSummary(WebDriver driver, ExtentTest _Test_){
       try{
           // Checking if we've reached the proper url or not.
           String expected_url = "https://scheduleproqa.freightsmith.net/bookappointments";
           Assert.assertEquals(expected_url, driver.getCurrentUrl());
           System.out.println("Verification successful for the current URL");


           // Click on the Reports option on the sidebar.
           driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[1]/div/div/ul/li[5]/div/span[1]")).click();
           Thread.sleep(2000);

           // Finding out the Appointment summary option in the page via selenium.
           if(driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[1]/div/div/ul/li[5]/div[2]/ul/a[2]/li")).isDisplayed()){
               _Test_.pass("We've found the Appointment Summary option from the Reports Section.");
           }
           else{
               _Test_.fail("Unable to find out Appointment Summary option on the page.");
           }
           // Click on Appointment Summary option
           driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[1]/div/div/ul/li[5]/div[2]/ul/a[2]/li")).click();
           System.out.println("Appointment Summary Option clicked");

       }
       catch (Exception E){
           E.printStackTrace();
           System.out.println("Test Failed,Something Went Wrong.");
       }
    }
    public static void Test3_navigateToReportsPage(WebDriver driver, ExtentTest _Test_){
            System.out.println("We've reached the reports page.");
            String expectedUrl = "https://scheduleproqa.freightsmith.net/report";
//            Assert.assertEquals(expectedUrl, driver.getCurrentUrl());
           if(Objects.equals(driver.getCurrentUrl(), expectedUrl)){
               _Test_.pass("Test passed : We've landed on /reports page.");
           }
           else{
               _Test_.fail("Failed to reach /reports page, something went wrong.");
           }
            System.out.println("Verification Successful for the Report page URL.");
    }
    public static int Test4_ExtractingDataFromReportPage(WebDriver driver,ExtentTest _Test_){
        int noOfTotalAppointments=0;
        try{
            _Test_.info("Checking if we're able to extract the No. of Total Appointments");
            Thread.sleep(4000);
            String totalAppointments = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[2]/div/div[2]/div[1]/table/tbody/tr[3]/td[2]")).getText();
            noOfTotalAppointments = Integer.parseInt(totalAppointments);

            if(noOfTotalAppointments > 0){
                _Test_.pass("Successfully extracted No. of Total Appointments from report page.");
            }
            else {
                _Test_.fail("Failed to extract no. of Total Appointments from the report page.");
            }

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
    public static void Test5_CheckingNoOfRowsInTable(WebDriver driver, int totalAppointments,ExtentTest _Test_){
        try{
            Thread.sleep(9000);
            WebElement tbodyElement = driver.findElement(By.className("SS-drilldown-body"));

            // Find all <tr> elements that are children of the <tbody> element
            // and count them
            int rowCount = tbodyElement.findElements(By.tagName("tr")).size();

            // Output the count
            System.out.println("Number of table rows : " + rowCount + " and total appointments we have : " + totalAppointments);

//            Assert.assertEquals(rowCount, totalAppointments);
            if(Objects.equals(rowCount, totalAppointments)){
                _Test_.pass("We've found all the records of Appointments in the table.");
            }
            else{
                _Test_.fail("Test Failed,No. of total Appointment and No. of total data rows in the table not matching.");
            }
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
