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
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

public class DriverCheckoutData {
    public static void main(String[] args) {
        // Todo : Setting up ExtentReport package to give us test results.
        ExtentReports extent = new ExtentReports();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = sdf.format(new Date());
        String filename = "target/Reports/CheckOut_data_validation-" + timestamp + ".html";
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
            navigate_to_checkout_data(driver, prop, extent);

            Thread.sleep(3900); // Wait for 3.9 seconds before closing the browser.
            driver.quit();
            extent.flush();
        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Tests failed , something went wrong.");
        }
    }
    public static void auto_sign_in(WebDriver driver, Properties prop ,ExtentReports extent){
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

    public static void navigate_to_checkout_data(WebDriver driver, Properties prop,ExtentReports extent ) {

        try{

            ExtentTest test_navigate_to_driver_checkout = extent.createTest("Navigating to Driver Checkout Page.");
            // Click on Reports option.
            test_navigate_to_driver_checkout.info("Clicking on Reports section");
            driver.findElement(By.xpath("//span[text()=\"Reports\"]")).click();
            Thread.sleep(4000);

            // Clicking on driver checkout.
            test_navigate_to_driver_checkout.info("Selecting Driver Checkout Option from the menu.");
            driver.findElement(By.xpath("//a[@class='dropdown-item' and text()='Driver Checkout']")).click();
            // Choose driver checkout and wait for 5 seconds.
            Thread.sleep(5000);

            WebElement Check_out_text_element = driver.findElement(By.xpath("//div[@class='fw-bold' and text()=\"Driver Checkout\"]"));

            if(Check_out_text_element.isDisplayed()){
                test_navigate_to_driver_checkout.pass("Test passed, we've reached Driver Checkout page.");
            }
            else {
                test_navigate_to_driver_checkout.fail("Failed to navigate to Driver Checkout page.");
            }


            ExtentTest test_checking_checkIn_checkOut_date_time = extent.createTest("Checking the CheckIn and CheckOut date time.", "In this test we're checking if there is any similar " +
                    "date time for CheckOut or CheckIn.");

            test_checking_checkIn_checkOut_date_time.info("Grabbing the target trailer number.");
            String targeted_trailer_no = prop.getProperty("checkout_data_trailer_no");

            // Now compare the Check in and Check out time between two data rows for the same trailer number.
            test_checking_checkIn_checkOut_date_time.info("Checking no. of total rows in the table for trailer no : " + targeted_trailer_no);

            List<WebElement> rows_with_target_trailerNo = driver.findElements(By.xpath("//tr/td[text() = '" + targeted_trailer_no + "']//parent::tr[1]"));

            System.out.println("No. of total Re-Check Ins : " + rows_with_target_trailerNo.size());

            test_checking_checkIn_checkOut_date_time.info("Total rows found with trailer number : " + targeted_trailer_no + " is : " + rows_with_target_trailerNo.size());


            if(rows_with_target_trailerNo.size() > 1){

                test_checking_checkIn_checkOut_date_time.info("Let's check CheckIn and CheckOut Date and Time.");
                HashSet<String> checkOutTimeSet = new HashSet<>();
                boolean isDuplicate = false;

                for(int i=0; i<2; i++) {
                    System.out.println("I am here.");
                    String checkIn_date_XPath = "(//tr/td[text() = '" + targeted_trailer_no + "']//parent::tr[1]/td[5]/div/span[1])[" + (i + 1 )+ "]" ;
                    System.out.println(checkIn_date_XPath);
                    WebElement checkInDateElement = driver.findElement(By.xpath(checkIn_date_XPath));
                    String checkInDate_value = checkInDateElement.getText();

                    System.out.println("Check In Date for row " + (i + 1 )+ ": " + checkInDate_value);
                    System.out.println("I am here.");

                    String checkIn_time_XPath = "(//tr/td[text() = '" + targeted_trailer_no + "']//parent::tr[1]/td[5]/div/span[2])[" +(i + 1 )+ "]";
                    WebElement checkIn_time_Element = driver.findElement(By.xpath(checkIn_time_XPath));
                    String checkIn_time_value = checkIn_time_Element.getText();

                    System.out.println("Check In time for row " + (i + 1 ) + ": " + checkIn_time_value);

                    test_checking_checkIn_checkOut_date_time.info("In  row " + (i + 1 ) +" , CheckIn Date : " + checkInDate_value + " and CheckIn Time : " + checkIn_time_value);

                    // Check out Data
                    String checkOut_date_XPath = "(//tr/td[text() = '" + targeted_trailer_no + "']//parent::tr[1]/td[6]/div/span[1])[" + (i + 1 )+ "]" ;

                    WebElement checkoutDateElement = driver.findElement(By.xpath(checkOut_date_XPath));
                    String check_out_Date_value = checkoutDateElement.getText();

                    System.out.println("Check out Date for row " + (i + 1 )+ ": " + check_out_Date_value);

                    String check_out_time_XPath = "(//tr/td[text() = '" + targeted_trailer_no + "']//parent::tr[1]/td[6]/div/span[2])[" + (i + 1 ) + "]";
                    WebElement check_out_time_Element = driver.findElement(By.xpath(check_out_time_XPath));
                    String check_out_time_value = check_out_time_Element.getText();
                    System.out.println("Check out time for row " + (i + 1 ) + ": " + check_out_time_value);

                    test_checking_checkIn_checkOut_date_time.info("In  row " + (i + 1 ) +" , CheckOut Date : " + check_out_Date_value + " and CheckOut Time : " + check_out_time_value);
                    // Check if the value already exists in the HashSet
                    if (checkOutTimeSet.contains(check_out_time_value)) {
                        isDuplicate = true;
                        break;
                    } else {
                        checkOutTimeSet.add(check_out_time_value);
                    }
                }

                System.out.println("Is there any duplicate check-out time ? " + isDuplicate);

                if(isDuplicate){
                    test_checking_checkIn_checkOut_date_time.info("Since we've found same Checkout time for different CheckIn");
                    test_checking_checkIn_checkOut_date_time.fail("Test failed, due to repeated CheckOut time for different CheckIn entries");
                }
                else{
                    test_checking_checkIn_checkOut_date_time.pass("Test pass, since we've found different CheckOut time for different CheckIn entries");
                }


            }
            else if(rows_with_target_trailerNo.size() == 0){
                System.out.println("There is not Check data for trailer no : " + targeted_trailer_no);
                test_checking_checkIn_checkOut_date_time.fail("Test Failed, since there is no data available for trailer no : " + targeted_trailer_no);
            }
            else{
                test_checking_checkIn_checkOut_date_time.fail("Test Failed, since there is only one row data available for trailer no : " + targeted_trailer_no + " and nothing to compare with.");
                System.out.println("There is Just a single Check In with this trailer no : " + targeted_trailer_no);
            }




            // These Check In and the CheckOut time should be different.
        }
        catch (Exception E){
            E.printStackTrace();
        }

    }

}
