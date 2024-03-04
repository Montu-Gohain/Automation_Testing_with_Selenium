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
public class Delete_PO_Appointment {

    public static void main(String[] args) {
        // Todo : Setting up ExtentReport package to give us test results.
        ExtentReports extent = new ExtentReports();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = sdf.format(new Date());
        String filename = "target/Reports/ExtentReport_testing_PO_appointment_Delete-" + timestamp + ".html";
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
            Navigate_To_PO_management(driver, prop);

//            Step 3 : To Schedule an existing PO which is not yet scheduled.
            Delete_PO_appointment(driver, prop, extent);

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

    public  static void Navigate_To_PO_management(WebDriver driver, Properties prop){
        try{
            System.out.println("Let's navigate to PO Management Page");

            WebElement PO_Management_link = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[1]/div/div/ul/li[4]/a/div"));

            PO_Management_link.click();
            Thread.sleep(6000);

            String expected_url = prop.getProperty("expected_url_for_PO");
            String current_url = driver.getCurrentUrl();

            if(Objects.equals(current_url, expected_url)){
                System.out.println("We've successfully reached PO Management Page");
            }else{
                System.out.println("Failed to Reach PO Management Page");
            }
        }
        catch (Exception E){
            E.printStackTrace();
        }
    }

    public static void Delete_PO_appointment(WebDriver driver, Properties prop, ExtentReports extent){
        try{
            ExtentTest test_Select_PO_to_delete = extent.createTest("Selecting a PO to Delete the currently Scheduled Appointment.", "Selecting the Appointment of a PO from the table in PO Management Page.");

            String targeted_PO_number = prop.getProperty("target_PO_to_delete_appointment");
            test_Select_PO_to_delete.info("Received the target PO with PO Number : " + targeted_PO_number + " from testdata.properties file");

            String target_row_xpath = "//tr//td[text() = '" + targeted_PO_number + "']//parent::tr/td[8]/div";

            test_Select_PO_to_delete.info("Clicking on  Scheduled button");
            driver.findElement(By.xpath(target_row_xpath)).click();

            test_Select_PO_to_delete.info("Waiting for 3 seconds to redirect to a different page , where we can Reschedule this PO.");
            Thread.sleep(3000);


            String PO_number_in_card = driver.findElement(By.xpath("(//div[@class='carrier-po-value'])[1]")).getText();

            if(Objects.equals(PO_number_in_card, targeted_PO_number)){
                System.out.println("Now we can delete this PO");
                test_Select_PO_to_delete.pass("Test passed, we've successfully selected the PO with PO number : " + targeted_PO_number + " to delete.");



                String test1_desc = prop.getProperty("test_delete_PO_desc");
                ExtentTest test_delete_PO_appointment = extent.createTest("Deleting the currently Scheduled appointment for this PO",test1_desc);

                test_delete_PO_appointment.info("Click on the Trash Bin Icon");
                driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[2]/div/div/div[2]/div/div[1]/div/div[2]/div[2]/div/button[1]")).click();

                Thread.sleep(2000);

                test_delete_PO_appointment.info("Click on yes button in the confirmation modal.");
                driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div[3]/div/button[1]")).click();

                test_delete_PO_appointment.info("Wait for 9 seconds to redirect to a different page.");
                Thread.sleep(9000);

                test_delete_PO_appointment.info("Navigate to PO Management page again.");

                Navigate_To_PO_management(driver, prop);
                Thread.sleep(5000);

                test_delete_PO_appointment.info("Checking the current status of Appointment for the PO.");

                String current_appointment_status = driver.findElement(By.xpath(target_row_xpath)).getText();
                System.out.println();

                test_delete_PO_appointment.info("Current status for Appointment of this PO with PO number : " + targeted_PO_number + " is " + current_appointment_status);
                test_delete_PO_appointment.pass("Successfully deleted currently Scheduled Appointment.");
                System.out.println("Scheduled Appointment got deleted successfully.");

            }
            else{
                System.out.println("Couldn't find the targeted PO");
                test_Select_PO_to_delete.fail("Failed to find the PO with PO number : " + targeted_PO_number);
            }

            }
        catch (Exception E){
            E.printStackTrace();
        }
    }
}
