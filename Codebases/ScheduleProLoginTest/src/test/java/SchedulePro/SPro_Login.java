package SchedulePro;

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
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class SPro_Login {
    public static void main(String[] args) {
        // Todo : Setting up ExtentReport package to give us test results.
        ExtentReports extent = new ExtentReports();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = sdf.format(new Date());
        String filename = "target/Reports/ExtentReport_testing_login-" + timestamp + ".html";
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

            String testCase_1_description = prop.getProperty("test_case_1_desc");
            String testCase_2_description = prop.getProperty("test_case_2_desc");
            String testCase_3_description = prop.getProperty("test_case_3_desc");

            // Step 0 : Open the browser and hit the url
            driver.get(TEST_URL);

//            Step 1 : Try to login using invalid email address
            ExtentTest test_input_validation = extent.createTest("Testing Input validation in Schedule Pro Login Page.", testCase_1_description);
            input_validation(driver, prop,  test_input_validation);


//            Step 2 : Try to login using wrong credentials.
            ExtentTest test_wrong_credentials = extent.createTest("Trying to login with wrong credentials.",testCase_2_description );
            authentication_failed(driver, prop,test_wrong_credentials);

//             Step 3 : Login to the home page.
            ExtentTest test_login_happy_flow = extent.createTest("Successfully Logging in with correct credentials.",testCase_3_description );
            Login_happy_flow(driver,  prop, test_login_happy_flow);

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
    public static void input_validation(WebDriver driver,Properties prop,  ExtentTest test_){
        try{
            System.out.println("Testing Input Validation");


            test_.info("Collecting the expected error message for email field from testdata.properties");
            String expected_email_err_message = prop.getProperty("failed_auth_email_msg");


            Thread.sleep(4000);
            test_.info("Giving a random piece of text for the email field and expecting the input validation message to kick in");
            driver.findElement(By.xpath("//input[@placeholder=\"Enter Email\"]")).sendKeys("a");
            Thread.sleep(2000);
            driver.findElement(By.xpath("//input[@placeholder=\"Enter Email\"]")).sendKeys("b");
            driver.findElement(By.xpath("//input[@placeholder=\"Enter Email\"]")).sendKeys("cde");

            test_.info("Entering email :  abcde");
            Thread.sleep(3000);

            test_.info("Extracting out the error message shown below the email field and comparing it with the expected error message");
            String validation_message = driver.findElement(By.className("errorMessage")).getText();

            if(Objects.equals(validation_message, expected_email_err_message)){
                test_.pass("Email validation is working perfectly.");
            }
            else{
                test_.fail("Failed to see any kind of email validation.");
            }

            Thread.sleep(3000);
        }
        catch (Exception E){
            test_.fail("Test failed. Failed to see the email validation message.");
            E.printStackTrace();
        }
    }
    public static void authentication_failed(WebDriver driver,Properties prop, ExtentTest test){
        try{
            System.out.println("Testing out failed authentication with wrong credentials.");
            // Clearing out the input field
            clearInputField(driver);

            test.info("Collecting wrong credentials from testdata.properties file");
            String wrong_cred_email = prop.getProperty("wrong_email");
            String wrong_cred_password = prop.getProperty("wrong_password");

            test.info("Entering email : " + wrong_cred_email);
            driver.findElement(By.xpath("//input[@placeholder=\"Enter Email\"]")).sendKeys(wrong_cred_email);

            test.info("Entering password : " + wrong_cred_password);
            driver.findElement(By.xpath("//input[@name=\"password\"]")).sendKeys(wrong_cred_password);

            test.info("Clicking on the login button and waiting for 8 seconds.");
            driver.findElement(By.className("btn-submit")).click();
            Thread.sleep(8000);


            // At this point we should be able to see the error messages , so let's test out if the error messages are being shown or not.

            test.info("Collecting the expected error message from testdata.properties file");
            String expected_email_err_msg = prop.getProperty("failed_auth_email_msg");
            String expected_password_err_msg = prop.getProperty("failed_auth_password_msg");

            String displayed_email_err_msg = driver.findElement(By.xpath("(//span[@class='errorMessage'])[1]")).getText();
            String displayed_password_err_msg =   driver.findElement(By.xpath("(//span[@class='errorMessage'])[2]")).getText();

            if(Objects.equals(displayed_email_err_msg,expected_email_err_msg) && Objects.equals(displayed_password_err_msg, expected_password_err_msg)){
                test.info("Displaying error message for email : " + displayed_email_err_msg );
                test.info("Displaying error message for password : " + displayed_password_err_msg);
                test.pass("Test passed, expected error messages are displayed.");
            }
            else{
                test.fail("Test failed,since no error message is shown even after failed login due to wrong credentials.");
            }

        }
        catch (Exception E){
            test.fail("Test failed,since no error message is shown even after failed login due to wrong credentials.");
            E.printStackTrace();
        }
    }
    public  static void Login_happy_flow(WebDriver driver,Properties prop,ExtentTest test_){
        try{
            System.out.println("Testing out the happy flow for login with correct credentials.");
            test_.info("Collecting the correct credentials from testdata.properties file");
            String userEmail = prop.getProperty("userEmail");
            String userPassword = prop.getProperty("userPass");

//            Clearing existing input
            clearInputField(driver);
            // Step 1 : Login
            test_.info("Entering email : " + userEmail);
            driver.findElement(By.xpath("//input[@placeholder=\"Enter Email\"]")).sendKeys(userEmail);
            Thread.sleep(2000);

            test_.info("Entering password : " + userPassword);
            driver.findElement(By.xpath("//input[@name=\"password\"]")).sendKeys(userPassword);
            Thread.sleep(2000);
            // Finally click on Login button.
            test_.info("Clicking on the Login button.");
            driver.findElement(By.className("btn-submit")).click();
            Thread.sleep(8000);

            test_.info("Checking if PO Management element is visible on the page.So that we can confirm successful login.");

            WebElement target_element =  driver.findElement(By.xpath("/html/body/div/div[2]/div/div[1]/div/div/ul/li[4]/a/div"));

            if(Objects.equals(target_element.getText(), "PO Management")){
                test_.pass("Login Successful with these credentials.");
            }
            else{
                test_.fail("Test failed, failed to reach book appointments page.");
            }
            System.out.println("We've Reached the home page");
        }
        catch (Exception E){
            test_.fail("Failed to login due to Authorization failure.");
            E.printStackTrace();
            System.out.println("Something went wrong, Test failed");
        }
    }

    public static void clearInputField(WebDriver driver){
        //            Clearing existing input
        System.out.println("Clearing out existing input values.");
        driver.findElement(By.xpath("//input[@placeholder=\"Enter Email\"]")).sendKeys(Keys.CONTROL, "a");
        driver.findElement(By.xpath("//input[@placeholder=\"Enter Email\"]")).sendKeys(Keys.DELETE);


//        Clearing out password field

        driver.findElement(By.xpath("//input[@name=\"password\"]")).sendKeys(Keys.CONTROL, "a");
        driver.findElement(By.xpath("//input[@name=\"password\"]")).sendKeys(Keys.DELETE);
    }

}
