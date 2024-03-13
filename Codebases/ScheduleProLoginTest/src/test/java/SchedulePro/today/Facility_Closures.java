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
import java.util.Properties;

public class Facility_Closures {
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
    public static void navigate_to_facility_closures(WebDriver driver, ExtentReports extent){}

}
