package SchedulePro.today;

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
import java.util.List;
import java.util.Properties;

public class Docks_and_timeslot {
    public static void main(String[] args) {
        // Todo : Setting up ExtentReport package to give us test results.
        ExtentReports extent = new ExtentReports();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = sdf.format(new Date());
        String filename = "target/Reports/Delete_an_appointment-" + timestamp + ".html";
        ExtentSparkReporter spark = new ExtentSparkReporter(filename);
        spark.config().setTheme(Theme.DARK);
        extent.attachReporter(spark);

        // Todo : Initializing the Webdriver in our case ChromeDriver.

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1500,968");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(120));

        try{
            Properties prop = new Properties();
            File file = new File("testdata.properties");
            FileInputStream fis = new FileInputStream(file);
            prop.load(fis);

            //    Login
            Login(driver,  prop, extent);

//            Navigate to Docks and Timeslot
            navigate_to_docks_and_timeslot(driver, extent);

//            Add a new Timeslot
            add_new_timeslot(driver,prop, extent);

//            Validate new Timeslot
            validate_new_timeslot(driver, prop, extent);

//            Enable / disable timeSlots
            enable_disable(driver, prop, extent);

//            Edit timeslot
            edit_timeslot(driver, prop, extent);


            Thread.sleep(3900); // Wait for 3.9 seconds before closing the browser.
            driver.quit();
//            extent.flush();
        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Tests failed , something went wrong.");
        }
    }
    public  static void Login(WebDriver driver, Properties prop, ExtentReports extent){
        try{

            String TEST_URL = prop.getProperty("scheduleProUrl");
            driver.get(TEST_URL);
            ExtentTest test_login = extent.createTest("Login Test","In this test we'll be logging in using valid credentials and test " +
                    "whether we've reached the dashboard page or not");

            System.out.println("Logging in with credentials");
            test_login.info("Grabbing the email and password from testdata file");

            String userEmail = prop.getProperty("userEmail");
            String userPassword = prop.getProperty("userPass");
//            Clearing existing input
            // Step 1 : Login
            test_login.info("Entering email as : " + userEmail);
            driver.findElement(By.xpath("//input[@placeholder=\"Enter Email\"]")).sendKeys(userEmail);
            Thread.sleep(2000);

            test_login.info("Entering password as : " + userPassword);
            driver.findElement(By.xpath("//input[@name=\"password\"]")).sendKeys(userPassword);
            Thread.sleep(2000);

            // Finally click on Login button.
            test_login.info("Clicking on Login button");
            driver.findElement(By.className("btn-submit")).click();
            Thread.sleep(8000);

            WebElement welcome_text = driver.findElement(By.xpath("//span[@class='banner-heading ms-2']"));

            if(welcome_text.isDisplayed()){
                test_login.pass("Test passed, we've successfully reached the Warehouse Dashboard page.");
                System.out.println("Login test passed");
            }
            else{
                test_login.fail("Test failed, we've failed to reach the Warehouse Dashboard page.");
                System.out.println("Login test failed");
            }


            System.out.println("We've Reached the home page");
        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Something went wrong, Test failed");
        }
    }

    public static void navigate_to_docks_and_timeslot(WebDriver driver, ExtentReports extent){

       try{
           ExtentTest test_nav_to_docks_and_timeslot = extent.createTest("Navigate to Docks and Timeslot","In this test we're navigating to docks and timeslot from the warehouse management page.");

           test_nav_to_docks_and_timeslot.info("Clicking on the Right pointing arrow at the bottom");
           driver.findElement(By.xpath("(//i[@class='fa-solid fa-angle-right'])[2]")).click();
           Thread.sleep(2000);

           test_nav_to_docks_and_timeslot.info("Choosing Schedule Admin from the options.");
           driver.findElement(By.xpath("//div[@class='ms-1' and text()='Schedule Admin']")).click();

           test_nav_to_docks_and_timeslot.info("Select Docks and TimeSlot from the top options.");
           driver.findElement(By.xpath("//button[@class='unselected-button' and text()='Docks & Timeslot']")).click();

           Thread.sleep(4000);

            WebElement Add_timeslot = driver.findElement(By.xpath("//button[text()='Add Timeslot']"));

            if(Add_timeslot.isDisplayed()){
                test_nav_to_docks_and_timeslot.pass("Test passed, we've reached the Docks and Timeslot page.");
            }
            else{
                test_nav_to_docks_and_timeslot.fail("Test failed, we've failed to reach Docks and Timeslot page.");
            }
       }
       catch (Exception E){
           E.printStackTrace();
           System.out.println("Test failed, Something went wrong.");
       }



    }

    public static void add_new_timeslot(WebDriver driver,Properties prp, ExtentReports extent){
        try{

            ExtentTest test_add_new_timeslot_validation = extent.createTest("Validating Add TimeSlot form","In this test we'll try to add a new Timeslot without providing the required values in the form" +
                    "and check whether we can see errors in the page or not.");

            test_add_new_timeslot_validation.info("Clicking on Add Timeslot button");
            driver.findElement(By.xpath("//button[text()='Add Timeslot']")).click();

            test_add_new_timeslot_validation.info("Without entering anything click on save button.");
            driver.findElement(By.xpath("//button[text()='Save']")).click();

            Thread.sleep(2000);

            List<WebElement> list_of_error_messages = driver.findElements(By.xpath("//span[@class='errorMessage']"));

            if(list_of_error_messages.size() > 0){
                WebElement error_message = driver.findElement(By.xpath("(//span[@class='errorMessage'])[1]"));
                test_add_new_timeslot_validation.pass("Test passed, error message is shown as : " + error_message.getText());
                System.out.println("Form validation is working perfectly");
            }
            else {
                System.out.println("Form validation is not working");
                test_add_new_timeslot_validation.fail("Test failed, no error message is shown. Hence form validation failed.");
            }

//            Todo: Let's add a New Timeslot.

            ExtentTest test_add_new_timeslot = extent.createTest("Create a new Time Slot","In this test we're adding new time slot.");

            List<WebElement> all_time_slots = driver.findElements(By.xpath("//tbody/tr"));

            int timeslot_count_before = all_time_slots.size();
//            Currently total time slots.
            test_add_new_timeslot.info("No. of total timeslots available : " + timeslot_count_before);

            System.out.println("Before total rows : " + timeslot_count_before);

            test_add_new_timeslot.info("Clicking on Add Timeslot");
            driver.findElement(By.xpath("//button[text()='Add Timeslot']")).click();

            String dockType = prp.getProperty("dock_type");
            test_add_new_timeslot.info("Selecting Dock Type as : " + dockType);
            driver.findElement(By.xpath("//select[@name='IDDock']")).click();

            Thread.sleep(2000);

            driver.findElement(By.xpath("//option[text()='"+ dockType+"']")).click();

            String time_slot_no = prp.getProperty("no_of_timeslots");
            test_add_new_timeslot.info("Entering Timeslot value : " + time_slot_no );

            driver.findElement(By.xpath("//input[@name='TimeSlotName']")).sendKeys(time_slot_no);

            Thread.sleep(2000);

            test_add_new_timeslot.info("Selecting option : Schedule visible Externally.");

            driver.findElement(By.xpath("(//input[@type='radio'])[1]")).click();

            test_add_new_timeslot.info("Clicking on Save button.");

            driver.findElement(By.xpath("//button[text()='Save']")).click();

            Thread.sleep(5000);

//            Now total time slots.

            List<WebElement> all_timeslot_rows = driver.findElements(By.xpath("//tbody/tr"));

            if(all_timeslot_rows.size() > timeslot_count_before){
                test_add_new_timeslot.pass("Test passed, we've successfully added a new TimeSlot");
            }
            else{
                test_add_new_timeslot.fail("Test failed, we've failed to add a new time Slot");
            }
        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Tests failed, something went wrong.");
        }
    }

    public static void validate_new_timeslot(WebDriver driver, Properties prop, ExtentReports extent){
        try{
            ExtentTest test_newly_added_timeslot = extent.createTest("Testing values in Newly Added Timeslot","In this test we'll be validating the values in the newly added Timeslots");

            String time_slot_no = prop.getProperty("no_of_timeslots");
            String dockType = prop.getProperty("dock_type");

            WebElement targeted_data_row = driver.findElement(By.xpath("((//tbody/tr/td[3 and text()='"+time_slot_no+"'])//parent::*)[1]"));

            if(targeted_data_row.isDisplayed()){
                System.out.println("We've found the newly added Timeslot row.");

                String dock_type_in_row = driver.findElement(By.xpath("(//tbody/tr/td[3 and text()='"+time_slot_no+"'])//parent::*/td[2]")).getText();

                test_newly_added_timeslot.info("Entered value in Docktype : " + dockType + " and docktype found in the table row : " + dock_type_in_row);
                boolean matching_docktype = dockType.equals(dock_type_in_row);

                if(matching_docktype){

                    test_newly_added_timeslot.pass("Test passed, values are matching which we've entered during new Timeslot creation.");
                }
                else{
                    test_newly_added_timeslot.fail("Test failed, data got corrupted while creating new Timeslot");
                }

            }
            else{
                test_newly_added_timeslot.info("Newly Added timeslot not found in the table columns");
            }



        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Tests failed, something went wrong.");
        }
    }

    public static void enable_disable(WebDriver driver,Properties prop, ExtentReports extent){
        try{
            ExtentTest test_enable_disable = extent.createTest("Enable / Disable test","In this test we'll be checking whether the enable disable button is working or not.");

            String target_row_serial_no = prop.getProperty("target_sl_no");
            String current_state = driver.findElement(By.xpath("(//tbody/tr/td[1 and text()='"+target_row_serial_no+"'])//parent::*/td[5]")).getText();
            test_enable_disable.info("Current state of S.No " + target_row_serial_no + " timeslot : " + current_state );

            test_enable_disable.info("Clicking on the Disable button");
            driver.findElement(By.xpath("((//tbody/tr/td[1 and text()='"+ target_row_serial_no +"'])//parent::*/td[6]/div/span/button)[2]")).click();
            Thread.sleep(6000);

            String final_status = driver.findElement(By.xpath("(//tbody/tr/td[1 and text()='"+target_row_serial_no+"'])//parent::*/td[5]")).getText();

            if(final_status.equals("Disabled")){
                test_enable_disable.pass("Test passed, we've successfully changed the status of this Timeslot");
            }
            else{
                test_enable_disable.fail("Test failed, we've failed to change the status of the Timeslot.");
            }

        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Tests failed, something went wrong.");
        }
    }
    public static void edit_timeslot(WebDriver driver,Properties prop, ExtentReports extent){
        try{

            ExtentTest test_edit_timeslot = extent.createTest("Edit a timeslot", "In this test we're editing the value of the Timeslot field in an existing timeslot and verifying whether " +
                    "it got updated successfully.");

            test_edit_timeslot.info("Grabbing new timeslot count from testdata file");
            String new_time_slot_count = prop.getProperty("new_timeslot_no");
            String target_sl_no = prop.getProperty("target_edit_timeslot_sl_no");

            test_edit_timeslot.info("Clicking on the edit icon for Timeslot with Serial no : " + target_sl_no );





        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Tests failed, something went wrong.");
        }
    }
    public static void delete_timeslot(WebDriver driver, ExtentReports extent){
        try{

        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Tests failed, something went wrong.");
        }
    }

}
