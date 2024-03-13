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

public class CheckInEdit {
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
            auto_sign_in(driver, prop,extent);

//            Edit Check In
            edit_check_in(driver, prop, extent);

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
    public static void edit_check_in(WebDriver driver, Properties prop, ExtentReports extent){
        try{

              ExtentTest test_navigate_to_checkout_page = extent.createTest("Navigate to CheckOut Page.", "In this test we're checking whether we're able to navigate to the Check Out page.");

              test_navigate_to_checkout_page.info("Click on Check Out option from the top menu.");
//            Click On Check Out section.
              driver.findElement(By.xpath("//div[@class='navText' and text()='Check Out']")).click();


              WebElement driver_checkout_text = driver.findElement(By.xpath("//div[@class='fw-bold' and text()='Driver Check Out']"));
              if(driver_checkout_text.isDisplayed()){
                  test_navigate_to_checkout_page.pass("Test passed, we've reached Driver Check Out Page successfully.");
              }
              else {
                  test_navigate_to_checkout_page.fail("Failed to reach Driver Check Out Page.");
              }

              String targeted_trailer_no = prop.getProperty("editable_trailer_no");

              ExtentTest test_selecting_trailer_no = extent.createTest("Finding the edit button in the row by giving trailer no." ,"In this test we're finding the edit button in a row in the table by " +
                      "providing the trailer no.");
//            Select Other Load from the selector

             test_selecting_trailer_no.info("Clicking on the Load Selector.");

//            Opening the dropdown list
              driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[3]/div[1]/div[2]/div[1]/div/div/button/div")).click();

              test_selecting_trailer_no.info("Selecting Other Load from the options.");
              driver.findElement(By.xpath("//a[@class='dropdown-item' and text()='Other Load']")).click();

              Thread.sleep(3000);

//            Click on the Pencil icon to edit that trailer data.
              String target_edit_icon_xpath = "//tr/td[text() ='" + targeted_trailer_no + "']//parent::tr/td[9]/div/img[@alt='Edit Po']";

              WebElement edit_button = driver.findElement(By.xpath(target_edit_icon_xpath));

              if(edit_button.isDisplayed()){
                  test_selecting_trailer_no.pass("Test passed, we've found the row with trailer no : " + targeted_trailer_no + ", so that we can edit this row.");
              }
              else{
                  test_selecting_trailer_no.fail("Test failed. No data found for the trailer no : " + targeted_trailer_no);
              }


              edit_button.click();
              Thread.sleep(3000);

//            Verify that the field got updated by clicking on the view more.
            ExtentTest test_data_validation_from_earlier_checkIn = extent.createTest("Testing data Validation after the update.", "In this test we'll be comparing the values between what " +
                    "we've entered during the Update and the data shown in the Check Out page's cardView.");

//            Edit whatever field you want to edit and click on the save button.

            test_data_validation_from_earlier_checkIn.info("Grabbing updated data from testdata file");

              String firstName = prop.getProperty("editable_firstName");
              String lastName = prop.getProperty("editable_lastName");
              String driverLicense = prop.getProperty("editable_driverLicense");
              String carrierCode = prop.getProperty("editable_carrierCode");

              test_data_validation_from_earlier_checkIn.info("Clearing out existing values in the fields to edit and update.");

                // 1. Driver First Name
                clearOutFields(driver, "//input[@name='DFirstName']");
                driver.findElement(By.xpath("//input[@name='DFirstName']")).sendKeys(firstName);

                test_data_validation_from_earlier_checkIn.info("Updating firstname to : " + firstName);

                // 2. Driver Last Name
                clearOutFields(driver, "//input[@name='DlastName']");
                driver.findElement(By.xpath("//input[@name='DlastName']")).sendKeys(lastName);
                test_data_validation_from_earlier_checkIn.info("Updating lastname to : " + lastName);

                // 3. Driver License
                clearOutFields(driver, "//input[@name='DLicenceNo']");
                driver.findElement(By.xpath("//input[@name='DLicenceNo']")).sendKeys(driverLicense);
                test_data_validation_from_earlier_checkIn.info("Updating License No to : " + driverLicense);

                // 4. Carrier Code
                clearOutFields(driver, "//input[@name='carrierCode']");
                driver.findElement(By.xpath("//input[@name='carrierCode']")).sendKeys(carrierCode);
                test_data_validation_from_earlier_checkIn.info("Updating Carrier Code to : " + carrierCode);

                // Click on Save button.
               test_data_validation_from_earlier_checkIn.info("Clicking on the save button.");
                driver.findElement(By.xpath("//button[@type='button' and text()='Save']")).click();

                Thread.sleep(6000);


            System.out.println("We've reached Check Out page where we can see Check In data");

            test_data_validation_from_earlier_checkIn.info("Clicking on View more option.");

            //tr/td[text() ='5432']//parent::tr/td[9]/div/button
            test_data_validation_from_earlier_checkIn.info("Clicking on the view more button in the updated row.");

            driver.findElement(By.xpath("//tr/td[text() ='" + targeted_trailer_no + "']//parent::tr/td[9]/div/button")).click();
            test_data_validation_from_earlier_checkIn.info("View more modal got opened.");
            System.out.println("This opened Load Details Modal.");
            Thread.sleep(7000);
            test_data_validation_from_earlier_checkIn.info("Let's match of values from Load Details modal and the values we've entered earlier during updating the values");

            String name_in_card = driver.findElement(By.xpath("/html/body/div[3]/div/div/div[2]/div[5]/p[2]")).getText();
            boolean matching_name = name_in_card.equals(firstName + " " + lastName);
            System.out.println("Driver name matching : " + matching_name);
            test_data_validation_from_earlier_checkIn.info("Name in Load Details card : " + name_in_card + " & name we've entered during updating the values : " + firstName + " " + lastName);

            Thread.sleep(3000);


            String license_no_in_card = driver.findElement(By.xpath("/html/body/div[3]/div/div/div[2]/div[7]/p[2]")).getText();
            boolean matching_license_no = license_no_in_card.equals(driverLicense);
            System.out.println("Driver License number matching : " + matching_license_no);
            test_data_validation_from_earlier_checkIn.info("License number in Load Details card : " + license_no_in_card + " & License number we've entered during updating the values : " + driverLicense);
            Thread.sleep(3000);

            Thread.sleep(3000);

            String carrierCode_in_card = driver.findElement(By.xpath("/html/body/div[3]/div/div/div[2]/div[9]/p[2]")).getText();
            boolean matching_carrier_code = carrierCode_in_card.equals(carrierCode);
            System.out.println("Driver License number matching : " + matching_carrier_code);
            test_data_validation_from_earlier_checkIn.info("Carrier code in Load Details card : " + carrierCode_in_card + " & Carrier code we've entered during updating the values : " + carrierCode);
            Thread.sleep(3000);

            Thread.sleep(3000);



            if(matching_name && matching_license_no  ){
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
        }
    }
    public static void clearOutFields(WebDriver driver, String xpath){
            driver.findElement(By.xpath(xpath)).sendKeys(Keys.CONTROL, "a");
            driver.findElement(By.xpath(xpath)).sendKeys(Keys.DELETE);
    }
}
