package SchedulePro;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

public class ScheduleProLogin {
    public static void main(String[] args) {
        ExtentReports extent = new ExtentReports();
        // Todo : Generate timestamp for the current time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        // Convert the timestamp to string
        String timestamp = sdf.format(new Date());

        // Create dynamic filename with the timestamp.
        String filename = "target/Reports/ExtentReport-" + timestamp + ".html";
        ExtentSparkReporter spark = new ExtentSparkReporter(filename);
        spark.config().setTheme(Theme.DARK);
        extent.attachReporter(spark);
        try {
            // Step 1: Access the testdata.properties file
            Properties prop = new Properties();
            File file = new File("testdata.properties");
            FileInputStream fis = new FileInputStream(file);
            prop.load(fis);

            // Step 2: Extract the values from the testdata.properties file
            String Test_URL = prop.getProperty("scheduleProUrl");
            String userEmail = prop.getProperty("userEmail");
            String userPassword = prop.getProperty("userPass");
            String predicted_url = prop.getProperty("redirectedUrl");

            // Todo: Let's begin our testing.
            // Test 1: Visit the URL first
            WebDriver driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(25));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
            ExtentTest test_visit_url = extent.createTest("Visiting Schedule Pro Login page", "Opening SchedulePro Login Page");
                try {
                    test_visit_url.info("Opening Login page of Schedule Pro");
                    driver.get(Test_URL);
//                    Thread.sleep(20000);
                    String InPage_text_finder = driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div[2]/div[2]/form/h5")).getText();
                    System.out.println(InPage_text_finder);

                    if (InPage_text_finder.equals("Login")) {
                        test_visit_url.pass("We have reached SchedulePro Login Page");
                        System.out.println("Test 1 Passed : We're reached the login page");
                    } else {
                        test_visit_url.fail("Failed to reach SchedulePro Login Page");
                        System.out.println("Test Failed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Failed to visit Schedule pro Login page. Retrying...");
                    try{
                        test_visit_url.info("Opening Login page of Schedule Pro");
//                        Thread.sleep(20000);
                        String InPage_text_finder = driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div[2]/div[2]/form/h5")).getText();
                        System.out.println(InPage_text_finder);

                        if (InPage_text_finder.equals("Login")) {
                            test_visit_url.pass("We have reached SchedulePro Login Page");
                            System.out.println("Test 1 Passed : We're reached the login page");
                        } else {
                            test_visit_url.fail("Failed to reach SchedulePro Login Page");
                            System.out.println("Test Failed");
                        }
                    }
                    catch (Exception E){
                        E.printStackTrace();
                        test_visit_url.fail("Failed to visit SchedulePro Login page");

                    }
                }

            // Test 2: Log In Using the credentials

            ExtentTest test_login = extent.createTest("LogIn Using Credentials", "Let's Log In Using correct Credentials");
                try{
                    // Step 1 : Fill up the form with proper credentials.
                    driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div[2]/div[2]/form/div[1]/div/input")).sendKeys(userEmail);
                    driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div[2]/div[2]/form/div[2]/div/div/input")).sendKeys(userPassword);

                    // Step 2 : Click on the checkbox

                    driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div[2]/div[2]/form/p[2]/input")).click();

                    Thread.sleep(5000);
                    // Step 3 : Click on Login Button
                    driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div[2]/div[2]/form/div[3]/button")).click();
                    test_login.info("LogIn Button Clicked");
                    // Wait for 10 seconds till the redirection happens.

                    Thread.sleep(20000);
                    // Step 4 : Let's see if we've reached the book appointments page.
                    String current_url = driver.getCurrentUrl();
                    System.out.println(current_url);
                    if(current_url.equals(predicted_url)){
                        test_login.pass("Login Successful");
                    }
                    else {
                        test_login.fail("Login Failed");
                    }

                }
                catch (Exception E){
                    E.printStackTrace();
                }

            // Test 3 : Logout from that dashboard page.

            ExtentTest test_logout = extent.createTest("Logout from the page", "Let's logout from SchedulePro" );

                try{
                    // Click on the arrow
                    driver.findElement(By.xpath("/html/body/div/div[2]/div/div[1]/div/ul/li[2]/i")).click();
                    Thread.sleep(3000);
                    // Wait for 3 seconds
                    driver.findElement(By.xpath("/html/body/div/div[2]/div/div[1]/div/ul/li[2]/div[2]/ul/li[6]")).click();
                    test_logout.info("Click on the Logout Option");
                    System.out.println("Logout option clicked , confirm message should appear");

                    Thread.sleep(4000);
                    driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div[3]/div/button[2]")).click();
                    test_logout.info("Logout Confirmation button clicked");

                    Thread.sleep(7000);
                    String final_url = driver.getCurrentUrl();
                    System.out.println(final_url);

                    if(final_url.equals(Test_URL)){
                        test_logout.pass("Logout Test Passed");
                    }
                    else {
                        test_logout.pass("Logout Test Failed");
                    }
                }
                catch (Exception E){
                    E.printStackTrace();
                }


            // Test 4 : Login With Remember me.

            ExtentTest test_login_with_remember_me = extent.createTest("LogIn While Remember me is enabled", "LogIn with Remeber me checked");
                try{
//                    driver.get(Test_URL);
                    Thread.sleep(7000);
                    driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div[2]/div[2]/form/div[3]/button")).click();
                    test_login_with_remember_me.info("LogIn Button Clicked");
                    Thread.sleep(17000);
                    String current_url_ = driver.getCurrentUrl();
                    System.out.println(current_url_ + "After running login in with Remember me.");
                    if(current_url_.equals(predicted_url)){
                        test_login_with_remember_me.pass("Login With Remember Me passed");
                    }
                    else {
                        test_login_with_remember_me.fail("Login with remember me test failed");
                    }
                }catch (Exception E){
                    E.printStackTrace();
                }

            // Closing browser windows.
            Thread.sleep(15000);
            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Test Completed");
        extent.flush();
    }
}
