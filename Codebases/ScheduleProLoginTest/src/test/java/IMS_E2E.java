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
//        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(180));
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
            ExtentTest test_presenceOf_data_in_table = extent.createTest("Verify if the table contains all appointments data.", "Check if Total no. of appointments = Total no. of rows in the table.");
            Test5_CheckingNoOfRowsInTable(driver, total_appointments,test_presenceOf_data_in_table, "Appointment by warehouse");

            exp_wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//i[contains(@class, 'fa-regular') and contains(@class, 'fa-calendar')])[2]")));
            ExtentTest test_findingDataWithCustomDates = extent.createTest("Find Data with Custom date range", "Search Appointments data with Custom Date range.");
            Test_6_get_appointmentsData_with_customDates(driver, test_findingDataWithCustomDates);

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
        driver.quit();
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
            Thread.sleep(5000);
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
           Thread.sleep(7000);

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
            _Test_.info("Checking whether number of Total Appointments is visible on the page.");
            Thread.sleep(4000);
            String warehouse_ap = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div/div[2]/div[1]/table/tbody/tr[1]/td[2]")).getText();
            String carrier_ap = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div/div[2]/div[1]/table/tbody/tr[2]/td[2]")).getText();

            int appointment_by_warehouse = Integer.parseInt(warehouse_ap);
            int appointment_by_carrier = Integer.parseInt(carrier_ap);

            if(appointment_by_carrier > 0 || appointment_by_warehouse > 0){
                _Test_.pass("We've found, Appointment by warehouse : " + appointment_by_warehouse + ", Appointment by carrier : " + appointment_by_carrier);
            }
            else{
                _Test_.fail("Failed to fetch Appointments Data");
            }
            _Test_.info("Retrieve Total number of Total Appointments");
            int total_appointments =appointment_by_carrier + appointment_by_warehouse;
            System.out.println("Warehouse appointments : " + appointment_by_warehouse + " Carrier Appointments : " + appointment_by_carrier + " Total Appointments : " + total_appointments);


            String totalAppointments = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[2]/div/div[2]/div[1]/table/tbody/tr[3]/td[2]")).getText();
            noOfTotalAppointments = Integer.parseInt(totalAppointments);

            if(total_appointments > 0 && total_appointments == noOfTotalAppointments){
                _Test_.pass("Total Appointments found : " + total_appointments);
            }
            else{
                _Test_.fail("Failed to retrieve number of Total Appointments");
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
    public static void Test5_CheckingNoOfRowsInTable(WebDriver driver, int totalAppointments,ExtentTest _Test_, String testSubject){
        try{
            Thread.sleep(17000);
            WebElement tbodyElement = driver.findElement(By.className("SS-drilldown-body"));
            _Test_.info("Compare number of rows in the table with number of " + testSubject);
            // Find all <tr> elements that are children of the <tbody> element
            // and count them
            int rowCount = tbodyElement.findElements(By.tagName("tr")).size();

            // Output the count
            System.out.println("Number of table rows : " + rowCount + " and total appointments we have : " + totalAppointments);

//            Assert.assertEquals(rowCount, totalAppointments);
            if(Objects.equals(rowCount, totalAppointments)){
                _Test_.pass("Number of rows in the data table is : " + rowCount + ", which matches with the value found through the previous test, which was also : " + totalAppointments);
            }
            else{
                _Test_.fail("Test Failed, number of total Appointments was : " + totalAppointments + " and number of rows in the data table : " + rowCount);
            }
            Thread.sleep(3000);

            _Test_.info("Click on the arrow to get back to the Reports page.");
            // Todo : Click on the back arrow and check we are getting redirected to the /reports page.
            driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[2]/div/div[1]/div[1]/span/i")).click();

            Thread.sleep(5000);
            String expectedUrl = "https://scheduleproqa.freightsmith.net/report";
            if(Objects.equals(expectedUrl, driver.getCurrentUrl())){
                _Test_.pass("We successfully redirected back to Reports page");
            }
            else{
                _Test_.fail("Failed to redirect to the Reports page.");
            }
            System.out.println("Back button is working as expected.");
        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Test Failed,Something Went Wrong.");

        }
    }
    public static void Test_6_get_appointmentsData_with_customDates(WebDriver driver, ExtentTest _Test_){
        try{
            Thread.sleep(9000);
            System.out.println("Testing out with custom dates");

            _Test_.info("Fetching Appointment data by selecting a custom date range");

            select_end_date(driver);

            Thread.sleep(8000);
            String warehouse_ap = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div/div[2]/div[1]/table/tbody/tr[1]/td[2]")).getText();
            String carrier_ap = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div/div[2]/div[1]/table/tbody/tr[2]/td[2]")).getText();

            int appointment_by_warehouse = Integer.parseInt(warehouse_ap);
            int appointment_by_carrier = Integer.parseInt(carrier_ap);

            if(appointment_by_carrier > 0 || appointment_by_warehouse > 0){
                _Test_.pass("We've found, Appointment by warehouse : " + appointment_by_warehouse + ", Appointment by carrier : " + appointment_by_carrier);
            }
            else{
                _Test_.fail("Failed to fetch Appointments Data");
            }
            _Test_.info("Retrieve Total number of Total Appointments");
            int total_appointments =appointment_by_carrier + appointment_by_warehouse;
            System.out.println("Warehouse appointments : " + appointment_by_warehouse + " Carrier Appointments : " + appointment_by_carrier + " Total Appointments : " + total_appointments);

            if(total_appointments > 0){
                _Test_.pass("Total Appointments found : " + total_appointments);
            }
            else{
                _Test_.fail("Failed to retrieve number of Total Appointments");
            }
            // Todo : Verification of data in the tables page.
            // Todo : 1. Click on the green part of the graph
            WebElement carrier_graph = driver.findElement(By.xpath("(//*[name()='svg']//*[name()='path'])[2]"));
            if(carrier_graph.isDisplayed()){
                carrier_graph.click();
            }
            Test5_CheckingNoOfRowsInTable(driver, appointment_by_carrier, _Test_, "Appointment by Carrier");
        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Test Failed, Something Went Wrong.");
        }
    }
    public static void select_end_date(WebDriver driver){
        // Open the end date Calendar Modal
        driver.findElement(By.xpath("(//i[contains(@class, 'fa-regular') and contains(@class, 'fa-calendar')])[3]")).click();
        // Selecting the end date
        driver.findElement(By.xpath("//button[@class='picker-day' and @data-day='28']")).click();

    }

}
