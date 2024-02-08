import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class SignInPageWithExtentReport {

    public static void main(String[] args) {

        ExtentReports extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("target/Reports/ExtentReportsForSignInPage_8th_jan.html");
        extent.attachReporter(spark);

        try { // To Check if the testdata.properties file exist or not, we are using this try catch block

            Properties prop = new Properties();
            File file = new File("testdata.properties");
            FileInputStream fis = new FileInputStream(file);
            prop.load(fis);

            String userName = prop.getProperty("userName");
            String userPassword =prop.getProperty("userPassword");
            String Test_URl = prop.getProperty("testUrl");

            //Todo : Step One -> Visit the URL
            System.out.println("Open Rahul Shetty Academy Sign In Page");
            WebDriver driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.get(Test_URl);

            // Todo : Step two -> Let's try to SignIn the page. From here our extent report should generate.

            ExtentTest testBrowserSignIn = extent.createTest("Rahul Shetty Academy Signin", "SignIn to rahulshettyacademy");

            try {

                // Fill up the username input field
                driver.findElement(By.xpath("/html/body/div/div/div/div/div/div/div[2]/form/input[1]")).sendKeys(userName);
                System.out.println("Entered username");
                testBrowserSignIn.info("Entered username");
                Thread.sleep(1200);

                // Fill up the password input field
                driver.findElement(By.xpath("/html/body/div/div/div/div/div/div/div[2]/form/input[2]")).sendKeys(userPassword);
                System.out.println("Entered password");
                testBrowserSignIn.info("Entered password");
                Thread.sleep(1200);


                // Then check the checkboxes
                driver.findElement(By.xpath("/html/body/div/div/div/div/div/div/div[2]/form/div[1]/span[1]/input")).click();
                driver.findElement(By.xpath("/html/body/div/div/div/div/div/div/div[2]/form/div[1]/span[2]/input")).click();

                System.out.println("Checked the checkboxes");
                testBrowserSignIn.info("Checked the checkboxes.");

                // Lastly click on the SignIn Button
                Thread.sleep(1100);
                driver.findElement(By.xpath("/html/body/div/div/div/div/div/div/div[2]/form/button")).click();
                System.out.println("SignIn Button Clicked");
                testBrowserSignIn.pass("SignIn Button clicked");
               }
                catch (Exception E){
                    E.printStackTrace();
                    System.out.println("SignIn unsuccessful");
                    testBrowserSignIn.fail("SignIn Failed");
                }
            Thread.sleep(3000); // Waiting for 3 seconds after closing the browser.
            driver.quit();
            }
            catch (Exception E) {
                E.printStackTrace();
            }


        System.out.println("Test Completed");
        extent.flush();
    }
}
