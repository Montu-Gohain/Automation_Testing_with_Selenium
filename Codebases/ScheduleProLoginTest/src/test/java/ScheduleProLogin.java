import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
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

            // Todo: Let's begin our testing.
            // Test 1: Visit the URL first
            WebDriver driver = new ChromeDriver();
            driver.manage().window().maximize();
            ExtentTest test_visit_url = extent.createTest("Visiting Schedule Pro Login page", "Opening SchedulePro Login Page");
            boolean success = false;
            int retries = 3; // Number of retries
            while (!success && retries > 0) {
                try {
                    test_visit_url.info("Opening Login page of Schedule Pro");
                    driver.get(Test_URL);
                    Thread.sleep(19000);
                    String text_finder = driver.findElement(By.xpath("/html/body/div/div[2]/div/div/div[2]/div[2]/form/h5")).getText();
                    System.out.println(text_finder);
                    if (text_finder.equals("Login")) {
                        test_visit_url.pass("We have reached SchedulePro Login Page");
                        success = true;
                        System.out.println("Test Passed");
                    } else {
                        test_visit_url.fail("Failed to reach SchedulePro Login Page");
                        System.out.println("Test Failed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Failed to visit Schedule pro Login page. Retrying...");
                    retries--;
                    if (retries > 0) {
                        Thread.sleep(10000); // Wait for 10 seconds before retrying
                    }
                    test_visit_url.fail("Failed to visit SchedulePro Login page");

                }
            }
            // Closing browser windows.
            Thread.sleep(4000);
            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Test Completed");
        extent.flush();
    }
}
