package WegmansGuard;

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

public class CheckIn {

    public static void main(String[] args) {
        // Todo : Setting up ExtentReport package to give us test results.
        ExtentReports extent = new ExtentReports();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = sdf.format(new Date());
        String filename = "target/Reports/CheckIn_with_validation-" + timestamp + ".html";
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
            auto_sign_in(driver, prop, extent);

//            Check In
            check_in(driver, prop, extent);

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
    public static void check_in(WebDriver driver, Properties prop, ExtentReports extent){
        try{

            // Todo : Check in Input Validation Start
            ExtentTest test_input_validation = extent.createTest("Testing out Input validation for Driver Trailer number and Driver Phone number", "In this test we're " +
                    "going to click the search button without entering Driver Trailer Number and Driver Phone Number. Doing that the user should see error message below these input fields");

            // Selecting other load

            test_input_validation.info("Let's select the Other load option");
            driver.findElement(By.xpath("//input[@value=\"Other Load\"]")).click();
            System.out.println("Selected the Other Load option");

            // Without Entering Driver trailer number and Driver Phone Number we click on Search Button.
            test_input_validation.info("Clicking on the Search button.");
            driver.findElement(By.xpath("//button[@class='loginButton w-25']")).click();
            System.out.println("Clicked on the Search Button.");

            // After this we should see error message below these input fields.
            test_input_validation.info("Checking whether the error messages are shown below the input fields are not.");

            Thread.sleep(4000);
            List<WebElement> error_message_elements =   driver.findElements(By.xpath("//span[@class=\"errorMessage\"]"));

            if(error_message_elements.size() == 2){
                String error_message_text = error_message_elements.get(1).getText();
                test_input_validation.pass("Test passed, Error messages with text :-" + error_message_text + " ,are being displayed below the input field.");
            }
            else{
                test_input_validation.fail("Test failed, Failed to see the error messages below the input fields.");
            }

            // Todo : Check in Input Validation End.
            Thread.sleep(3000);
            // ======================================     Let's do a Check In using Other Load option  ================================================
            String driver_trailer_no = prop.getProperty("trailer_number");
            String driver_phone_no = prop.getProperty("driver_phone_number");

            // Let's enter driver trailer number and driver phone number.
            driver.findElement(By.xpath("//input[@name='po']")).sendKeys(driver_trailer_no);
            Thread.sleep(2000);
            driver.findElement(By.xpath("//input[@name='phoneNo']")).sendKeys(driver_phone_no);
            driver.findElement(By.xpath("//button[@class='loginButton w-25']")).click();
            Thread.sleep(2000);
            System.out.println("Clicked on the Search Button.");

            // At this point we'll be in the Check in Form page, where we can enter the Trailer and driver details.
            // Todo : Testing out the Input Validation on Driver's Information form.

            ExtentTest test_driver_info_input_validation = extent.createTest("Testing out the input validation for Driver's Information.", "In this test we'll be clicking the Check In Now " +
                    "button without entering all the mandatory fields in the form. Here we are expecting to see error messages below the input fields where input is mandatory.");

            test_driver_info_input_validation.info("Clicking on the Check In Now button");
            driver.findElement(By.xpath("//button[text()='Check In Now']")).click();

            Thread.sleep(4000);

            // Now we should see error messages below these input fields.
            List<WebElement> error_messages_driver_info = driver.findElements(By.xpath("//span[@class=\"errorMessage\" and text()=\"This field is required\"]"));

            if(error_messages_driver_info.size() ==3){
                String error_msg = error_messages_driver_info.get(1).getText();
                test_driver_info_input_validation.info("Displaying error message : " + error_msg);
                test_driver_info_input_validation.pass("Test passed, input validation is working since we can see the error message for not entering the mandatory fields");
                System.out.println("Input validation is working in the driver information form");
            }
            else{
                System.out.println("Input validation is Not working in the driver information form");
                test_driver_info_input_validation.fail("Test failed, input validation is not working since there is no error message after leaving the mandatory fields blank.");
            }
            // Todo : Testing out the Input Validation on Driver's Information form Ending.

            // ======================================== Let's test out the CheckIn Form by providing required fields. ===================================================

            // Todo : Starting of CheckIn Feature ...

            ExtentTest test_checkIn_happy_flow = extent.createTest("Testing out the happy flow of CheckIn Feature with Other Load option selected", "In this test we are going to enter all the mandatory fields " +
                    "and we'll try to CheckIn Successfully.");

            test_checkIn_happy_flow.info("Grabbing the values for Driver's Information form , from testdata file.");
            String driver_first_name = prop.getProperty("driver_firstName");
            String driver_last_name = prop.getProperty("driver_lastName");
            String driver_license_no = prop.getProperty("driver_license");
            String carrier_code = prop.getProperty("carrier_code");
            String vendor = prop.getProperty("vender_");
            String trailer_type = prop.getProperty("trailer_type");

            test_checkIn_happy_flow.info("Let's enter these values in their input fields.");
            // Now let's enter these values in their input fields.
            // 1. Driver First Name

            test_checkIn_happy_flow.info("Entering Driver's First Name as : " + driver_first_name);
            driver.findElement(By.xpath("//input[@name='DFirstName']")).sendKeys(driver_first_name);
            // 2. Driver Last Name
            test_checkIn_happy_flow.info("Entering Driver's Last Name : " + driver_last_name);
            driver.findElement(By.xpath("//input[@name='DlastName']")).sendKeys(driver_last_name);
            // 3. Driver License
            test_checkIn_happy_flow.info("Entering Driver's License number as : " + driver_license_no);
            driver.findElement(By.xpath("//input[@name='DLicenceNo']")).sendKeys(driver_license_no);
            // 4. Carrier Code
            test_checkIn_happy_flow.info("Entering Carrier Code as : " + carrier_code);
            driver.findElement(By.xpath("//input[@name='carrierCode']")).sendKeys(carrier_code);
            // 5. Vendor
            test_checkIn_happy_flow.info("Entering vendor name : " + vendor);
            driver.findElement(By.xpath("//input[@name='shipmentId']")).sendKeys(vendor);

            //6. Trailer Type
            test_checkIn_happy_flow.info("Selecting Trailer Type : " + trailer_type);
            String xpath_trailer_type = "//input[@value=\"%s\"]";
            String xpath_trailer_type_selected = String.format(xpath_trailer_type, trailer_type);

            driver.findElement(By.xpath(xpath_trailer_type_selected)).click();

//            Click on the CheckIn Now button and wait for check In
            driver.findElement(By.xpath("//button[@type='button' and text()='Check In Now']")).click();
            Thread.sleep(4000);

//            Let's verify that the success message is shown in the page correctly.

            WebElement success_message =  driver.findElement(By.xpath("//span[text()='Arrival Completed Successfully.']"));
            if(success_message.isDisplayed()){
               test_checkIn_happy_flow.info("Success message is shown as : " + success_message.getText());
               test_checkIn_happy_flow.pass("Test passed, we've successfully Checked In");
            }
            else
            {
                test_checkIn_happy_flow.fail("Test failed, Something went wrong, since we couldn't see the success message hence CheckIn Failed.");
            }
            // Todo : Ending of CheckIn Feature ...

            // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= Now let's compare the data we've entered and data being displayed for that CheckIn.
            // Todo : Data validation from the earlier CheckIn. Starting......

            ExtentTest test_data_validation_from_earlier_checkIn = extent.createTest("Testing data Validation from the earlier CheckIn.", "In this test we'll be comparing the values between what " +
                    "we've entered during checkIn and the data shown in the Check Out page's cardView.");

            test_data_validation_from_earlier_checkIn.info("Select the Check Out section from top.");
            driver.findElement(By.xpath("//div[@class='navText' and text()='Check Out']")).click();

            System.out.println("We've reached Check Out page where we can see Check In data");

            test_data_validation_from_earlier_checkIn.info("Clicking on View more option.");
            driver.findElement(By.xpath("//*[@id=\"pdf-content\"]/tbody/tr[1]/td[10]/div/button")).click();

            System.out.println("This opened Load Details Modal.");
            Thread.sleep(7000);
            test_data_validation_from_earlier_checkIn.info("Let's match of values from Load Details modal and the values we've entered earlier during CheckIn");

            String name_in_card = driver.findElement(By.xpath("/html/body/div[3]/div/div/div[2]/div[5]/p[2]")).getText();
            boolean matching_name = name_in_card.equals(driver_first_name + " " + driver_last_name);
            System.out.println("Driver name matching : " + matching_name);
            test_data_validation_from_earlier_checkIn.info("Name in Load Details card : " + name_in_card + " & name we've entered during CheckIn : " + driver_first_name + " " + driver_last_name);

            Thread.sleep(3000);

            String phone_no_in_card = driver.findElement(By.xpath("/html/body/div[3]/div/div/div[2]/div[6]/p[2]")).getText();
            String user_entered_phone_no = formatPhoneNumber(driver_phone_no);
            boolean matching_driver_phone_no = phone_no_in_card.equals(user_entered_phone_no);
            System.out.println("Driver Phone number matching : " + matching_driver_phone_no);
            test_data_validation_from_earlier_checkIn.info("Phone number in Load Details card : " + phone_no_in_card + " & Phone number we've entered during CheckIn : " + user_entered_phone_no);

            String license_no_in_card = driver.findElement(By.xpath("/html/body/div[3]/div/div/div[2]/div[7]/p[2]")).getText();
            boolean matching_license_no = license_no_in_card.equals(driver_license_no);
            System.out.println("Driver License number matching : " + matching_license_no);
            test_data_validation_from_earlier_checkIn.info("License number in Load Details card : " + license_no_in_card + " & License number we've entered during CheckIn : " + driver_license_no);
            Thread.sleep(3000);


            String trailer_no_in_card = driver.findElement(By.xpath("/html/body/div[3]/div/div/div[2]/div[14]/p[2]")).getText();
            boolean matching_trailer_no = trailer_no_in_card.equals(driver_trailer_no);
            System.out.println("Trailer number matching : " + matching_trailer_no);
            test_data_validation_from_earlier_checkIn.info("Trailer number in Load Details card : " + trailer_no_in_card + " & Trailer number we've entered during CheckIn : " + driver_trailer_no);

            String trailer_type_in_card = driver.findElement(By.xpath("/html/body/div[3]/div/div/div[2]/div[12]/p[2]")).getText();
            boolean matching_trailer_type = trailer_type_in_card.equals(trailer_type);
            System.out.println("Trailer type matching : " + matching_trailer_type);
            test_data_validation_from_earlier_checkIn.info("Trailer type in Load Details card : " + trailer_type_in_card + " & Trailer type we've selected during CheckIn : " + trailer_type);

            Thread.sleep(3000);



            if(matching_name && matching_license_no && matching_driver_phone_no && matching_trailer_no && matching_trailer_type){
                System.out.println("Test passed, data is preserved during CheckIn");
                test_data_validation_from_earlier_checkIn.pass("Test passed, value are same in the Load Details modal with whatever we've entered earlier during CheckIn");
            }
            else{
                System.out.println("Test failed, data got corrupted during CheckIn");
                test_data_validation_from_earlier_checkIn.fail("Test failed, values are not matching with whatever we've entered earlier during CheckIn");
            }

            // let's close this Load Details modal
            driver.findElement(By.id("close-btn")).click();
            // Todo : Data validation from the earlier CheckIn. Ending......
        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Tests failed , Something went wrong.");
        }

    }
    public static String formatPhoneNumber(String phoneNumber) {
        String areaCode = phoneNumber.substring(0, 3);
        String middleDigits = phoneNumber.substring(3, 6);
        String lastDigits = phoneNumber.substring(6);
        return String.format("(%s) %s-%s", areaCode, middleDigits, lastDigits);
    }
}
