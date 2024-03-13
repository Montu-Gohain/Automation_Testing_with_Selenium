package WegmansGuard;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

public class ReCheckIn {
    public static void main(String[] args) {
        // Todo : Setting up ExtentReport package to give us test results.
        ExtentReports extent = new ExtentReports();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = sdf.format(new Date());
        String filename = "target/Reports/ReCheckIn-" + timestamp + ".html";
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
            File file = new File("testdata_wegmansguard.properties");
            FileInputStream fis = new FileInputStream(file);
            prop.load(fis);

//            Auto sign in to the Check in Page
            auto_sign_in(driver, prop,extent);

//            Edit Check In
            Re_check_in(driver, prop, extent);

            Thread.sleep(3900); // Wait for 3.9 seconds before closing the browser.
            driver.quit();
            extent.flush();
        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Tests failed , something went wrong.");
        }
    }
    public static void auto_sign_in(WebDriver driver, Properties prop,ExtentReports extent ){
        try{
            ExtentTest test_auto_signIn = extent.createTest("Testing Auto SignIn", "In this test we're check if we're able to see the " +
                    "Auto Sign In feature of WegmansGuard site.");

            test_auto_signIn.info("Grabbing the Test Site Url from testdata file");
            String TEST_URL = prop.getProperty("testUrl");
            System.out.println("Opening up the Test url");
            test_auto_signIn.info("Opening the url in the browser.");
            driver.get(TEST_URL);

            // Now we'll check if we've reached the page which we've been looking for
            WebElement select_an_option_text = driver.findElement(By.xpath("//span[@class=\"inputTitle\"]"));

            test_auto_signIn.info("Checking if we can see the Select a Type text in the Page.");
            if(select_an_option_text.isDisplayed()){
                System.out.println("Test passed,since the target text is shown in the page.");
                test_auto_signIn.pass("Test passed, We've successfully reached the Check In page.");
            }
            else{
                System.out.println("Test failed,target text is not visible on the page.");
                test_auto_signIn.fail("Test Failed, Failed to reach the Check In Page");
            }
        }
        catch (Exception E){
            E.printStackTrace();
        }
    }
    public static void Re_check_in(WebDriver driver, Properties prop, ExtentReports extent){
        try{

            ExtentTest test_navigate_to_checkInFormPage = extent.createTest("Navigate to CheckIn Form Page.", "In this test we're entering Trailer no. of already Checked " +
                    "In live truck and checking if we'll get informed that It is already Checked In or not.");


            test_navigate_to_checkInFormPage.info("Selecting Other Load option");
            System.out.println("Selecting Other Load");
            driver.findElement(By.xpath("//input[@value=\"Other Load\"]")).click();

            System.out.println("Let's Enter Details in Check In Page");
            // Let's enter driver trailer number and driver phone number.
            test_navigate_to_checkInFormPage.info("Grabbing the Trailer no and Driver's Phone number from testdata file");
            String driver_trailer_no = prop.getProperty("test4_trailer_no");
            String driver_phone_no = prop.getProperty("test4_phone_no");

            test_navigate_to_checkInFormPage.info("Entering Trailer no : " + driver_trailer_no);
            driver.findElement(By.xpath("//input[@name='po']")).sendKeys(driver_trailer_no);
            Thread.sleep(2000);
            test_navigate_to_checkInFormPage.info("Entering Driver Phone no : " + driver_phone_no);
            driver.findElement(By.xpath("//input[@name='phoneNo']")).sendKeys(driver_phone_no);

            test_navigate_to_checkInFormPage.info("Clicked on Search Button.");
            driver.findElement(By.xpath("//button[@class='loginButton w-25']")).click();
            System.out.println("Clicked on the Search Button.");
            Thread.sleep(5000);

            WebElement verify_driver_info_text = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[3]/div/div/div[2]/span"));

            if(verify_driver_info_text.isDisplayed()){
                test_navigate_to_checkInFormPage.pass("Test passed, we've reached Check In Form page successfully.");
            }
            else{
                test_navigate_to_checkInFormPage.fail("Test failed, we've failed to reach Check In Form Page");
            }

//            Todo : Test 2 => Checking if Load is already checked in message appearing or not.

          ExtentTest test_upating_values = extent.createTest("Updating existing values","In this test we are updating the existing values in certain fields.");
            // To verify that we are Re Check In , the mandatory fields should be prefilled.

            String current_driver_firstname = driver.findElement(By.xpath("//input[@name='DFirstName']")).getAttribute("value");
            String current_driver_lastname = driver.findElement(By.xpath("//input[@name='DlastName']")).getAttribute("value");
            String current_driver_phone_no = driver.findElement(By.xpath("//input[@name='phone_number']")).getAttribute("value");
            String current_driver_license_no = driver.findElement(By.xpath("//input[@name='DLicenceNo']")).getAttribute("value");


            test_upating_values.info("Extracting current data ,linked with trailer no : " + driver_trailer_no);

            test_upating_values.info("Current Driver name : " + current_driver_firstname + " " + current_driver_lastname);
            test_upating_values.info("Current Phone number : " + current_driver_phone_no);
            test_upating_values.info("Current Driver License number : " + current_driver_license_no);

            // Todo : Let's update some of the fields.

            String new_carrier_code = "OSSINT99049993";
            String new_vendor = "SSSKK";
            String new_trailer_name = "F 16";
            String new_comments = "This load is quite heavy.";

            clear_input_field(driver, "//input[@name='carrierCode']");
            test_upating_values.info("Updating carrier code to : " +  new_carrier_code);
            driver.findElement(By.xpath("//input[@name='carrierCode']")).sendKeys(new_carrier_code);

            clear_input_field(driver, "//input[@name='shipmentId']");
            test_upating_values.info("Updating Vendor name to : " +  new_vendor);
            driver.findElement(By.xpath("//input[@name='shipmentId']")).sendKeys(new_vendor);


            clear_input_field(driver, "//input[@name='trailerName']");
            test_upating_values.info("Updating Trailer name to : " +  new_trailer_name);
            driver.findElement(By.xpath("//input[@name='trailerName']")).sendKeys(new_trailer_name);


            clear_input_field(driver, "//input[@name='memo']");
            test_upating_values.info("Updating Comments to : " +  new_comments);
            driver.findElement(By.xpath("//input[@name='memo']")).sendKeys(new_comments);



            System.out.println("Clicking on the Re-Check In Button");
            driver.findElement(By.xpath("//button[@type='button' and text()=\"Re-Check in\"]")).click();
            test_upating_values.info("Clicked on the Re-Check In Button");

            Thread.sleep(5000);

            WebElement re_checkin_success_msg = driver.findElement(By.xpath("//span[text()='Arrival Completed Successfully.']"));

            if(re_checkin_success_msg.isDisplayed()){
                test_upating_values.pass("Test passed, since success message is showing as : " + re_checkin_success_msg.getText());
                System.out.println("Arrival Completed Successfully.");
            }
            else{
                test_upating_values.fail("Test Failed, since there is no success message showing.");
                System.out.println("Test failed, not showing success message of Arrival Completed Successfully.");
            }

            ExtentTest test_validate_updated_values = extent.createTest("Validated the newly updated values", "In this test we'll verify that the updated values are showing once we Re-CheckIn");


            test_validate_updated_values.info("Clicking on the back button.");
            driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[3]/div/div/div/button")).click();

            Thread.sleep(5000);
            test_validate_updated_values.info("Re Entering Driver trailer no. and any random Phone number.");
            test_validate_updated_values.info("Again Entering Trailer no : " + driver_trailer_no);
            driver.findElement(By.xpath("//input[@name='po']")).sendKeys(driver_trailer_no);
            Thread.sleep(2000);
            test_validate_updated_values.info("Again Entering Driver Phone no : " + driver_phone_no);
            driver.findElement(By.xpath("//input[@name='phoneNo']")).sendKeys(driver_phone_no);

            test_validate_updated_values.info("Clicked on Search Button.");
            driver.findElement(By.xpath("//button[@class='loginButton w-25']")).click();
            System.out.println("Clicked on the Search Button.");
            Thread.sleep(5000);

//            Todo : This time we'll verify the updated values which we just entered earlier , are they getting stored and updated or not, we'll test out this.
            test_validate_updated_values.info("Comparing the updated fields values.");
            boolean matching_carrier_code = new_carrier_code.equals(driver.findElement(By.xpath("//input[@name='carrierCode']")).getAttribute("value"));
            boolean matching_trailer_name = new_trailer_name.equals(driver.findElement(By.xpath("//input[@name='trailerName']")).getAttribute("value"));
            boolean matching_vendor_name = new_vendor.equals(driver.findElement(By.xpath("//input[@name='shipmentId']")).getAttribute("value"));
            boolean matching_comments = new_comments.equals(driver.findElement(By.xpath("//input[@name='memo']")).getAttribute("value"));

            if(matching_carrier_code && matching_trailer_name && matching_vendor_name && matching_comments){
                test_validate_updated_values.pass("Test passed, Since all the updated values are stored and got updated in the database.Hence Successful Re-CheckIn");
                System.out.println("Test passed, updated fields matched");
            }
            else{
                test_validate_updated_values.fail("Re-Check In failed due to not updated data after the earlier Re-CheckIn");
                System.out.println("Test failed, updated fields do not match.");
            }

        }
        catch (Exception E){
            E.printStackTrace();
        }
    }
    public static void clear_input_field(WebDriver driver, String target_xpath){
        try{
            driver.findElement(By.xpath(target_xpath)).sendKeys(Keys.CONTROL,"A");
            driver.findElement(By.xpath(target_xpath)).sendKeys(Keys.DELETE);

        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Something went wrong.");
        }
    }

}
