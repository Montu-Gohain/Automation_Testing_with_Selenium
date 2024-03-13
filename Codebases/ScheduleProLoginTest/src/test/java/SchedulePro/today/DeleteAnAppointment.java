package SchedulePro.today;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class DeleteAnAppointment {
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
//          Navigate to My Appointments page.
            Navigate_to_my_appointments(driver, extent);
//            Delete an Appointment
            DeleteAppointment(driver, prop, extent);

            Thread.sleep(3900); // Wait for 3.9 seconds before closing the browser.
            driver.quit();
            extent.flush();
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
    public static void Navigate_to_my_appointments(WebDriver driver,  ExtentReports extent){
        try{
            ExtentTest test_navigate_to_my_appointment = extent.createTest("Navigate to My Appointments Page","In this test we'll check if we're able to navigate to " +
                    "My Appointments Page");
            test_navigate_to_my_appointment.info("Clicking on My Appointments sections from the left menu");
            driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[1]/div/div/ul/li[3]/a/div")).click();
            Thread.sleep(6000);

            WebElement my_appointment_heading = driver.findElement(By.xpath("//h4"));

            if(my_appointment_heading.isDisplayed()){
                test_navigate_to_my_appointment.pass("Test passed, we've successfully navigated to My Appointments page.");
            }
            else{
                test_navigate_to_my_appointment.fail("Test failed, failed to navigate to My Appointments Page.");
            }

        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Tests failed, Something went wrong.");
        }
    }

    public static void DeleteAppointment(WebDriver driver, Properties prop, ExtentReports extent){
        try{
            ExtentTest test_delete_appointment = extent.createTest("Delete an Appointment","In this test we're deleting an Appointment by following its PO number.");

            test_delete_appointment.info("Grabbing the target PO number to Delete from testdata file");
            String target_po_no = prop.getProperty("target_PO_to_delete_appointment");

            List<WebElement> all_appointment_cards = driver.findElements(By.xpath("//div[@class='carrier-po-wrapper p-2']"));

            int total_appointments = all_appointment_cards.size();
            System.out.println("Before delete no. total appointments : " + total_appointments);

            test_delete_appointment.info("Before delete total available Appointments : " + total_appointments );


            //div[@class='carrier-po-wrapper p-2']/div[2]/div/div/div[@class='carrier-po-value' and text()='110301']
            WebElement delete_button = driver.findElement(By.xpath("//div[@class='carrier-po-wrapper p-2']/div[2]/div/div/div[@class='carrier-po-value' and text()='" + target_po_no + "']/parent::*/parent::*/parent::*/div[2]/div/button[1]"));

                test_delete_appointment.info("Clicking on the Delete Icon in the Appointment with PO number : " +  target_po_no);
                delete_button.click();

                Thread.sleep(3000);
                test_delete_appointment.info("Clicking on the Yes button on Confirmation Message pop-up.");
                driver.findElement(By.xpath("//button[text()='Yes']")).click();
                Thread.sleep(15000);
                test_delete_appointment.info("After deleting one Appointment total available Appointments : " +  all_appointment_cards.size() );

               List<WebElement> all_appointment_cards_current = driver.findElements(By.xpath("//div[@class='carrier-po-wrapper p-2']"));

                System.out.println("After deleting an Appointment total available Appointments :  " + all_appointment_cards_current.size());
                if( total_appointments > all_appointment_cards_current.size()){
                    System.out.println("Test passed, we've successfully an appointment.");
                    test_delete_appointment.pass("Test passed, successfully deleted the Appointment with PO : " + target_po_no);
                }
                else{
                    test_delete_appointment.fail("Test failed, failed to delete the Appointment with PO : " + target_po_no);
                    System.out.println("Test failed, we've failed to delete an appointment.");
                }

        }catch (Exception E){
            E.printStackTrace();
            System.out.println("Something went wrong, tests failed.");
        }
    }

}
