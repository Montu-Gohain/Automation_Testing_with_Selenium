package SchedulePro;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;

public class ScheduleAnAppointment {
    public static void main(String[] args) {
        // Todo : Setting up ExtentReport package to give us test results.
        ExtentReports extent = new ExtentReports();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = sdf.format(new Date());
        String filename = "target/Reports/ExtentReport_testing_PO_appointment_schedule-" + timestamp + ".html";
        ExtentSparkReporter spark = new ExtentSparkReporter(filename);
        spark.config().setTheme(Theme.DARK);
        extent.attachReporter(spark);

        // Todo : Initializing the Webdriver in our case ChromeDriver.

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1500,968");
        WebDriver driver = new ChromeDriver(options);
        // Due to longer loading times  let's add an implicit wait of 3 seconds
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(180));
        try{
            Properties prop = new Properties();
            File file = new File("testdata.properties");
            FileInputStream fis = new FileInputStream(file);
            prop.load(fis);

            String TEST_URL =  prop.getProperty("scheduleProUrl");

            // Step 0 : Open the browser and hit the url
            driver.get(TEST_URL);

//             Step 1. Login
            Login(driver,  prop);

//            Step 2 : Navigate to PO Management Page
            Navigate_To_PO_management(driver, prop, extent);

//            Step 3 : To Schedule an existing PO which is not yet scheduled.
            SchedulePO(driver, prop, extent);

            Thread.sleep(3000);
            // Final wait before closing the browser.
        }

        catch (Exception E){
            E.printStackTrace();
            System.out.println("Something went wrong, Test failed");
        }
        driver.quit();
        System.out.println("Performed all tests.");
        extent.flush();
    }
    public  static void Login(WebDriver driver, Properties prop){
        try{
            System.out.println("Logging in with credentials");
            String userEmail = prop.getProperty("userEmail");
            String userPassword = prop.getProperty("userPass");

//            Clearing existing input
            // Step 1 : Login
            driver.findElement(By.xpath("//input[@placeholder=\"Enter Email\"]")).sendKeys(userEmail);
            Thread.sleep(2000);

            driver.findElement(By.xpath("//input[@name=\"password\"]")).sendKeys(userPassword);
            Thread.sleep(2000);
            // Finally click on Login button.
            driver.findElement(By.className("btn-submit")).click();
            Thread.sleep(8000);



            System.out.println("We've Reached the home page");
        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Something went wrong, Test failed");
        }
    }

    public  static void Navigate_To_PO_management(WebDriver driver, Properties prop,ExtentReports extent){

        String test1_desc = prop.getProperty("PO_Schedule_test1_desc");

        ExtentTest test_navigate_to_PO = extent.createTest("Navigate to PO Management Page", test1_desc);

        try{
            System.out.println("Let's navigate to PO Management Page");

            test_navigate_to_PO.info("Clicking on the PO Management link from the left side menu");
            WebElement PO_Management_link = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[1]/div/div/ul/li[4]/a/div"));

            PO_Management_link.click();
            test_navigate_to_PO.info("Waiting to redirect to the PO Management Page");
            Thread.sleep(6000);

            String expected_url = prop.getProperty("expected_url_for_PO");
            String current_url = driver.getCurrentUrl();
            test_navigate_to_PO.info("Current URL : " + current_url);

            test_navigate_to_PO.info("Comparing the url with the Expected URL for this page.");
            if(Objects.equals(current_url, expected_url)){
                System.out.println("We've successfully reached PO Management Page");
                test_navigate_to_PO.pass("URLs matched , we've successfully reached PO Management Page");
            }else{
                System.out.println("Failed to Reach PO Management Page");
                test_navigate_to_PO.fail("Failed to react PO Management Page");
            }
        }
        catch (Exception E){
            E.printStackTrace();
        }
    }

    public static void SchedulePO(WebDriver driver, Properties prop, ExtentReports extent){
        try{

            String test2_desc = prop.getProperty("PO_Schedule_test2_desc");

            ExtentTest test_Select_PO_to_Schedule = extent.createTest("Selecting a PO to Schedule an Appointment", test2_desc);

            String targeted_PO_number = prop.getProperty("target_PO_no");
            test_Select_PO_to_Schedule.info("Received the target PO with PO Number : " + targeted_PO_number + " from testdata.properties file");


            String target_row_xpath = "//tr//td[text() = '" + targeted_PO_number + "']//parent::tr/td[8]/div";

            test_Select_PO_to_Schedule.info("Clicking on Not Scheduled button");
            driver.findElement(By.xpath(target_row_xpath)).click();

            test_Select_PO_to_Schedule.info("Waiting for 3 seconds to redirect to a different page , where we can commence Appointment creation.");
            Thread.sleep(3000);


            test_Select_PO_to_Schedule.info("Extracting text from the page to confirm successful selection of a PO");
            // Now verify that Click on Schedule to proceed text is visible.
            WebElement click_on_text = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div/div/div[2]/div/div[2]/span"));



            if(click_on_text.isDisplayed()){
                System.out.println("Now we can Schedule this PO");
               test_Select_PO_to_Schedule.pass("Test passed, we've successfully selected PO with PO number : " + targeted_PO_number + " to Schedule an Appointment");


               String test3_desc = prop.getProperty("PO_Schedule_test3_desc");
               ExtentTest test_Schedule_by_Recommendation = extent.createTest("Schedule this PO by the Recommended way",test3_desc);

               test_Schedule_by_Recommendation.info("Click on the blue Schedule button");
               // click on the Schedule button
                WebElement scheduleButton = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div/div/div[2]/div/div[1]/div/div[2]/div[2]/button"));
                scheduleButton.click();

                test_Schedule_by_Recommendation.info("Waiting for 2 seconds so that the options appear on the page");
                Thread.sleep(2000);

                // Select Recommendation option
                test_Schedule_by_Recommendation.info("Choose Recommendation option");
                driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div/div/div[2]/div/div[2]/div/div/div[1]/div[2]/button")).click();
                Thread.sleep(1500);


                test_Schedule_by_Recommendation.info("Click on the next button");
                // Click on Next button
                WebElement next_button = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div/div/div[2]/div/div[2]/div/div/div[2]/button"));
                next_button.click();


                Thread.sleep(2000);
                // Verify  that we've reached the next section which is date and tiem pick
                WebElement date_time_select_text = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div/div/div[2]/div/div[2]/div/div[1]/div[1]/p"));

                if(date_time_select_text.isDisplayed()){
                    test_Schedule_by_Recommendation.pass("We've Chosen the recommended way and reached at the date and time picker section");
                }
                else{
                    test_Select_PO_to_Schedule.fail("Failed to chose the Recommend way , hence failed to Schedule Appointment for the PO with PO number : " + targeted_PO_number);
                }

                String test4_desc = prop.getProperty("PO_Schedule_test4_desc");
                ExtentTest test_Select_Date_and_Time = extent.createTest("Select Date and Time of Appointment.",test4_desc );

                // Select Date
                test_Select_Date_and_Time.info("Click on the right arrow in the calender component to go to the next month.");
                driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div/div/div[2]/div/div[2]/div/div[1]/div[1]/div/div[1]/div[1]/button[2]")).click();
                Thread.sleep(1300);
                test_Select_Date_and_Time.info("Selecting Appointment Date as : 8th of March");
                driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div/div/div[2]/div/div[2]/div/div[1]/div[1]/div/div[1]/div[3]/button[8]")).click();

                test_Select_Date_and_Time.info("Waiting 4 seconds for the available time slots to appear.");
                Thread.sleep(4600);
                // Select Time
                test_Select_Date_and_Time.info("Click on the first time slot of the day");
                driver.findElement(By.xpath("(//div[@class='time-slot-ss'])[1]")).click();

                // Click on the Next Button
                test_Select_Date_and_Time.info("Clicking on the next button after selecting Date and Time properly");
                driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div/div/div[2]/div/div[2]/div/div[2]/div[2]/button[2]")).click();


                test_Select_Date_and_Time.pass("Successfully chosen Date and Time slot to Schedule the Appointment.");
                // Now we select the Carrier
                // Select Carrier
                ExtentTest testSelectCarrier = extent.createTest("Select Carrier", "Select Carrier");

                try {
                    driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div/div/div[2]/div/div[2]/div/div[2]/div/div[1]/div/div/div/div[2]/div/div[1]/div[2]/input")).click();
                    System.out.println("Click on select carrier");
                    testSelectCarrier.info("Click on select carrier");
                    Thread.sleep(1000);

                    driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div/div/div[2]/div/div[2]/div/div[2]/div/div[1]/div/div/div/div[2]/div/div[1]/div[2]/input")).sendKeys("TEST");
                    System.out.println("Enter Carrier Name: " + "TEST");
                    testSelectCarrier.info("Enter Carrier Name: " + "TEST");
                    Thread.sleep(7000);

//                    try {
                    String selectedCarrier = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div/div/div[2]/div/div[2]/div/div[2]/div/div[1]/div/div/div/div[2]/div[2]/div[1]/div[text()='" + "TEST" + "']")).getText();
                    driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div/div/div[2]/div/div[2]/div/div[2]/div/div[1]/div/div/div/div[2]/div[2]/div[1]/div[text()='" + "TEST"+ "']")).click();

                    System.out.println("Select carrier: " + selectedCarrier);
                    testSelectCarrier.pass("Select carrier: " + selectedCarrier);

//
//
//
//
//                    } catch (Exception E) {
//                        System.out.println("Select Carrier failed");
//                        testSelectCarrier.fail("Failed to Select Carrier field");
//
//                    }


                } catch (Exception E) {
                    System.out.println("Select Carrier unsuccessful");
                    testSelectCarrier.fail("Select Carrier unsuccessful");
                }

                String test_desc = prop.getProperty("PO_Schedule_test5_desc");

                ExtentTest test_schedule_PO_final = extent.createTest("Schedule Appointment for a PO",test_desc);
//                After selecting the carrier we can now finally click on the submit button
                WebElement submit_button = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[2]/div/div/div[2]/div/div[2]/div/div[2]/div/div[2]/button[2]"));
                submit_button.click();

                test_schedule_PO_final.info("Checking for Appointment Successful message.");

                String appointment_successful_msg = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[2]/div/div/div[2]/div/div[2]/div/div/p")).getText();
                System.out.println(appointment_successful_msg);

                test_schedule_PO_final.info("Collecting Appointment Date and Time");
                Thread.sleep(3000);

                String ap_date = driver.findElement(By.xpath("(//span[@class='dateTime-value'])[1]")).getText();
                String ap_time = driver.findElement(By.xpath("(//span[@class='dateTime-value'])[2]")).getText();

                test_schedule_PO_final.pass("Successfully Scheduled an Appointment for PO number : " + targeted_PO_number + " with date : " + ap_date + " and Time : " + ap_time);
            }
            else{
                System.out.println("Can't Schedule this PO, we are not in the correct page.");
            }

        }
        catch (Exception E){
            E.printStackTrace();
        }
    }

}
